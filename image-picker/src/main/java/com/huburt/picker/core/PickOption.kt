package com.huburt.picker.core

/**
 * Created by hubert on 2018/5/15.
 *
 */
internal object PickOption {
    var limit: Int = 9
    var min: Int = 1
    var capture: Boolean = true
    var crop: Boolean = false

    fun reset() {
        limit = 9
        min = 1
        capture = true
        crop = false
    }
}