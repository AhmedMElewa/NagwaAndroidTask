package com.elewa.nagwaandroidtask.data.network

import com.elewa.nagwaandroidtask.data.model.ItemModel
import io.reactivex.Single
import javax.inject.Inject

class ApiHelperImpl @Inject constructor(private val apiService: NagwaApi) :
    ApiHelper {
    override fun getVideos(): Single<List<ItemModel>> {
        return apiService.getVideos()
    }


}