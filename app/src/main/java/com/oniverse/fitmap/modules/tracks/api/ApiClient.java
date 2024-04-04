package com.oniverse.fitmap.modules.tracks.api;

import android.content.Context;

import androidx.annotation.NonNull;

import com.oniverse.fitmap.modules.MapRenderer;
import com.oniverse.fitmap.modules.gpxparser.Gpx;
import com.oniverse.fitmap.modules.tracks.Track;
import com.oniverse.fitmap.modules.tracks.TrackList;

import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiClient {
    private static final String BASE_URL = "https://api.openrunner.com/api/v2/";

    public static void findTracks(int page) {
        findTracks(page, false, null, null);
    }

    //TODO: If page = -1 (or other), then download all pages (use threads)
    public static void findTracks(int page, boolean downloadGpx, Context context, MapRenderer mapRenderer) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        OpenRunnerService service = retrofit.create(OpenRunnerService.class);

        service.getTracks(page).enqueue(new Callback<TracksResponse>() {
            @Override
            public void onResponse(@NonNull Call<TracksResponse> call, @NonNull Response<TracksResponse> response) {
                if (response.isSuccessful()) {
                    TracksResponse routeResponse = response.body();
                    if (routeResponse != null) {
                        if (downloadGpx) {
                            for (Track track : routeResponse.data) {
                                downloadGpxFile(track, context, mapRenderer);
                            }
                        }
                        TrackList.getInstance().addTracks(routeResponse.data);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<TracksResponse> call, @NonNull Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public static void downloadGpxFile(Track track, Context context, MapRenderer mapRenderer) {
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
                                track.setGpx(Gpx.readGpxFile(pathToGpx), mapRenderer);
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        t.printStackTrace();
                    }
                });
    }
}