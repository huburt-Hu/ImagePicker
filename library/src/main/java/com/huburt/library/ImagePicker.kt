package com.huburt.library

import android.content.Context
import com.huburt.library.bean.ImageItem
import com.huburt.library.loader.ImageLoader
import com.huburt.library.ui.ShadowActivity
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * Created by hubert
 *
 * Created on 2017/10/12.
 */
object ImagePicker {
    init {
        println("imagePicker init ...")
    }

    internal var imageLoader: ImageLoader by InitializationCheck()

    internal var pickHelper: PickHelper = PickHelper()

    internal var listener: ImagePicker.OnPickImageResultListener? = null

    /**
     * 在Application中初始化图片加载框架
     */
    @JvmStatic
    fun init(imageLoader: ImageLoader) {
        this.imageLoader = imageLoader
    }

    /**
     * 准备图片选择，初始化参数配置
     */
    @JvmStatic
    fun prepare(): ImagePicker {
        pickHelper = PickHelper()
        return this
    }

    /**
     * 重置默认图片选择参数，及图片选择记录
     */
    @JvmStatic
    fun clear() {
        pickHelper = PickHelper()
    }

    /**
     * 图片数量限制，默认9张
     */
    @JvmStatic
    fun limit(max: Int): ImagePicker {
        pickHelper.limit = max
        return this
    }

    /**
     * 是否显示相机，默认显示
     */
    @JvmStatic
    fun showCamera(boolean: Boolean): ImagePicker {
        pickHelper.isShowCamera = boolean
        return this
    }

    /**
     * 是否多选,默认显示
     */
    @JvmStatic
    fun multiMode(boolean: Boolean): ImagePicker {
        pickHelper.isMultiMode = boolean
        return this
    }

    /**
     * 是否裁剪
     */
    @JvmStatic
    fun isCrop(boolean: Boolean): ImagePicker {
        pickHelper.isCrop = boolean
        return this
    }

    @JvmStatic
    fun pick(context: Context, listener: OnPickImageResultListener) {
        this.listener = listener
        ShadowActivity.start(context, 0, 0)
    }

    @JvmStatic
    fun camera(context: Context, listener: OnPickImageResultListener) {
        this.listener = listener
        ShadowActivity.start(context, 2, 0)
    }

    @JvmStatic
    fun review(context: Context, position: Int, listener: OnPickImageResultListener) {
        this.listener = listener
        ShadowActivity.start(context, 1, position)
    }

    interface OnPickImageResultListener {
        fun onImageResult(imageItems: ArrayList<ImageItem>)

    }
}