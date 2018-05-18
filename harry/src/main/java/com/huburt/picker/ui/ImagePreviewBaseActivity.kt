package com.huburt.picker.ui

import android.os.Bundle
import com.github.chrisbanes.photoview.OnPhotoTapListener
import com.huburt.picker.R
import com.huburt.picker.ui.adapter.ImagePageAdapter
import kotlinx.android.synthetic.main.activity_image_preview.*
import kotlinx.android.synthetic.main.include_top_bar.*

/**
 * Created by hubert
 *
 * Created on 2017/10/24.
 */
abstract class ImagePreviewBaseActivity : BaseActivity(), OnPhotoTapListener {


    protected lateinit var imagePageAdapter: ImagePageAdapter

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