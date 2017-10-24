package com.huburt.library.ui

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Button
import android.widget.TextView
import com.huburt.library.*
import com.huburt.library.adapter.ImageFolderAdapter
import com.huburt.library.adapter.ImageRecyclerAdapter
import com.huburt.library.bean.ImageFolder
import com.huburt.library.bean.ImageItem
import com.huburt.library.util.CameraUtil
import com.huburt.library.util.Utils
import com.huburt.library.view.FolderPopUpWindow
import com.huburt.library.view.GridSpacingItemDecoration
import java.io.File
import java.util.ArrayList

/**
 * Created by hubert
 *
 * Created on 2017/10/12.
 */
class ImageGridActivity : BaseActivity(), View.OnClickListener, ImageDataSource.OnImagesLoadedListener, ImageRecyclerAdapter.OnImageItemClickListener {
    companion object {

        val REQUEST_PERMISSION_STORAGE = 0x12
        val REQUEST_PERMISSION_CAMERA = 0x13
        val REQUEST_CAMERA = 0x23
        val REQUEST_PREVIEW = 0x9
        val INTENT_MAX = 1000

        fun startForResult(activity: Activity, requestCode: Int) {
            activity.startActivityForResult(Intent(activity, ImageGridActivity::class.java), requestCode)
        }
    }

    private val pickerHelper: PickHelper = ImagePicker.pickHelper
    private val imageDataSource = ImageDataSource(this)
    private lateinit var btnPreview: TextView
    private lateinit var tvDir: TextView
    private lateinit var btnOk: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ImageRecyclerAdapter
    private lateinit var footBar: ViewGroup
    private lateinit var mFolderPopupWindow: FolderPopUpWindow
    private lateinit var mImageFolderAdapter: ImageFolderAdapter
    private lateinit var imageFolders: List<ImageFolder>
    private lateinit var takeImageFile: File

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_grid)
        initView()
        initPopWindow()

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_PERMISSION_STORAGE)
        } else {
            imageDataSource.loadImage(this)
        }
    }

    override fun onResume() {
        super.onResume()
        //数据刷新
        adapter.notifyDataSetChanged()
        onCheckChanged(pickerHelper.selectedImages.size, pickerHelper.limit)
    }

    private fun initView() {
        recyclerView = findViewById(R.id.recycler) as RecyclerView

        footBar = findViewById(R.id.footer_bar) as ViewGroup
        findViewById(R.id.ll_dir).setOnClickListener(this)
        tvDir = findViewById(R.id.tv_dir) as TextView
        btnOk = findViewById(R.id.btn_ok) as Button
        btnOk.setOnClickListener(this)
        findViewById(R.id.btn_back).setOnClickListener(this)
        btnPreview = findViewById(R.id.btn_preview) as TextView
        btnPreview.setOnClickListener(this)

        recyclerView.layoutManager = GridLayoutManager(this, 3)
        recyclerView.addItemDecoration(GridSpacingItemDecoration(3, Utils.dp2px(this, 2f), false))
        adapter = ImageRecyclerAdapter(this, pickerHelper)
        adapter.listener = this
    }

    private fun initPopWindow() {
        mImageFolderAdapter = ImageFolderAdapter(this, null)
        mFolderPopupWindow = FolderPopUpWindow(this, mImageFolderAdapter)
        mFolderPopupWindow.setOnItemClickListener(object : FolderPopUpWindow.OnItemClickListener {
            override fun onItemClick(adapterView: AdapterView<*>, view: View, position: Int, l: Long) {
                mImageFolderAdapter.selectIndex = position
                mFolderPopupWindow.dismiss()
                val imageFolder = adapterView.adapter?.getItem(position) as ImageFolder
                adapter.refreshData(imageFolder.images)
                tvDir.text = imageFolder.name
            }
        })
        footBar.post({ mFolderPopupWindow.setMargin(footBar.height) })
    }

    private fun showPopupFolderList() {
        mImageFolderAdapter.refreshData(imageFolders.toMutableList())  //刷新数据
        if (mFolderPopupWindow.isShowing) {
            mFolderPopupWindow.dismiss()
        } else {
            mFolderPopupWindow.showAtLocation(footBar, Gravity.NO_GRAVITY, 0, 0)
            //默认选择当前选择的上一个，当目录很多时，直接定位到已选中的条目
            var index = mImageFolderAdapter.selectIndex
            index = if (index == 0) index else index - 1
            mFolderPopupWindow.setSelection(index)
        }
    }

    override fun onImageItemClick(imageItem: ImageItem, position: Int) {
        var images = adapter.images
        var p = position
        if (images.size > INTENT_MAX) {//数据量过大
            val s: Int
            val e: Int
            if (position < images.size / 2) {//点击position在list靠前
                s = Math.max(position - INTENT_MAX / 2, 0)
                e = Math.min(s + INTENT_MAX, images.size)
            } else {
                e = Math.min(position + INTENT_MAX / 2, images.size)
                s = Math.max(e - INTENT_MAX, 0)
            }
            p = position - s
            Log.e("hubert", "start:$s , end:$e , position:$p")
//            images = ArrayList()
//            for (i in s until e) {
//                images.add(adapter.images[i])
//            }
            //等同于上面，IDE提示换成的Kotlin的高阶函数
            images = (s until e).mapTo(ArrayList()) { adapter.images[it] }
        }
        ImagePreviewActivity.startForResult(this, REQUEST_PREVIEW, p, images)
    }

    override fun onCheckChanged(selected: Int, limit: Int) {
        if (selected == 0) {
            btnOk.isEnabled = false
            btnOk.text = getString(R.string.ip_complete)
            btnOk.setTextColor(resources.getColor(R.color.ip_text_secondary_inverted))
            btnPreview.isEnabled = false
            btnPreview.text = getString(R.string.ip_preview)
            btnPreview.setTextColor(resources.getColor(R.color.ip_text_secondary_inverted))
        } else {
            btnOk.isEnabled = true
            btnOk.text = getString(R.string.ip_select_complete, selected, limit)
            btnOk.setTextColor(resources.getColor(R.color.ip_text_primary_inverted))
            btnPreview.isEnabled = true
            btnPreview.text = getString(R.string.ip_preview_count, selected, limit)
            btnPreview.setTextColor(resources.getColor(R.color.ip_text_primary_inverted))
        }
    }

    override fun onCameraClick() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA),
                    ImageGridActivity.REQUEST_PERMISSION_CAMERA)
        } else {
            takeImageFile = CameraUtil.takePicture(this, REQUEST_CAMERA)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSION_STORAGE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                imageDataSource.loadImage(this)
            } else {
                showToast("权限被禁止，无法选择本地图片")
            }
        } else if (requestCode == REQUEST_PERMISSION_CAMERA) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                takeImageFile = CameraUtil.takePicture(this, REQUEST_CAMERA)
            } else {
                showToast("权限被禁止，无法打开相机")
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CAMERA && resultCode == Activity.RESULT_OK) {//相机返回
            Log.e("hubert", takeImageFile.absolutePath)
            //广播通知新增图片
            val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
            mediaScanIntent.data = Uri.fromFile(takeImageFile)
            sendBroadcast(mediaScanIntent)

            val imageItem = ImageItem(takeImageFile.absolutePath)
            pickerHelper.selectedImages.clear()
            pickerHelper.selectedImages.add(imageItem)

            if (pickerHelper.isCrop) {//需要裁剪

            } else {
                setResult()
            }
        } else if (requestCode == REQUEST_PREVIEW) {//预览界面返回
            if (resultCode == Activity.RESULT_OK) {
                setResult()
            }
        }
    }

    private fun setResult() {
        val result = Intent()
        result.putExtra(C.EXTRA_IMAGE_ITEMS, pickerHelper.selectedImages)
        setResult(Activity.RESULT_OK, result)
        finish()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.ll_dir -> {
                showPopupFolderList()
            }
            R.id.btn_ok -> {
                setResult()
            }
            R.id.btn_preview -> {
                ImagePreviewActivity.startForResult(this, REQUEST_PREVIEW, 0, pickerHelper.selectedImages)
            }
            R.id.btn_back -> {
                setResult(Activity.RESULT_CANCELED)
                finish()
            }
        }
    }

    override fun onImagesLoaded(imageFolders: List<ImageFolder>) {
        this.imageFolders = imageFolders
        adapter.refreshData(imageFolders[0].images)
        recyclerView.adapter = adapter
    }

    override fun onDestroy() {
        super.onDestroy()
        imageDataSource.destroyLoader()
    }

}
