package com.oniverse.fitmap.modules.tracks.api;

import android.content.Context;

import androidx.annotation.NonNull;

import com.oniverse.fitmap.modules.tracks.Track;
import com.oniverse.fitmap.modules.tracks.TrackList;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiClient {
    private static final String BASE_URL = "https://api.openrunner.com/api/v2/";
    private static final int timeout = 30;

    public static void findTracks(int page) {
        findTracks(page, false, null);
    }

    public static void findTracks(int page, boolean downloadGpx, Context context) {
        findTracks(page, downloadGpx, context, () -> null);
    }

    public static void findTracks(int page, boolean downloadGpx, Context context, Callable<Void> callback) {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(timeout, TimeUnit.SECONDS) // Temps d'attente de la connexion
                .readTimeout(timeout, TimeUnit.SECONDS) // Temps d'attente de la lecture des données
                .writeTimeout(timeout, TimeUnit.SECONDS) // Temps d'attente de l'écriture des données
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        OpenRunnerService service = retrofit.create(OpenRunnerService.class);

        service.getTracks(page).enqueue(new Callback<TracksResponse>() {
            @Override
            public void onResponse(@NonNull Call<TracksResponse> call, @NonNull Response<TracksResponse> response) {
                if (response.isSuccessful()) {
                    TracksResponse routeResponse = response.body();
                    if (routeResponse != null) {
                        TrackList.getInstance().addTracks(routeResponse.data);

                        for (Track track : routeResponse.data) {
                            findMoreInfo(track);
                        }

                        if (downloadGpx) {
                            for (Track track : routeResponse.data) {
                                downloadGpxFile(track, context, callback);
                            }
                        }

                        System.gc();
                        Runtime.getRuntime().gc();
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

    public static void findMoreInfo(Track track) {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(timeout, TimeUnit.SECONDS) // Temps d'attente de la connexion
                .readTimeout(timeout, TimeUnit.SECONDS) // Temps d'attente de la lecture des données
                .writeTimeout(timeout, TimeUnit.SECONDS) // Temps d'attente de l'écriture des données
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        OpenRunnerService service = retrofit.create(OpenRunnerService.class);

        service.getTrack(track.id).enqueue(new Callback<TrackResponse>() {
            @Override
            public void onResponse(@NonNull Call<TrackResponse> call, @NonNull Response<TrackResponse> response) {
                if (response.isSuccessful()) {
                    System.out.println("Found more info for track " + track.id);
                    TrackResponse trackResponse = response.body();
                    if (trackResponse != null && trackResponse.data.id == track.id) {
                        System.out.println(trackResponse.data.difficulty);
                        track.difficulty = trackResponse.data.difficulty;
                        // add more info here if needed
                    }
                    System.gc();
                    Runtime.getRuntime().gc();
                }
            }

            @Override
            public void onFailure(@NonNull Call<TrackResponse> call, @NonNull Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public static void setMetaData(Callable<Void> callback) {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(timeout, TimeUnit.SECONDS) // Temps d'attente de la connexion
                .readTimeout(timeout, TimeUnit.SECONDS) // Temps d'attente de la lecture des données
                .writeTimeout(timeout, TimeUnit.SECONDS) // Temps d'attente de l'écriture des données
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
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
                        System.gc();
                        Runtime.getRuntime().gc();
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
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(timeout, TimeUnit.SECONDS) // Temps d'attente de la connexion
                .readTimeout(timeout, TimeUnit.SECONDS) // Temps d'attente de la lecture des données
                .writeTimeout(timeout, TimeUnit.SECONDS) // Temps d'attente de l'écriture des données
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
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
                            if (pathToGpx != null)
                                track.setGpxTrack(new Track.GpxTrack(pathToGpx));

                            System.gc();
                            Runtime.getRuntime().gc();
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