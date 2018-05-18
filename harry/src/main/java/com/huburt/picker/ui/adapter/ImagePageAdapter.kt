package com.huburt.picker.ui.adapter

import android.app.Activity
import android.support.v4.view.PagerAdapter
import android.view.View
import android.view.ViewGroup
import com.github.chrisbanes.photoview.OnPhotoTapListener
import com.github.chrisbanes.photoview.PhotoView
import com.huburt.picker.bean.ImageItem
import com.huburt.picker.core.PickOption
import com.huburt.picker.util.getScreenPix
import java.util.*

class ImagePageAdapter(
        private val mActivity: Activity,
        private var images: ArrayList<ImageItem> = ArrayList()
) : PagerAdapter() {

    private val screenWidth: Int
    private val screenHeight: Int
    var listener: OnPhotoTapListener? = null

    init {
        val dm = mActivity.getScreenPix()
        screenWidth = dm.widthPixels
        screenHeight = dm.heightPixels
    }

    fun setData(images: ArrayList<ImageItem>) {
        this.images = images
        notifyDataSetChanged()
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val photoView = PhotoView(mActivity)
        val imageItem = images[position]
        if (imageItem.isGif()) {
            PickOption.imageLoader.loadGif(mActivity, imageItem.path, photoView, screenWidth, screenHeight)
        } else {
            PickOption.imageLoader.loadImage(mActivity, imageItem.path, photoView, screenWidth, screenHeight)
        }
        photoView.setOnPhotoTapListener(listener)
        container.addView(photoView)
        return photoView
    }

    override fun getCount(): Int = images.size

    override fun isViewFromObject(view: View, `object`: Any): Boolean = view === `object`

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun getItemPosition(`object`: Any?): Int = PagerAdapter.POSITION_NONE

}