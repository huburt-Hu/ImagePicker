package com.huburt.library.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.view.View
import android.view.animation.AnimationUtils
import com.huburt.library.C
import com.huburt.library.R

/**
 * Created by hubert
 *
 * Created on 2017/10/24.
 */
class ImagePreviewDelActivity : ImagePreviewBaseActivity() {

    private var current: Int = 0

    companion object {
        fun startForResult(activity: Activity, requestCode: Int, position: Int) {
            val intent = Intent(activity, ImagePreviewDelActivity::class.java)
            intent.putExtra(C.EXTRA_POSITION, position)
            activity.startActivityForResult(intent, requestCode)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        current = intent.extras[C.EXTRA_POSITION] as Int
        init()
    }

    private fun init() {
        btnBack.setOnClickListener({
            val data = Intent()
            data.putExtra(C.EXTRA_IMAGE_ITEMS, pickHelper.selectedImages)
            setResult(Activity.RESULT_OK, data)
            finish()
        })
        btnOk.visibility = View.GONE
        btnDel.visibility = View.VISIBLE

        updateTitle()
        imagePageAdapter.setData(pickHelper.selectedImages)
        viewPager.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                current = position
                updateTitle()
            }
        })
        btnDel.setOnClickListener({
            pickHelper.selectedImages.removeAt(current)
            if (current != 0) current -= 1
            imagePageAdapter.notifyDataSetChanged()
            viewPager.currentItem = current
            updateTitle()
        })
    }

    private fun updateTitle() {
        tvDes.text = getString(R.string.ip_preview_image_count, current + 1, pickHelper.selectedImages.size)
    }

    override fun onPhotoTap(view: View?, x: Float, y: Float) {
        changeTopBar()
    }

    override fun onOutsidePhotoTap() {
        changeTopBar()
    }

    private fun changeTopBar() {
        if (topBar.visibility == View.VISIBLE) {
            topBar.animation = AnimationUtils.loadAnimation(this, R.anim.top_out)
            topBar.visibility = View.GONE
        } else {
            topBar.animation = AnimationUtils.loadAnimation(this, R.anim.top_in)
            topBar.visibility = View.VISIBLE
        }
    }
}