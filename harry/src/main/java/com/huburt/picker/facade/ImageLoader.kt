package com.huburt.picker.facade

import android.app.Activity
import android.widget.ImageView

import java.io.Serializable

interface ImageLoader {

    fun loadThumbnail(activity: Activity, path: String?, imageView: ImageView, width: Int, height: Int)

    fun loadGifThumbnail(activity: Activity, path: String?, imageView: ImageView, width: Int, height: Int)

    fun loadImage(activity: Activity, path: String?, imageView: ImageView, width: Int, height: Int)

    fun loadGif(activity: Activity, path: String?, imageView: ImageView, width: Int, height: Int)

}