package com.huburt.picker.util

import android.app.Activity
import android.content.Context
import android.util.DisplayMetrics

/**
 * Created by hubert on 2018/5/16.
 *
 */

fun Activity.getScreenPix(): DisplayMetrics {
    val displaysMetrics = DisplayMetrics()
    windowManager.defaultDisplay.getMetrics(displaysMetrics)
    return displaysMetrics
}
