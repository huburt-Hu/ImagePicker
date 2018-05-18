package com.huburt.picker.core

import com.huburt.picker.bean.ImageItem

/**
 * Created by hubert on 2018/5/17.
 *
 */
class SelectedItemCollection {

    val selectedImages: ArrayList<ImageItem> = ArrayList<ImageItem>()

    fun clear() {
        selectedImages.clear()
    }

    fun contains(imageItem: ImageItem?): Boolean {
        selectedImages.forEach {
            if (it.path == imageItem?.path) {
                return true
            }
        }
        return false
    }

    fun remove(imageItem: ImageItem?) {
        selectedImages.remove(imageItem)
    }

    fun size(): Int {
        return selectedImages.size
    }

    fun canAdd(imageItem: ImageItem?): Boolean {
        return selectedImages.size < PickOption.limit
    }

    fun add(imageItem: ImageItem) {
        selectedImages.add(imageItem)
    }

    fun getList(): ArrayList<ImageItem> {
        return selectedImages
    }

    fun indexOf(imageItem: ImageItem): Int {
        return selectedImages.indexOf(imageItem)
    }

    operator fun get(index: Int): ImageItem? {
        return selectedImages[index]
    }

    fun removeAt(index: Int) {
        selectedImages.removeAt(index)
    }
}