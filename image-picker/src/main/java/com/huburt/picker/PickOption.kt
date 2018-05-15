package com.huburt.picker

/**
 * Created by hubert on 2018/5/15.
 *
 */
class PickOption {
    var limit: Int = 0
    var min: Int = 0
    var capture: Boolean = true
    var crop: Boolean = false

    private object Holder {
        val optionBuilder = PickOption()
    }

    companion object {
        @JvmStatic
        fun getInstance(): PickOption {
            return Holder.optionBuilder
        }

        @JvmStatic
        fun getClearInstance(): PickOption = getInstance().apply {
            limit = 0
            min = 0
            capture = true
            crop = false
        }
    }
}