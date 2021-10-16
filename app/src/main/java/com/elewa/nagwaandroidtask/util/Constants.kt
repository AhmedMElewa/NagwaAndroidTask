package com.elewa.nagwaandroidtask.util

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import androidx.fragment.app.FragmentActivity

object Constants {
    const val BASE_URL = "https://nagwa.free.beeceptor.com/"
    const val DATABASE_NAME = "nagawa_db"
    var DOWNLOADFLAG = false

    fun verifyAvailableNetwork(activity: Activity):Boolean{
        val connectivityManager=activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo=connectivityManager.activeNetworkInfo
        return  networkInfo!=null && networkInfo.isConnected
    }
}