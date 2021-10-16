package com.elewa.nagwaandroidtask.util

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.widget.ProgressBar
import com.elewa.nagwaandroidtask.R
import com.github.ybq.android.spinkit.style.Circle
import javax.inject.Inject

class LoadingView @Inject constructor(activity: Activity) {
    var myDialog: Dialog? = null
    var activity: Activity = activity


    fun showLoading() {
        myDialog = Dialog(activity)
        myDialog?.setContentView(R.layout.dialog_loading)
        myDialog?.setCancelable(false)
        myDialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val progressBar: ProgressBar = myDialog!!.findViewById<ProgressBar>(R.id.spin_kit)
        val wave = Circle()
        progressBar.indeterminateDrawable = wave
        myDialog?.show()
    }

    fun checkLoading(): Boolean {
        return if (myDialog != null) {
            myDialog!!.isShowing
        } else {
            false
        }
    }

    fun dismissLoading() {
        if (myDialog != null) {
            myDialog!!.dismiss()
        }

    }

}