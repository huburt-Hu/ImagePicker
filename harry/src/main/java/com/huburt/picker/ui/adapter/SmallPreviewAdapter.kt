package com.huburt.picker.ui.adapter

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.huburt.picker.R
import com.huburt.picker.bean.ImageItem
import com.huburt.picker.core.PickOption

/**
 * Created by hubert
 *
 * Created on 2017/11/2.
 */
class SmallPreviewAdapter(
        private val mActivity: Activity,
        var images: List<ImageItem> = PickOption.selectedCollection.getList()
) : RecyclerView.Adapter<SmallPreviewAdapter.SmallPreviewViewHolder>() {

    var current: ImageItem? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var listener: OnItemClickListener? = null

    override fun getItemCount(): Int = images.size

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): SmallPreviewViewHolder {
        return SmallPreviewViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.item_small_preview, parent, false))
    }

    override fun onBindViewHolder(holder: SmallPreviewViewHolder?, position: Int) {
        holder?.bind(position)
    }

    override fun getItemId(position: Int): Long = position.toLong()

    inner class SmallPreviewViewHolder(private var mView: View) : RecyclerView.ViewHolder(mView) {

        val iv_small = mView.findViewById(R.id.iv_small) as ImageView
        val v_frame = mView.findViewById(R.id.v_frame)

        fun bind(position: Int) {
            val imageItem = images[position]
            mView.setOnClickListener {
                listener?.onItemClick(position, imageItem)
            }
            if (TextUtils.equals(current?.path, imageItem.path)) {
                v_frame.visibility = View.VISIBLE
            } else {
                v_frame.visibility = View.GONE
            }
            val resize = mActivity.resources.getDimensionPixelSize(R.dimen.small_preview_width)
            if (imageItem.isGif()) {
                PickOption.imageLoader.loadGifThumbnail(mActivity, imageItem.path, iv_small, resize, resize)
            } else {
                PickOption.imageLoader.loadThumbnail(mActivity, imageItem.path, iv_small, resize, resize)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int, imageItem: ImageItem)
    }
}