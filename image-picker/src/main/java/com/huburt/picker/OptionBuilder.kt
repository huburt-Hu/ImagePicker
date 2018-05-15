package com.huburt.picker

import android.content.Intent
import com.huburt.picker.ui.ImageGridActivity

/**
 * Created by hubert on 2018/5/15.
 *
 */
class OptionBuilder(val imagePicker: ImagePicker) {

    private val option: PickOption = PickOption.getClearInstance()

    fun limit(int: Int): OptionBuilder {
        option.limit = int
        return this
    }

    fun capture(boolean: Boolean): OptionBuilder {
        option.capture = boolean
        return this
    }


    fun forResult(requestCode: Int) {
        val activity = ImagePicker.getActivity() ?: return
        val intent = Intent(activity, ImageGridActivity::class.java)
        val fragment = ImagePicker.getFragment()
        if (fragment == null) {
            activity.startActivityForResult(intent, requestCode)
        } else {
            fragment.startActivityForResult(intent, requestCode)
        }
    }
}