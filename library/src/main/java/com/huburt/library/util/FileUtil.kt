package com.huburt.library.util

import android.content.Context
import java.io.File

/**
 * Created by hubert
 *
 * Created on 2017/10/31.
 */
object FileUtil {
    fun getCropCacheFolder(context: Context): File {
        return File(context.cacheDir.toString() + "/ImagePicker/cropTemp/")
    }
}