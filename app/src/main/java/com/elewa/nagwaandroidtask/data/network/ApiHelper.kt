package com.elewa.nagwaandroidtask.data.network

import com.elewa.nagwaandroidtask.data.model.ItemModel
import io.reactivex.Single

interface ApiHelper {
    fun getVideos(): Single<List<ItemModel>>
}