package com.elewa.nagwaandroidtask.data.repository

import com.elewa.nagwaandroidtask.data.model.ItemModel
import com.elewa.nagwaandroidtask.data.network.ApiHelper
import io.reactivex.Single
import javax.inject.Inject

class MainRepository @Inject constructor(private val apiHelper: ApiHelper) {

    fun getRemoteVideos(): Single<List<ItemModel>> {
        return apiHelper.getVideos()
    }

}