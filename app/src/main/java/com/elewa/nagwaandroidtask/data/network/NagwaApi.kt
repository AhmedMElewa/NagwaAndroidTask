package com.elewa.nagwaandroidtask.data.network

import com.elewa.nagwaandroidtask.data.model.ItemModel
import io.reactivex.Single
import retrofit2.http.GET

interface NagwaApi {

    @GET("movies")
    fun getVideos(): Single<List<ItemModel>>
}