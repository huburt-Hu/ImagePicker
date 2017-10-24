package com.huburt.library.bean

import android.os.Parcel
import android.os.Parcelable

import java.io.Serializable

data class ImageItem(
        var path: String? = null//图片的路径
) : Serializable, Parcelable {

    var name: String? = null       //图片的名字
    var size: Long = 0     //图片的大小
    var width: Int = 0     //图片的宽度
    var height: Int = 0    //图片的高度
    var mimeType: String? = null   //图片的类型
    var addTime: Long = 0   //图片的创建时间

    constructor(parcel: Parcel) : this(parcel.readString()) {
        name = parcel.readString()
        size = parcel.readLong()
        width = parcel.readInt()
        height = parcel.readInt()
        mimeType = parcel.readString()
        addTime = parcel.readLong()
    }

    /**
     * 图片的路径和创建时间相同就认为是同一张图片
     */
    override fun equals(other: Any?): Boolean {
        if (other is ImageItem) {
            val item = other as ImageItem?
            return this.path.equals(item!!.path, ignoreCase = true) && this.addTime == item.addTime
        }
        return super.equals(other)
    }

    override fun hashCode(): Int {
        return path?.hashCode() ?: 0
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(path)
        parcel.writeString(name)
        parcel.writeLong(size)
        parcel.writeInt(width)
        parcel.writeInt(height)
        parcel.writeString(mimeType)
        parcel.writeLong(addTime)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ImageItem> {
        override fun createFromParcel(parcel: Parcel): ImageItem {
            return ImageItem(parcel)
        }

        override fun newArray(size: Int): Array<ImageItem?> {
            return arrayOfNulls(size)
        }
    }

}
