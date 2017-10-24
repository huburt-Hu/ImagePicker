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
import uk.co.senab.photoview.PhotoViewAttacher

/**
 * Created by hubert
 *
 * Created on 2017/10/24.
 */
abstract class ImagePreviewBaseActivity : BaseActivity(), PhotoViewAttacher.OnPhotoTapListener {
    protected lateinit var viewPager: ViewPager
    protected lateinit var btnOk: Button
    protected lateinit var cbOrigin: AppCompatCheckBox
    protected lateinit var cbCheck: AppCompatCheckBox
    protected lateinit var bottomBar: View
    protected lateinit var tvDes: TextView
    protected lateinit var topBar: View
    protected lateinit var btnBack: View
    protected lateinit var btnDel: View

    protected lateinit var imagePageAdapter: ImagePageAdapter
    protected var pickHelper: PickHelper = ImagePicker.pickHelper


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_preview)
        initView()
    }

    private fun initView() {
        btnBack = findViewById(R.id.btn_back)
        topBar = findViewById(R.id.top_bar)
        btnDel = findViewById(R.id.btn_del)
        viewPager = findViewById(R.id.viewpager) as ViewPager
        btnOk = findViewById(R.id.btn_ok) as Button
        tvDes = findViewById(R.id.tv_des) as TextView
        bottomBar = findViewById(R.id.bottom_bar)
        cbOrigin = findViewById(R.id.cb_origin) as AppCompatCheckBox
        cbCheck = findViewById(R.id.cb_check) as AppCompatCheckBox

        btnBack.setOnClickListener { finish() }

        imagePageAdapter = ImagePageAdapter(this)
        imagePageAdapter.listener = this
        viewPager.adapter = imagePageAdapter
    }

}