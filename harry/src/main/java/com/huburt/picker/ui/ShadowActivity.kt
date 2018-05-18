package com.huburt.picker.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.huburt.picker.facade.Harry

/**
 * Created by hubert
 *
 * Created on 2017/10/20.
 */
class ShadowActivity : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(javaClass.simpleName, "start")
        startActivityForResult(Intent(this, ImageGridActivity::class.java), 7)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.i(javaClass.simpleName, "onActivityResult")
        Log.i(javaClass.simpleName, "requestCode:$requestCode")
        if (resultCode == Harry.RESULT_OK) {
            val images = Harry.obtainResult(data)
            Harry.resultListener?.onImageResult(if (images === null) ArrayList() else images)
            Harry.resultListener = null
        }
        finish()
    }
}