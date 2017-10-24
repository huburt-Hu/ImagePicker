package com.huburt.library.loader

import android.app.Activity
import android.widget.ImageView

import java.io.Serializable

interface ImageLoader : Serializable {

    fun displayImage(activity: Activity, path: String, imageView: ImageView, width: Int, height: Int)

    fun displayImagePreview(activity: Activity, path: String, imageView: ImageView, width: Int, height: Int)

    fun clearMemoryCache()
}