package com.elewa.nagwaandroidtask.data.repository

import androidx.lifecycle.LiveData
import com.elewa.nagwaandroidtask.data.local.ItemDao
import com.elewa.nagwaandroidtask.data.model.ItemModel
import com.elewa.nagwaandroidtask.data.network.ApiHelper
import com.elewa.nagwaandroidtask.util.Resource
import io.reactivex.Single
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val apiHelper: ApiHelper,
    private val itemDao: ItemDao
) {

    fun getRemoteVideos(): Single<List<ItemModel>> {
        return apiHelper.getVideos()
    }


    fun getVideoFromDB(): Flow<List<ItemModel>> {
        return itemDao.get("VIDEO");
    }

    fun getBookFromDB(): Flow<List<ItemModel>> {
        return itemDao.get("PDF");
    }

    suspend fun insert(videos: List<ItemModel>){
        itemDao.insert(videos)
    }


}