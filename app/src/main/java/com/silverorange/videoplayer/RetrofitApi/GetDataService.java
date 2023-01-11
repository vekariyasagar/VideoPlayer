package com.silverorange.videoplayer.RetrofitApi;

import com.silverorange.videoplayer.Model.DataModel;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;

public interface GetDataService {

    // API Interface

    @GET("videos")
    Call<ArrayList<DataModel>> getVideosData();

}
