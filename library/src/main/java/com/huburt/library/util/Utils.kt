package com.huburt.library.util

import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.Environment
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.Display
import android.view.KeyCharacterMap
import android.view.KeyEvent
import android.view.ViewConfiguration
import android.view.WindowManager


object Utils {

    /** 获得状态栏的高度  */
    @JvmStatic
    fun getStatusHeight(context: Context): Int {
        var statusHeight = -1
        try {
            val clazz = Class.forName("com.android.internal.R\$dimen")
            val obj = clazz.newInstance()
            val height = Integer.parseInt(clazz.getField("status_bar_height").get(obj).toString())
            statusHeight = context.resources.getDimensionPixelSize(height)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return statusHeight
    }

    /** 根据屏幕宽度与密度计算GridView显示的列数， 最少为三列，并获取Item宽度  */
    @JvmStatic
    fun getImageItemWidth(activity: Activity): Int {
        val screenWidth = activity.resources.displayMetrics.widthPixels
        val densityDpi = activity.resources.displayMetrics.densityDpi
        var cols = screenWidth / densityDpi
        cols = if (cols < 3) 3 else cols
        val columnSpace = (2 * activity.resources.displayMetrics.density).toInt()
        return (screenWidth - columnSpace * (cols - 1)) / cols
    }

    /**
     * 判断SDCard是否可用
     */
    @JvmStatic
    fun existSDCard(): Boolean {
        return Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
    }

    /**
     * 获取手机大小（分辨率）
     */
    @JvmStatic
    fun getScreenPix(activity: Activity): DisplayMetrics {
        val displaysMetrics = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(displaysMetrics)
        return displaysMetrics
    }

    /** dp转px  */
    @JvmStatic
    fun dp2px(context: Context, dpVal: Float): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal, context.resources.displayMetrics).toInt()
    }

    /**
     * 判断手机是否含有虚拟按键  99%
     */
    @JvmStatic
    fun hasVirtualNavigationBar(context: Context): Boolean {
        var hasSoftwareKeys = true

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            val d = (context.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay

            val realDisplayMetrics = DisplayMetrics()
            d.getRealMetrics(realDisplayMetrics)

            val realHeight = realDisplayMetrics.heightPixels
            val realWidth = realDisplayMetrics.widthPixels

            val displayMetrics = DisplayMetrics()
            d.getMetrics(displayMetrics)

            val displayHeight = displayMetrics.heightPixels
            val displayWidth = displayMetrics.widthPixels

            hasSoftwareKeys = realWidth - displayWidth > 0 || realHeight - displayHeight > 0
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            val hasMenuKey = ViewConfiguration.get(context).hasPermanentMenuKey()
            val hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK)
            hasSoftwareKeys = !hasMenuKey && !hasBackKey
        }

        return hasSoftwareKeys
    }

    /**
     * 获取导航栏高度，有些没有虚拟导航栏的手机也能获取到，建议先判断是否有虚拟按键
     */
    @JvmStatic
    fun getNavigationBarHeight(context: Context): Int {
        val resourceId = context.resources.getIdentifier("navigation_bar_height", "dimen", "android")
        return if (resourceId > 0) context.resources.getDimensionPixelSize(resourceId) else 0
    }
}
