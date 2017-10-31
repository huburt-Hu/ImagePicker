package com.huburt.library.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import com.huburt.library.C
import com.huburt.library.ImagePicker
import com.huburt.library.R
import com.huburt.library.bean.ImageItem
import com.huburt.library.util.BitmapUtil
import com.huburt.library.util.FileUtil
import com.huburt.library.util.Utils
import com.huburt.library.view.CropImageView
import kotlinx.android.synthetic.main.activity_image_crop.*
import kotlinx.android.synthetic.main.include_top_bar.*
import java.io.File

/**
 * Created by hubert
 *
 * Created on 2017/10/20.
 */
class ImageCropActivity : BaseActivity(), View.OnClickListener, CropImageView.OnBitmapSaveCompleteListener {

    companion object {
        fun start(activity: Activity, requestCode: Int) {
            activity.startActivityForResult(Intent(activity, ImageCropActivity::class.java), requestCode)
        }
    }

    private val pickHelper = ImagePicker.pickHelper
    private var mBitmap: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_crop)
        initView()
    }

    private fun initView() {
        btn_back.setOnClickListener(this)
        tv_des.text = getString(R.string.ip_photo_crop)
        btn_ok.text = getString(R.string.ip_complete)
        btn_ok.setOnClickListener(this)
        cv_crop_image.setOnBitmapSaveCompleteListener(this)
        cv_crop_image.focusStyle = pickHelper.focusStyle
        cv_crop_image.focusWidth = Utils.dp2px(this, pickHelper.focusWidth.toFloat())
        cv_crop_image.focusHeight = Utils.dp2px(this, pickHelper.focusHeight.toFloat())

        var imagePath = pickHelper.selectedImages[0].path
        //缩放图片
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(imagePath, options)
        val displayMetrics = resources.displayMetrics
        options.inSampleSize = calculateInSampleSize(options, displayMetrics.widthPixels, displayMetrics.heightPixels)
        options.inJustDecodeBounds = false
        mBitmap = BitmapFactory.decodeFile(imagePath, options)

        cv_crop_image.setImageBitmap(cv_crop_image.rotate(mBitmap, BitmapUtil.getBitmapDegree(imagePath)))
    }

    private fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
        val width = options.outWidth
        val height = options.outHeight
        var inSampleSize = 1
        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                inSampleSize = width / reqWidth
            } else {
                inSampleSize = height / reqHeight
            }
        }
        return inSampleSize
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_back ->
                finish()
            R.id.btn_ok ->
                cv_crop_image.saveBitmapToFile(FileUtil.getCropCacheFolder(this), pickHelper.outPutX, pickHelper.outPutY, pickHelper.isSaveRectangle)
        }
    }

    override fun onBitmapSaveSuccess(file: File?) {
        pickHelper.selectedImages.removeAt(0)
        val imageItem = ImageItem(file?.absolutePath)
        pickHelper.selectedImages.add(imageItem)

        val intent = Intent()
        intent.putExtra(C.EXTRA_IMAGE_ITEMS, pickHelper.selectedImages)
        setResult(Activity.RESULT_OK, intent)   //单选不需要裁剪，返回数据
        finish()
    }

    override fun onBitmapSaveError(file: File?) {

    }

    override fun onDestroy() {
        super.onDestroy()
        cv_crop_image.setOnBitmapSaveCompleteListener(null)
        if (null != mBitmap && !mBitmap!!.isRecycled) {
            mBitmap!!.recycle()
            mBitmap = null
        }
    }
}