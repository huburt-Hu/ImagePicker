package com.huburt.library

import com.huburt.library.bean.ImageItem
import java.io.Serializable
import java.util.*

/**
 * Created by hubert
 *
 * Created on 2017/10/13.
 */
class PickHelper : Serializable {
    var limit: Int = 9 //选择照片限制
    var isCrop: Boolean = false //是否裁剪
    var isShowCamera: Boolean = true //是否显示拍照按钮
    var isMultiMode: Boolean = true //选择模式

    var outPutX = 800           //裁剪保存宽度
    var outPutY = 800           //裁剪保存高度
    var focusWidth = 280         //焦点框的宽度
    var focusHeight = 280        //焦点框的高度
    var isSaveRectangle = false  //裁剪后的图片是否是矩形，否者跟随裁剪框的形状

    val selectedImages: ArrayList<ImageItem> = ArrayList()//已经选中的图片数据

    fun canSelect(): Boolean = selectedImages.size < limit

    override fun toString(): String {
        return "PickHelper(" +
                "limit=$limit, " +
                "isShowCamera=$isShowCamera, " +
                "isMultiMode=$isMultiMode, " +
                "selectedImages=$selectedImages)"
    }

}