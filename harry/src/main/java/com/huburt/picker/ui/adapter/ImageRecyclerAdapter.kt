package com.huburt.picker.ui.adapter

import android.app.Activity
import android.support.v7.widget.AppCompatCheckBox
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.ViewHolder
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import com.huburt.picker.R
import com.huburt.picker.bean.ImageItem
import com.huburt.picker.core.PickOption
import com.huburt.picker.util.getScreenPix
import java.util.*


internal class ImageRecyclerAdapter(
        private val mActivity: Activity,
        private val mRecyclerView: RecyclerView,
        var images: ArrayList<ImageItem> = ArrayList()
) : RecyclerView.Adapter<ViewHolder>() {

    private val mInflater: LayoutInflater = LayoutInflater.from(mActivity)
    var listener: OnImageItemClickListener? = null   //图片被点击的监听

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
        if (holder is CameraViewHolder) {
            holder.bindCamera()
        } else if (holder is ImageViewHolder) {
            holder.bind(position)
        }
    }

    override fun getItemViewType(position: Int): Int =
            if (PickOption.capture)
                if (position == 0) ITEM_TYPE_CAMERA else ITEM_TYPE_NORMAL
            else ITEM_TYPE_NORMAL

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getItemCount(): Int = if (PickOption.capture) images.size + 1 else images.size

    fun getItem(position: Int): ImageItem? {
        return if (PickOption.capture)
            if (position == 0) null else images[position - 1]
        else images[position]
    }

    private inner class ImageViewHolder constructor(rootView: View) : ViewHolder(rootView) {

        var ivThumb: ImageView = rootView.findViewById(R.id.iv_thumb) as ImageView
        var mask: View = rootView.findViewById(R.id.mask)
        var checkView: View = rootView.findViewById(R.id.checkView)
        var cbCheck: AppCompatCheckBox = rootView.findViewById(R.id.cb_check) as AppCompatCheckBox
        var ivGif: ImageView = rootView.findViewById(R.id.iv_gif) as ImageView

        internal fun bind(position: Int) {
            val imageItem = getItem(position)
            ivThumb.setOnClickListener { listener?.onImageItemClick(imageItem!!, if (PickOption.capture) position - 1 else position) }
            checkView.setOnClickListener {
                if (cbCheck.isChecked) {
                    PickOption.selectedCollection.remove(imageItem)
                    mask.visibility = View.GONE
                    cbCheck.isChecked = false
                } else {
                    if (!PickOption.selectedCollection.canAdd(imageItem)) {
                        Toast.makeText(mActivity.applicationContext,
                                mActivity.getString(R.string.ip_select_limit, PickOption.limit), Toast.LENGTH_SHORT).show()
                    } else {
                        mask.visibility = View.VISIBLE
                        PickOption.selectedCollection.add(imageItem!!)
                        cbCheck.isChecked = true
                    }
                }
                listener?.onCheckChanged(PickOption.selectedCollection.size(), PickOption.limit)
            }
            //根据是否多选，显示或隐藏checkbox
            if (!PickOption.singleMode) {
                cbCheck.visibility = View.VISIBLE
                if (PickOption.selectedCollection.contains(imageItem)) {
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
                val resize = getResize()
                if (imageItem.isGif()) {
                    ivGif.visibility = View.VISIBLE
                    PickOption.imageLoader.loadGifThumbnail(mActivity, imageItem.path, ivThumb, resize, resize) //显示图片
                } else {
                    ivGif.visibility = View.GONE
                    PickOption.imageLoader.loadThumbnail(mActivity, imageItem.path, ivThumb, resize, resize) //显示图片
                }
            }
        }

        private fun getResize(): Int {
            val displayMetrics = mActivity.getScreenPix()
            val spanCount = (mRecyclerView.layoutManager as GridLayoutManager).spanCount
            val availableWidth = displayMetrics.widthPixels -
                    mActivity.resources.getDimensionPixelSize(R.dimen.media_grid_spacing) * (spanCount - 1)
            val resize = availableWidth / spanCount
            return resize
        }
    }

    private inner class CameraViewHolder constructor(var mItemView: View) : ViewHolder(mItemView) {

        internal fun bindCamera() {
            mItemView.setOnClickListener {
                listener?.onCameraClick()
            }
        }
    }

    companion object {

        private const val ITEM_TYPE_CAMERA = 0  //第一个条目是相机
        private const val ITEM_TYPE_NORMAL = 1  //第一个条目不是相机
    }
}
