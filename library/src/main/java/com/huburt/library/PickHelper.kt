package com.huburt.library

import com.huburt.library.bean.ImageItem
import com.huburt.library.view.CropImageView
import java.io.Serializable
import java.util.*

/**
 * Created by hubert
 *
 * Created on 2017/10/13.
 */
data class PickHelper(
        var limit: Int = 9, //选择照片限制
        var isCrop: Boolean = false, //是否裁剪
        var isShowCamera: Boolean = true, //是否显示拍照按钮
        var isMultiMode: Boolean = true //选择模式
) : Serializable {

    var focusStyle = CropImageView.Style.RECTANGLE //裁剪框的形状
    var outPutX = 800           //裁剪保存宽度
    var outPutY = 800           //裁剪保存高度
    var focusWidth = 280         //焦点框的宽度
    var focusHeight = 280        //焦点框的高度
    var isSaveRectangle = false  //裁剪后的图片是否是矩形，否者跟随裁剪框的形状

    val selectedImages: ArrayList<ImageItem> = ArrayList()//已经选中的图片数据
    val historyImages: ArrayList<ImageItem> = ArrayList()//进入时已选中的的图片数据

    fun canSelect(): Boolean = selectedImages.size < limit

}