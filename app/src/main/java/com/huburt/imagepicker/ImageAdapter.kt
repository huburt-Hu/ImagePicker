package com.huburt.imagepicker

import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.huburt.picker.bean.ImageItem
import java.io.File

/**
 * Created by hubert
 *
 * Created on 2017/10/24.
 */
class ImageAdapter constructor(
        private var images: List<ImageItem>
) : RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {

    var listener: OnItemClickListener? = null

    fun updateData(images: List<ImageItem>) {
        this.images = images
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ImageViewHolder {
        return ImageViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.item_image, parent, false))
    }

    override fun onBindViewHolder(holder: ImageViewHolder?, position: Int) {
        holder?.imageView?.setOnClickListener({
            listener?.onItemClick(position)
        })
        Glide.with(holder?.imageView?.context)
                .load(Uri.fromFile(File(images[position].path)))
                .into(holder?.imageView)

    }

    override fun getItemCount(): Int {
        return images.size
    }

    inner class ImageViewHolder constructor(var rootView: View) : RecyclerView.ViewHolder(rootView) {
        val imageView: ImageView = rootView.findViewById(R.id.iv) as ImageView
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }
}