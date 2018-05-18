package com.huburt.picker.facade

import android.app.Activity
import android.content.Intent
import android.support.v4.app.Fragment
import com.huburt.picker.bean.ImageItem
import com.huburt.picker.core.PickOption
import java.lang.ref.SoftReference
import java.lang.ref.WeakReference
import java.util.*

/**
 * Created by hubert on 2018/5/16.
 *
 */
object Harry {
    const val RESULT_OK = Activity.RESULT_OK

    internal const val EXTRA_IMAGE_ITEMS = "extra_image_items"
    internal const val EXTRA_POSITION = "extra_position"

    private var mActivity: WeakReference<Activity>? = null
    private var mFragment: WeakReference<Fragment>? = null
    internal var resultListener: OnPickImageResultListener? = null

    @JvmStatic
    fun with(activity: Activity): OptionBuilder {
        mActivity = WeakReference<Activity>(activity)
        return OptionBuilder()
    }

    @JvmStatic
    fun with(fragment: Fragment): OptionBuilder {
        mFragment = WeakReference<Fragment>(fragment)
        mActivity = WeakReference<Activity>(fragment.activity)
        return OptionBuilder()
    }

    fun initImageLoader(imageLoader: ImageLoader) {
        PickOption.imageLoader = imageLoader
    }

    internal fun getActivity(): Activity? {
        return mActivity?.get()
    }

    internal fun getFragment(): Fragment? {
        return mFragment?.get()
    }


    fun obtainResult(data: Intent?): ArrayList<ImageItem>? {
        return data?.getParcelableArrayListExtra<ImageItem>(EXTRA_IMAGE_ITEMS)
    }
}