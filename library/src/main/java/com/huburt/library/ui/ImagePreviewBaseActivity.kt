package com.huburt.library.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.support.v7.widget.AppCompatCheckBox
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.TextView
import com.huburt.library.C
import com.huburt.library.ImagePicker
import com.huburt.library.PickHelper
import com.huburt.library.R
import com.huburt.library.adapter.ImagePageAdapter
import com.huburt.library.bean.ImageItem
import kotlinx.android.synthetic.main.activity_image_preview.*
import kotlinx.android.synthetic.main.include_top_bar.*
import uk.co.senab.photoview.PhotoViewAttacher

/**
 * Created by hubert
 *
 * Created on 2017/10/24.
 */
abstract class ImagePreviewBaseActivity : BaseActivity(), PhotoViewAttacher.OnPhotoTapListener {


    protected lateinit var imagePageAdapter: ImagePageAdapter
    protected var pickHelper: PickHelper = ImagePicker.pickHelper


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_preview)
        initView()
    }

    private fun initView() {
        btn_back.setOnClickListener { finish() }

        imagePageAdapter = ImagePageAdapter(this)
        imagePageAdapter.listener = this
        viewpager.adapter = imagePageAdapter
    }

}