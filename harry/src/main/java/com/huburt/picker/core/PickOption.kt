package com.huburt.picker.core

import com.huburt.picker.facade.ImageLoader
import com.huburt.picker.util.InitializationCheck

/**
 * Created by hubert on 2018/5/15.
 *
 */
internal object PickOption {
    var imageLoader: ImageLoader by InitializationCheck(
            "please call 'Harry.initImageLoader(XX)' in your application's onCreate")

    val selectedCollection: SelectedItemCollection = SelectedItemCollection()

    var singleMode: Boolean = false
    var limit: Int = 9
    var min: Int = 1
    var capture: Boolean = true
    var openCamera: Boolean = false
    var crop: Boolean = false
    var gridExpectedSize: Float = 0F
    var spanCount: Int = 3

    fun reset() {
        selectedCollection.clear()
        singleMode = false
        limit = 9
        min = 1
        capture = true
        openCamera = false
        crop = false
        gridExpectedSize = 0F
        spanCount = 3
    }
}