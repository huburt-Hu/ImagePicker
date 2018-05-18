package com.huburt.picker.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import com.huburt.picker.R
import com.huburt.picker.core.PickOption
import com.huburt.picker.facade.Harry
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
            intent.putExtra(Harry.EXTRA_POSITION, position)
            activity.startActivityForResult(intent, requestCode)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        current = intent.extras[Harry.EXTRA_POSITION] as Int
        init()
    }

    private fun init() {
        btn_back.setOnClickListener({
            setResult()
        })
        btn_ok.visibility = View.GONE
        btn_del.visibility = View.VISIBLE

        updateTitle()
        imagePageAdapter.setData(PickOption.selectedCollection.getList())
        viewpager.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                current = position
                updateTitle()
            }
        })
        viewpager.currentItem = current

        btn_del.setOnClickListener({
            PickOption.selectedCollection.removeAt(current)
            if (current != 0) current -= 1
            imagePageAdapter.notifyDataSetChanged()
            viewpager.currentItem = current
            updateTitle()
            if (PickOption.selectedCollection.size() == 0) {
                setResult()
            }
        })
    }

    private fun setResult() {
        val data = Intent()
        data.putExtra(Harry.EXTRA_IMAGE_ITEMS, PickOption.selectedCollection.getList())
        setResult(Activity.RESULT_OK, data)
        finish()
    }

    private fun updateTitle() {
        tv_des.text = getString(R.string.ip_preview_image_count,
                if (PickOption.selectedCollection.size() > 0) current + 1 else current,
                PickOption.selectedCollection.size())
    }

    override fun onPhotoTap(view: ImageView?, x: Float, y: Float) {
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