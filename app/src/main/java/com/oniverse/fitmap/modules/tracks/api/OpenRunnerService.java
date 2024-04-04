package com.oniverse.fitmap.modules.tracks.api;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface OpenRunnerService {
    @GET("routes")
    Call<TracksResponse> getTracks(@Query("page") int page);

    @GET("routes/{id}/export/gpx-track")
    Call<ResponseBody> downloadGpxFile(@Path("id") long trackId);
}
