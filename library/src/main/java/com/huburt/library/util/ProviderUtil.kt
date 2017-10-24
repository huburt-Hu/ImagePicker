package com.huburt.library.util

import android.content.Context

/**
 * Created by hubert
 *
 * Created on 2017/10/12.
 */
object ProviderUtil {
    fun getFileProviderName(context: Context): String = context.packageName + ".provider"
}