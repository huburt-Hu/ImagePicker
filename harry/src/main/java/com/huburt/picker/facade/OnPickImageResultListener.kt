package com.huburt.picker.facade

import com.huburt.picker.bean.ImageItem

interface OnPickImageResultListener {
    fun onImageResult(imageItems: ArrayList<ImageItem>)
}