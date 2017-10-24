package com.huburt.library.bean

import java.io.Serializable
import java.util.ArrayList


data class ImageFolder(var name: String?,                    //当前文件夹的名字
                       var path: String?,                   //当前文件夹的路径
                       var cover: ImageItem? = null,               //当前文件夹需要要显示的缩略图，默认为最近的一次图片
                       var images: ArrayList<ImageItem> = ArrayList() //当前文件夹下所有图片的集合
) : Serializable {


    /** 只要文件夹的路径和名字相同，就认为是相同的文件夹  */
    override fun equals(other: Any?): Boolean {
        try {
            val item = other as ImageFolder?
            return this.path!!.equals(item!!.path!!, ignoreCase = true) && this.name!!.equals(item.name!!, ignoreCase = true)
        } catch (e: ClassCastException) {
            e.printStackTrace()
        }
        return super.equals(other)
    }

    override fun hashCode(): Int {
        return path?.hashCode() ?: 0
    }
}
