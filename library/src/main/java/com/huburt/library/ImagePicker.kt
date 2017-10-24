package com.huburt.library

import android.content.Context
import com.huburt.library.bean.ImageItem
import com.huburt.library.loader.ImageLoader
import com.huburt.library.ui.ShadowActivity

/**
 * Created by hubert
 *
 * Created on 2017/10/12.
 */
object ImagePicker {
    init {
        println("imagePicker init ...")
    }

    var imageLoader: ImageLoader? = null

    var pickHelper: PickHelper = PickHelper()

    var listener: ImagePicker.OnPickImageResultListener? = null

    /**
     * 在Application中初始化图片加载框架
     */
    fun init(imageLoader: ImageLoader) {
        this.imageLoader = imageLoader
    }

    /**
     * 准备图片选择，初始化参数配置
     */
    fun prepare(): ImagePicker {
        pickHelper = PickHelper()
        return this
    }

    /**
     * 重置图片选择参数
     */
    fun resetConfig() {
        pickHelper = PickHelper()
    }

    fun limit(max: Int): ImagePicker {
        pickHelper.limit = max
        return this
    }

    fun showCamera(boolean: Boolean): ImagePicker {
        pickHelper.isShowCamera = boolean
        return this
    }

    fun multiMode(boolean: Boolean): ImagePicker {
        pickHelper.isMultiMode = boolean
        return this
    }

    fun pick(context: Context, listener: OnPickImageResultListener) {
        checkImageLoader()
        this.listener = listener
        ShadowActivity.start(context, 0, 0)
    }

    fun review(context: Context, position: Int, listener: OnPickImageResultListener) {
        checkImageLoader()
        this.listener = listener
        ShadowActivity.start(context, 1, position)
    }

    private fun checkImageLoader() {
        if (imageLoader == null) {
            throw IllegalArgumentException("""imagePicker has not init,please call "ImagePicker.init(xx)" in your Application's onCreate """)
        }
    }

    interface OnPickImageResultListener {
        fun onImageResult(imageItems: ArrayList<ImageItem>)

    }
}
