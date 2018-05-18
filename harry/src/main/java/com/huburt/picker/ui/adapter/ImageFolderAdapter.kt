package com.huburt.picker.ui.adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.huburt.picker.R
import com.huburt.picker.bean.ImageFolder
import com.huburt.picker.core.PickOption

class ImageFolderAdapter(
        private val mActivity: Activity,
        private var imageFolders: MutableList<ImageFolder> = ArrayList()
) : BaseAdapter() {
    private val mInflater: LayoutInflater =
            mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    var selectIndex = 0
        set(i) {
            if (selectIndex != i) {
                field = i
                notifyDataSetChanged()
            }
        }

    fun refreshData(folders: MutableList<ImageFolder>?) {
        if (folders != null && folders.size > 0) imageFolders = folders
        else imageFolders.clear()
        notifyDataSetChanged()
    }

    override fun getCount(): Int = imageFolders.size

    override fun getItem(position: Int): ImageFolder = imageFolders[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        val holder: ViewHolder
        if (view == null) {
            view = mInflater.inflate(R.layout.adapter_folder_list_item, parent, false)
            holder = ViewHolder(view)
        } else {
            holder = view.tag as ViewHolder
        }

        val (name, _, cover, images) = getItem(position)
        holder.folderName.text = name
        holder.imageCount.text = mActivity.getString(R.string.ip_folder_image_count, images.size)
        val imageSize = mActivity.resources.getDimensionPixelSize(R.dimen.folder_image_width)
        if (cover?.path != null) {
            if (cover.isGif()) {
                PickOption.imageLoader.loadGifThumbnail(mActivity, cover.path, holder.cover, imageSize, imageSize)
            } else {
                PickOption.imageLoader.loadThumbnail(mActivity, cover.path, holder.cover, imageSize, imageSize)
            }
        }

        if (selectIndex == position) {
            holder.folderCheck.visibility = View.VISIBLE
        } else {
            holder.folderCheck.visibility = View.INVISIBLE
        }

        return checkNotNull(view)
    }

    private inner class ViewHolder(view: View) {
        internal var cover: ImageView = view.findViewById(R.id.iv_cover) as ImageView
        internal var folderName: TextView = view.findViewById(R.id.tv_folder_name) as TextView
        internal var imageCount: TextView = view.findViewById(R.id.tv_image_count) as TextView
        internal var folderCheck: ImageView = view.findViewById(R.id.iv_folder_check) as ImageView

        init {
            view.tag = this
        }
    }
}
