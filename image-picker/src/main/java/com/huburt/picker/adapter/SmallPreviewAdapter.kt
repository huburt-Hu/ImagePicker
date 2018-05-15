package com.huburt.picker.adapter

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.huburt.picker.ImagePicker
import com.huburt.picker.R
import com.huburt.picker.bean.ImageItem
import java.util.*

/**
 * Created by hubert
 *
 * Created on 2017/11/2.
 */
class SmallPreviewAdapter(
        private val mActivity: Activity,
        var images: List<ImageItem> = ArrayList()
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
            mView.setOnClickListener {
                listener?.onItemClick(position, images[position])
            }
            if (TextUtils.equals(current?.path, images[position].path)) {
                v_frame.visibility = View.VISIBLE
            } else {
                v_frame.visibility = View.GONE
            }
            ImagePicker.imageLoader.displayImage(mActivity, images[position].path!!, iv_small, iv_small.width, iv_small.height)

        }
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int, imageItem: ImageItem)
    }
}