package com.huburt.picker.provider

import android.content.Context
import android.support.v4.content.FileProvider

/**
 * 自定义一个Provider，以免和引入的项目的provider冲突
 *
 * Author: nanchen
 * Email: liushilin520@foxmail.com
 * Date: 2017-03-17  16:10
 */

class HarryProvider : FileProvider() {
    companion object {
        fun getAuthorities(context: Context): String = context.packageName + ".harry.provider"
    }
}
