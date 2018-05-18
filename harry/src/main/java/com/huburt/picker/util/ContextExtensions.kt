package com.huburt.picker.util

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.support.annotation.ColorInt
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.ViewGroup
import android.os.Build
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import com.huburt.picker.R


/**
 * Created by hubert on 2018/5/16.
 *
 */

fun Context.dp2px(dpVal: Float): Float {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal, resources.displayMetrics)
}

fun Context.getStatusBarHeight(): Int {
    var statusHeight = dp2px(30f).toInt()
    try {
        val clazz = Class.forName("com.android.internal.R\$dimen")
        val obj = clazz.newInstance()
        val height = Integer.parseInt(clazz.getField("status_bar_height").get(obj).toString())
        statusHeight = resources.getDimensionPixelSize(height)
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return statusHeight
}

fun Activity.getScreenPix(): DisplayMetrics {
    val displaysMetrics = DisplayMetrics()
    windowManager.defaultDisplay.getMetrics(displaysMetrics)
    return displaysMetrics
}

