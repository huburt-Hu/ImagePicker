package com.huburt.picker.facade

import android.content.Intent
import com.huburt.picker.core.PickOption
import com.huburt.picker.ui.ImageGridActivity

/**
 * Created by hubert on 2018/5/15.
 *
 */
class OptionBuilder {

    init {
        PickOption.reset()
    }

    fun singleMode(): OptionBuilder {
        PickOption.singleMode = true
        PickOption.limit = 1
        return this
    }

    fun limit(int: Int): OptionBuilder {
        PickOption.limit = int
        return this
    }

    fun min(int: Int): OptionBuilder {
        PickOption.min = int
        return this
    }

    fun capture(boolean: Boolean): OptionBuilder {
        PickOption.capture = boolean
        return this
    }

    fun crop(boolean: Boolean): OptionBuilder {
        PickOption.crop = boolean
        return this
    }

    fun forResult(requestCode: Int) {
        val activity = Harry.getActivity() ?: return
        val intent = Intent(activity, ImageGridActivity::class.java)
        val fragment = Harry.getFragment()
        if (fragment == null) {
            activity.startActivityForResult(intent, requestCode)
        } else {
            fragment.startActivityForResult(intent, requestCode)
        }
    }

    fun openCamera(requestCode: Int) {
        PickOption.openCamera = true
        forResult(requestCode)
    }
}