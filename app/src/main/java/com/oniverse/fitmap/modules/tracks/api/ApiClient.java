package com.oniverse.fitmap.modules.tracks.api;

import android.content.Context;

import androidx.annotation.NonNull;

import com.oniverse.fitmap.modules.MapRenderer;
import com.oniverse.fitmap.modules.gpxparser.Gpx;
import com.oniverse.fitmap.modules.tracks.Track;
import com.oniverse.fitmap.modules.tracks.TrackList;

import java.util.concurrent.Callable;

import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiClient {
    private static final String BASE_URL = "https://api.openrunner.com/api/v2/";

    public static void findTracks(int page) {
        findTracks(page, false, null);
    }

    public static void findTracks(int page, boolean downloadGpx, Context context) {
        findTracks(page, downloadGpx, context, () -> null);
    }

    public static void findTracks(int page, boolean downloadGpx, Context context, Callable<Void> callback) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        OpenRunnerService service = retrofit.create(OpenRunnerService.class);

        service.getTracks(page).enqueue(new Callback<TracksResponse>() {
            @Override
            public void onResponse(@NonNull Call<TracksResponse> call, @NonNull Response<TracksResponse> response) {
                System.out.println("Response: " + response.code());
                if (response.isSuccessful()) {
                    TracksResponse routeResponse = response.body();
                    if (routeResponse != null) {
                        System.out.println("tout est ok");
                        if (downloadGpx) {
                            for (Track track : routeResponse.data) {
                                System.out.println("Downloading GPX file for track " + track.id);
                                downloadGpxFile(track, context, callback);
                            }
                        }
                        TrackList.getInstance().addTracks(routeResponse.data);
                        try {
                            callback.call();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<TracksResponse> call, @NonNull Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public static void setMetaData(Callable<Void> callback) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        OpenRunnerService service = retrofit.create(OpenRunnerService.class);

        service.getTracks(1).enqueue(new Callback<TracksResponse>() {
            @Override
            public void onResponse(@NonNull Call<TracksResponse> call, @NonNull Response<TracksResponse> response) {
                if (response.isSuccessful()) {
                    TracksResponse routeResponse = response.body();
                    if (routeResponse != null) {
                        TrackList.getInstance().setApiMetaData(
                                new TrackList.ApiMetaData(routeResponse.meta.last_page,
                                        routeResponse.meta.total, routeResponse.meta.per_page));
                        try {
                            callback.call();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<TracksResponse> call, @NonNull Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public static void downloadGpxFile(Track track, Context context) {
        downloadGpxFile(track, context, () -> null);
    }

    public static void downloadGpxFile(Track track, Context context, Callable<Void> callback) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        OpenRunnerService service = retrofit.create(OpenRunnerService.class);
                service.downloadGpxFile(track.id).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            assert response.body() != null;
                            String pathToGpx = GpxDownloader.saveGpx(track.id, response.body(), context);
                            System.out.println("Downloaded GPX file to: " + pathToGpx);
                            System.out.println("-----------------------------------");
                            if (pathToGpx != null)
                                track.setGpx(Gpx.readGpxFile(pathToGpx));

                            try {
                                callback.call();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        t.printStackTrace();
                    }
                });
    }
}