package com.huburt.library.adapter

import android.app.Activity
import android.support.v4.view.PagerAdapter
import android.view.View
import android.view.ViewGroup
import com.huburt.library.ImagePicker
import com.huburt.library.bean.ImageItem
import com.huburt.library.util.Utils
import uk.co.senab.photoview.PhotoView
import uk.co.senab.photoview.PhotoViewAttacher
import java.util.*

class ImagePageAdapter(
        private val mActivity: Activity,
        private var images: ArrayList<ImageItem> = ArrayList()
) : PagerAdapter() {

    private val screenWidth: Int
    private val screenHeight: Int
    var listener: PhotoViewAttacher.OnPhotoTapListener? = null

    init {
        val dm = Utils.getScreenPix(mActivity)
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
        ImagePicker.imageLoader?.displayImagePreview(mActivity, imageItem.path!!, photoView, screenWidth, screenHeight)
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