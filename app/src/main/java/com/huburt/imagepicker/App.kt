package com.huburt.imagepicker

import android.app.Application
import com.huburt.picker.facade.Harry

/**
 * Created by hubert
 *
 * Created on 2017/10/24.
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        Harry.initImageLoader(GlideImageLoader())
    }
}