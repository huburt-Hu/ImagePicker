package com.huburt.library.util

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import android.util.Log
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by hubert
 *
 * Created on 2017/10/20.
 */
object CameraUtil {
    fun takePicture(activity: Activity, requestCode: Int): File {
        var takeImageFile =
                if (Utils.existSDCard())
                    File(Environment.getExternalStorageDirectory(), "/DCIM/camera/")
                else
                    Environment.getDataDirectory()
        takeImageFile = createFile(takeImageFile, "IMG_", ".jpg")
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        takePictureIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        if (takePictureIntent.resolveActivity(activity.packageManager) != null) {

            // 默认情况下，即不需要指定intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            // 照相机有自己默认的存储路径，拍摄的照片将返回一个缩略图。如果想访问原始图片，
            // 可以通过dat extra能够得到原始图片位置。即，如果指定了目标uri，data就没有数据，
            // 如果没有指定uri，则data就返回有数据！

            val uri: Uri
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
                uri = Uri.fromFile(takeImageFile)
            } else {
                // 7.0 调用系统相机拍照不再允许使用Uri方式，应该替换为FileProvider
                // 并且这样可以解决MIUI系统上拍照返回size为0的情况
                uri = FileProvider.getUriForFile(activity, ProviderUtil.getFileProviderName(activity), takeImageFile)
                //加入uri权限 要不三星手机不能拍照
                val resInfoList = activity.packageManager.queryIntentActivities(takePictureIntent, PackageManager.MATCH_DEFAULT_ONLY)
                resInfoList
                        .map { it.activityInfo.packageName }
                        .forEach { activity.grantUriPermission(it, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION) }
            }
            Log.e("nanchen", ProviderUtil.getFileProviderName(activity))
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
        }
        activity.startActivityForResult(takePictureIntent, requestCode)
        return takeImageFile
    }

    /**
     * 根据系统时间、前缀、后缀产生一个文件
     */
    fun createFile(folder: File, prefix: String, suffix: String): File {
        if (!folder.exists() || !folder.isDirectory) folder.mkdirs()
        val dateFormat = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA)
        val filename = prefix + dateFormat.format(Date(System.currentTimeMillis())) + suffix
        return File(folder, filename)
    }
}