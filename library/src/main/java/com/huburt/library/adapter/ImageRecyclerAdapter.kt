package com.huburt.library.adapter

import android.app.Activity
import android.support.v7.widget.AppCompatCheckBox
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.ViewHolder
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.ImageView
import android.widget.Toast
import com.huburt.library.ImagePicker
import com.huburt.library.PickHelper
import com.huburt.library.R
import com.huburt.library.bean.ImageItem
import com.huburt.library.util.Utils
import java.util.*


class ImageRecyclerAdapter(
        private val mActivity: Activity,
        private val pickHelper: PickHelper,
        var images: ArrayList<ImageItem> = ArrayList()
) : RecyclerView.Adapter<ViewHolder>() {

    private val mImageSize: Int = Utils.getImageItemWidth(mActivity)  //每个条目的大小
    private val mInflater: LayoutInflater = LayoutInflater.from(mActivity)
    internal var listener: OnImageItemClickListener? = null   //图片被点击的监听

    interface OnImageItemClickListener {
        fun onImageItemClick(imageItem: ImageItem, position: Int)
        fun onCheckChanged(selected: Int, limit: Int)
        fun onCameraClick()
    }

    fun refreshData(images: ArrayList<ImageItem>?) {
        if (images != null) this.images = images
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return if (viewType == ITEM_TYPE_CAMERA) {
            CameraViewHolder(mInflater.inflate(R.layout.adapter_camera_item, parent, false))
        } else ImageViewHolder(mInflater.inflate(R.layout.adapter_image_list_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        (holder as? CameraViewHolder)?.bindCamera() ?: (holder as? ImageViewHolder)?.bind(position)
    }

    override fun getItemViewType(position: Int): Int =
            if (pickHelper.isShowCamera) if (position == 0) ITEM_TYPE_CAMERA else ITEM_TYPE_NORMAL
            else ITEM_TYPE_NORMAL

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getItemCount(): Int = if (pickHelper.isShowCamera) images.size + 1 else images.size

    fun getItem(position: Int): ImageItem? =
            if (pickHelper.isShowCamera)
                if (position == 0) null else images[position - 1]
            else images[position]


    private inner class ImageViewHolder internal constructor(internal var rootView: View) : ViewHolder(rootView) {

        internal var ivThumb: ImageView = rootView.findViewById(R.id.iv_thumb) as ImageView
        internal var mask: View = rootView.findViewById(R.id.mask)
        internal var checkView: View = rootView.findViewById(R.id.checkView)
        internal var cbCheck: AppCompatCheckBox = rootView.findViewById(R.id.cb_check) as AppCompatCheckBox

        init {
            rootView.layoutParams = AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mImageSize) //让图片是个正方形
        }

        internal fun bind(position: Int) {
            val imageItem = getItem(position)
            ivThumb.setOnClickListener { listener?.onImageItemClick(imageItem!!, if (pickHelper.isShowCamera) position - 1 else position) }
            checkView.setOnClickListener {
                if (cbCheck.isChecked) {
                    pickHelper.selectedImages.remove(imageItem)
                    mask.visibility = View.GONE
                    cbCheck.isChecked = false
                } else {
                    if (pickHelper.selectedImages.size >= pickHelper.limit) {
                        Toast.makeText(mActivity.applicationContext, mActivity.getString(R.string.ip_select_limit, pickHelper.limit), Toast.LENGTH_SHORT).show()
                    } else {
                        mask.visibility = View.VISIBLE
                        pickHelper.selectedImages.add(imageItem!!)
                        cbCheck.isChecked = true
                    }
                }
                listener?.onCheckChanged(pickHelper.selectedImages.size, pickHelper.limit)
            }
            //根据是否多选，显示或隐藏checkbox
            if (pickHelper.isMultiMode) {
                cbCheck.visibility = View.VISIBLE
                if (contains(pickHelper.selectedImages, imageItem)) {
                    mask.visibility = View.VISIBLE
                    cbCheck.isChecked = true
                } else {
                    mask.visibility = View.GONE
                    cbCheck.isChecked = false
                }
            } else {
                cbCheck.visibility = View.GONE
            }
            if (imageItem?.path != null) {
                ImagePicker.imageLoader.displayImage(mActivity, imageItem.path!!, ivThumb, mImageSize, mImageSize) //显示图片
            }
        }

        private fun contains(selectedImages: ArrayList<ImageItem>, imageItem: ImageItem?): Boolean {
//            for (item in selectedImages) {
//                if (TextUtils.equals(item.path, imageItem.path)) {
//                    return true
//                }
//            }
//            return false
            //等同于上方
            return selectedImages.any { TextUtils.equals(it.path, imageItem?.path) }
        }
    }

    private inner class CameraViewHolder internal constructor(internal var mItemView: View) : ViewHolder(mItemView) {

        internal fun bindCamera() {
            mItemView.layoutParams = AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mImageSize) //让图片是个正方形
            mItemView.tag = null
            mItemView.setOnClickListener {
                listener?.onCameraClick()
            }
        }
    }

    companion object {

        private val ITEM_TYPE_CAMERA = 0  //第一个条目是相机
        private val ITEM_TYPE_NORMAL = 1  //第一个条目不是相机
    }
}
