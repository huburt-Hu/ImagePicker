package com.huburt.library.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.support.v7.widget.AppCompatCheckBox
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.TextView
import com.huburt.library.C
import com.huburt.library.ImagePicker
import com.huburt.library.ImagePicker.pickHelper
import com.huburt.library.PickHelper
import com.huburt.library.R
import com.huburt.library.adapter.ImagePageAdapter
import com.huburt.library.bean.ImageItem
import uk.co.senab.photoview.PhotoViewAttacher

/**
 * Created by hubert
 *
 * Created on 2017/10/19.
 */
class ImagePreviewActivity : ImagePreviewBaseActivity(), View.OnClickListener, PhotoViewAttacher.OnPhotoTapListener {

    private lateinit var imageItems: ArrayList<ImageItem>
    private var current: Int = 0

    companion object {

        fun startForResult(activity: Activity, requestCode: Int, position: Int, imageItems: ArrayList<ImageItem>) {
            val intent = Intent(activity, ImagePreviewActivity::class.java)
            intent.putExtra(C.EXTRA_IMAGE_ITEMS, imageItems)
            intent.putExtra(C.EXTRA_POSITION, position)
            activity.startActivityForResult(intent, requestCode)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        imageItems = intent.extras[C.EXTRA_IMAGE_ITEMS] as ArrayList<ImageItem>
        current = intent.extras[C.EXTRA_POSITION] as Int
        init()
    }

    private fun init() {
        btnOk.setOnClickListener(this)
        bottomBar.visibility = View.VISIBLE

        tvDes.text = getString(R.string.ip_preview_image_count, current + 1, imageItems.size)
        viewPager.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                current = position
                val imageItem = imageItems[position]
                val contains = pickHelper.selectedImages.contains(imageItem)
                cbCheck.isChecked = contains
                tvDes.text = getString(R.string.ip_preview_image_count, position + 1, imageItems.size)
            }
        })

        imagePageAdapter.setData(imageItems)
        viewPager.currentItem = current

        onCheckChanged(pickHelper.selectedImages.size, pickHelper.limit)

        cbCheck.isChecked = pickHelper.selectedImages.contains(imageItems[current])
        cbCheck.setOnClickListener {
            //checkBox 点击时会自动处理isCheck的状态转变，也就是说如果是选中状态，点击触发OnclickListener时，isCheck已经变成false了
            //cbCheck.isChecked = !cbCheck.isChecked
            val imageItem = imageItems[current]
            if (cbCheck.isChecked) {
                if (pickHelper.canSelect()) {
                    pickHelper.selectedImages.add(imageItem)
                } else {
                    showToast(getString(R.string.ip_select_limit, pickHelper.limit))
                    cbCheck.isChecked = false
                }
            } else {
                pickHelper.selectedImages.remove(imageItem)
            }
            onCheckChanged(pickHelper.selectedImages.size, pickHelper.limit)
        }
    }

    private fun onCheckChanged(selected: Int, limit: Int) {
        if (selected == 0) {
            btnOk.isEnabled = false
            btnOk.text = getString(R.string.ip_complete)
            btnOk.setTextColor(resources.getColor(R.color.ip_text_secondary_inverted))
        } else {
            btnOk.isEnabled = true
            btnOk.text = getString(R.string.ip_select_complete, selected, limit)
            btnOk.setTextColor(resources.getColor(R.color.ip_text_primary_inverted))
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_ok -> {
                setResult(Activity.RESULT_OK)
                finish()
            }
        }
    }

    override fun onOutsidePhotoTap() {
        changeTopAndBottomBar()
    }

    override fun onPhotoTap(view: View?, x: Float, y: Float) {
        changeTopAndBottomBar()
    }

    private fun changeTopAndBottomBar() {
        if (topBar.visibility == View.VISIBLE) {
            topBar.animation = AnimationUtils.loadAnimation(this, R.anim.top_out)
            bottomBar.animation = AnimationUtils.loadAnimation(this, R.anim.fade_out)
            topBar.visibility = View.GONE
            bottomBar.visibility = View.GONE
        } else {
            topBar.animation = AnimationUtils.loadAnimation(this, R.anim.top_in)
            bottomBar.animation = AnimationUtils.loadAnimation(this, R.anim.fade_in)
            topBar.visibility = View.VISIBLE
            bottomBar.visibility = View.VISIBLE
        }
    }
}
