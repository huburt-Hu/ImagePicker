package com.huburt.picker.util

import android.app.Activity
import android.content.Context
import android.util.DisplayMetrics
import android.util.TypedValue

/**
 * Created by hubert on 2018/5/16.
 *
 */

fun Context.dp2px(dpVal: Float): Float {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal, resources.displayMetrics)
}

fun Activity.getScreenPix(): DisplayMetrics {
    val displaysMetrics = DisplayMetrics()
    windowManager.defaultDisplay.getMetrics(displaysMetrics)
    return displaysMetrics
}