package com.huburt.library.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.view.View
import android.view.animation.AnimationUtils
import com.huburt.library.C
import com.huburt.library.R
import kotlinx.android.synthetic.main.activity_image_preview.*
import kotlinx.android.synthetic.main.include_top_bar.*

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
        btn_back.setOnClickListener({
            setResult()
        })
        btn_ok.visibility = View.GONE
        btn_del.visibility = View.VISIBLE

        updateTitle()
        imagePageAdapter.setData(pickHelper.selectedImages)
        viewpager.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                current = position
                updateTitle()
            }
        })
        viewpager.currentItem = current

        btn_del.setOnClickListener({
            pickHelper.selectedImages.removeAt(current)
            if (current != 0) current -= 1
            imagePageAdapter.notifyDataSetChanged()
            viewpager.currentItem = current
            updateTitle()
            if (pickHelper.selectedImages.size == 0) {
                setResult()
            }
        })
    }

    private fun setResult() {
        val data = Intent()
        data.putExtra(C.EXTRA_IMAGE_ITEMS, pickHelper.selectedImages)
        setResult(Activity.RESULT_OK, data)
        finish()
    }

    private fun updateTitle() {
        tv_des.text = getString(R.string.ip_preview_image_count,
                if (pickHelper.selectedImages.size > 0) current + 1 else current,
                pickHelper.selectedImages.size)
    }

    override fun onPhotoTap(view: View?, x: Float, y: Float) {
        changeTopBar()
    }

    override fun onOutsidePhotoTap() {
        changeTopBar()
    }

    private fun changeTopBar() {
        if (top_bar.visibility == View.VISIBLE) {
            top_bar.animation = AnimationUtils.loadAnimation(this, R.anim.top_out)
            top_bar.visibility = View.GONE
        } else {
            top_bar.animation = AnimationUtils.loadAnimation(this, R.anim.top_in)
            top_bar.visibility = View.VISIBLE
        }
    }
}