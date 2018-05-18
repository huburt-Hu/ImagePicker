package com.huburt.picker.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import com.github.chrisbanes.photoview.OnPhotoTapListener
import com.huburt.picker.R
import com.huburt.picker.ui.adapter.SmallPreviewAdapter
import com.huburt.picker.bean.ImageItem
import com.huburt.picker.core.PickOption
import com.huburt.picker.facade.Harry
import kotlinx.android.synthetic.main.activity_image_preview.*
import kotlinx.android.synthetic.main.include_top_bar.*

/**
 * Created by hubert
 *
 * Created on 2017/10/19.
 */
class ImagePreviewActivity : ImagePreviewBaseActivity(), View.OnClickListener, OnPhotoTapListener {

    private lateinit var imageItems: ArrayList<ImageItem>
    private var current: Int = 0
    private var previewAdapter: SmallPreviewAdapter = SmallPreviewAdapter(this)

    companion object {

        fun startForResult(activity: Activity, requestCode: Int, position: Int, imageItems: ArrayList<ImageItem>) {
            val intent = Intent(activity, ImagePreviewActivity::class.java)
            intent.putExtra(Harry.EXTRA_IMAGE_ITEMS, imageItems)
            intent.putExtra(Harry.EXTRA_POSITION, position)
            activity.startActivityForResult(intent, requestCode)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        imageItems = intent.getParcelableArrayListExtra(Harry.EXTRA_IMAGE_ITEMS)
        current = intent.getIntExtra(Harry.EXTRA_POSITION, 0)
        init()
    }

    private fun init() {
        btn_ok.setOnClickListener(this)
        bottom_bar.visibility = View.VISIBLE

        tv_des.text = getString(R.string.ip_preview_image_count, current + 1, imageItems.size)
        viewpager.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                current = position
                val imageItem = imageItems[position]
                val contains = PickOption.selectedCollection.contains(imageItem)
                cb_check.isChecked = contains
                tv_des.text = getString(R.string.ip_preview_image_count, position + 1, imageItems.size)
                updatePreview()
            }
        })

        imagePageAdapter.setData(imageItems)
        viewpager.currentItem = current

        onCheckChanged(PickOption.selectedCollection.size(), PickOption.limit)

        cb_check.isChecked = PickOption.selectedCollection.contains(imageItems[current])
        cb_check.setOnClickListener {
            //checkBox 点击时会自动处理isCheck的状态转变，也就是说如果是选中状态，点击触发OnclickListener时，isCheck已经变成false了
            //cbCheck.isChecked = !cbCheck.isChecked
            val imageItem = imageItems[current]
            if (cb_check.isChecked) {
                if (PickOption.selectedCollection.canAdd(imageItem)) {
                    PickOption.selectedCollection.add(imageItem)
                } else {
                    showToast(getString(R.string.ip_select_limit, PickOption.limit))
                    cb_check.isChecked = false
                }
                previewAdapter.notifyItemInserted(PickOption.selectedCollection.size() - 1)
            } else {
                val index = PickOption.selectedCollection.indexOf(imageItem)
                PickOption.selectedCollection.remove(imageItem)
                previewAdapter.notifyItemRemoved(index)
            }
            onCheckChanged(PickOption.selectedCollection.size(), PickOption.limit)
            updatePreview()
        }

        rv_small.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        previewAdapter.listener = object : SmallPreviewAdapter.OnItemClickListener {
            override fun onItemClick(position: Int, imageItem: ImageItem) {
                viewpager.setCurrentItem(imageItems.indexOf(imageItem), false)
            }
        }
        rv_small.adapter = previewAdapter
        updatePreview()
    }

    private fun updatePreview() {
        if (PickOption.selectedCollection.size() > 0) {
            rv_small.visibility = View.VISIBLE
            val index = PickOption.selectedCollection.indexOf(imageItems[current])
            previewAdapter.current = if (index >= 0) PickOption.selectedCollection[index] else null
            if (index >= 0) {
                rv_small.smoothScrollToPosition(index)
            }
        } else {
            rv_small.visibility = View.GONE
        }
    }

    private fun onCheckChanged(selected: Int, limit: Int) {
        if (selected == 0) {
            btn_ok.isEnabled = false
            btn_ok.text = getString(R.string.ip_complete)
            btn_ok.setTextColor(resources.getColor(R.color.ip_text_secondary_inverted))
        } else {
            btn_ok.isEnabled = true
            btn_ok.text = getString(R.string.ip_select_complete, selected, limit)
            btn_ok.setTextColor(resources.getColor(R.color.ip_text_primary_inverted))
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_ok -> {
                setResult(Harry.RESULT_OK)
                finish()
            }
        }
    }

    override fun onPhotoTap(view: ImageView?, x: Float, y: Float) {
        changeTopAndBottomBar()
    }

    private fun changeTopAndBottomBar() {
        if (top_bar.visibility == View.VISIBLE) {
            top_bar.animation = AnimationUtils.loadAnimation(this, R.anim.top_out)
            bottom_bar.animation = AnimationUtils.loadAnimation(this, R.anim.fade_out)
            top_bar.visibility = View.GONE
            bottom_bar.visibility = View.GONE
        } else {
            top_bar.animation = AnimationUtils.loadAnimation(this, R.anim.top_in)
            bottom_bar.animation = AnimationUtils.loadAnimation(this, R.anim.fade_in)
            top_bar.visibility = View.VISIBLE
            bottom_bar.visibility = View.VISIBLE
        }
    }
}
