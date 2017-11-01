package com.huburt.library

import android.content.Context
import com.huburt.library.ImagePicker.pickHelper
import com.huburt.library.bean.ImageItem
import com.huburt.library.loader.ImageLoader
import com.huburt.library.ui.ShadowActivity
import com.huburt.library.view.CropImageView
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

    internal var imageLoader: ImageLoader by InitializationCheck("imageLoader is not initialized, please call 'ImagePicker.init(XX)' in your application's onCreate")

    internal var pickHelper: PickHelper = PickHelper()

    private var customPickHelper: PickHelper? = null

    internal var listener: ImagePicker.OnPickImageResultListener? = null

    /**
     * 在Application中初始化图片加载框架
     */
    @JvmStatic
    fun init(imageLoader: ImageLoader) {
        this.imageLoader = imageLoader
    }

    /**
     * 图片选择参数恢复默认，如有自定义默认（saveAsDefault方法保存）优先回复自定义默认
     */
    @JvmStatic
    fun defaultConfig(): ImagePicker {
        pickHelper = customPickHelper?.copy() ?: PickHelper()
        return this
    }

    /**
     * 当编辑过参数保存为自定义默认
     */
    @JvmStatic
    fun saveAsDefault(): ImagePicker {
        customPickHelper = pickHelper
        return this
    }

    /**
     * 清楚缓存的已选择图片
     */
    @JvmStatic
    fun clear() {
        pickHelper.selectedImages.clear()
        pickHelper.historyImages.clear()
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

    /**
     * @param focusStyle 裁剪框的形状
     * @param focusWidth 焦点框的宽度
     * @param focusHeight 焦点框的高度
     * @param outPutX 裁剪保存宽度
     * @param outPutY 裁剪保存高度
     * @param isSaveRectangle 裁剪后的图片是否是矩形，否者跟随裁剪框的形状
     */
    @JvmStatic
    fun CropConfig(focusStyle: CropImageView.Style, focusWidth: Int, focusHeight: Int, outPutX: Int, outPutY: Int, isSaveRectangle: Boolean) {
        pickHelper.focusStyle = focusStyle
        pickHelper.focusWidth = focusWidth
        pickHelper.focusHeight = focusHeight
        pickHelper.outPutX = outPutX
        pickHelper.outPutY = outPutY
        pickHelper.isSaveRectangle = isSaveRectangle
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