package com.huburt.imagepicker

import android.app.Activity
import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.huburt.picker.R
import com.huburt.picker.facade.ImageLoader
import java.io.File

/**
 * Created by hubert
 *
 * Created on 2017/10/12.
 */
class GlideImageLoader : ImageLoader {
    override fun loadThumbnail(activity: Activity, path: String, imageView: ImageView, width: Int, height: Int) {
        Glide.with(activity)                             //配置上下文
                .load(Uri.fromFile(File(path))) //设置图片路径(fix #8,文件名包含%符号 无法识别和显示)
                .asBitmap()
                .override(width, height)
                .thumbnail(0.2f)
                .error(R.drawable.ic_default_image)           //设置错误图片
                .placeholder(R.drawable.ic_default_image)     //设置占位图片
                .into(imageView)
    }

    override fun loadGifThumbnail(activity: Activity, path: String, imageView: ImageView, width: Int, height: Int) {
        Glide.with(activity)                             //配置上下文
                .load(Uri.fromFile(File(path)))      //设置图片路径(fix #8,文件名包含%符号 无法识别和显示)
                .asBitmap()
                .thumbnail(0.2f)
                .override(width, height)
                .into(imageView)
    }

    override fun loadImage(activity: Activity, path: String, imageView: ImageView, width: Int, height: Int) {
        Glide.with(activity)                             //配置上下文
                .load(Uri.fromFile(File(path)))      //设置图片路径(fix #8,文件名包含%符号 无法识别和显示)
                .override(width, height)
                .error(R.drawable.ic_default_image)           //设置错误图片
                .placeholder(R.drawable.ic_default_image)     //设置占位图片
                .into(imageView)
    }

    override fun loadGif(activity: Activity, path: String, imageView: ImageView, width: Int, height: Int) {
        Glide.with(activity)                             //配置上下文
                .load(Uri.fromFile(File(path)))      //设置图片路径(fix #8,文件名包含%符号 无法识别和显示)
                .into(imageView)
    }

}