package com.huburt.picker

import android.app.Activity
import android.support.v4.app.Fragment
import com.huburt.picker.core.OptionBuilder
import java.lang.ref.WeakReference

/**
 * Created by hubert on 2018/5/16.
 *
 */
object Harry {
    private var mActivity: WeakReference<Activity>? = null
    private var mFragment: WeakReference<Fragment>? = null

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

    internal fun getActivity(): Activity? {
        return mActivity?.get()
    }

    internal fun getFragment(): Fragment? {
        return mFragment?.get()
    }
}