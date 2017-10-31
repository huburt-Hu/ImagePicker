package com.huburt.library.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.huburt.library.C
import com.huburt.library.ImagePicker
import com.huburt.library.bean.ImageItem

/**
 * Created by hubert
 *
 * Created on 2017/10/20.
 */
class ShadowActivity : BaseActivity() {

    private var type: Int = 0//0pick 1review 2camera
    private var position: Int = 0

    companion object {
        fun start(context: Context, type: Int, position: Int) {
            val intent = Intent(context, ShadowActivity::class.java)
            intent.putExtra(C.EXTRA_TYPE, type)
            intent.putExtra(C.EXTRA_POSITION, position)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        type = intent.extras[C.EXTRA_TYPE] as Int
        position = intent.extras[C.EXTRA_POSITION] as Int
        startPick()
    }

    private fun startPick() {
        when (type) {
            0 -> ImageGridActivity.startForResult(this, 101, false)
            1 -> ImagePreviewDelActivity.startForResult(this, 102, position)
            2 -> ImageGridActivity.startForResult(this, 101, true)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val historyImages = ImagePicker.pickHelper.historyImages
        if (resultCode == Activity.RESULT_OK && data != null) {
            val images = data.extras[C.EXTRA_IMAGE_ITEMS] as ArrayList<ImageItem>
            historyImages.let {
                it.clear()
                it.addAll(images)
            }
            ImagePicker.listener?.onImageResult(images)
        } else if (resultCode == Activity.RESULT_CANCELED) {
            ImagePicker.pickHelper.selectedImages.let {
                it.clear()
                it.addAll(historyImages)
            }
            ImagePicker.listener?.onImageResult(historyImages)
        }
        ImagePicker.listener = null
        finish()
    }
}