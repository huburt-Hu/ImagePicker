package com.huburt.library.ui

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.huburt.library.ImagePicker
import com.huburt.library.R
import com.huburt.library.view.CropImageView

/**
 * Created by hubert
 *
 * Created on 2017/10/20.
 */
class ImageCropActivity : BaseActivity(), View.OnClickListener {

    private lateinit var cropIv: CropImageView
    private val pickHelper = ImagePicker.pickHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_crop)
        findViewById(R.id.btn_back).setOnClickListener(this)
        val tvDes = findViewById(R.id.tv_des) as TextView
        tvDes.text = getString(R.string.ip_photo_crop)
        val btnOk = findViewById(R.id.btn_ok) as Button
        btnOk.text = getString(R.string.ip_complete)
        cropIv = findViewById(R.id.cv_crop_image) as CropImageView

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_ok -> finish()
        }
    }
}