package com.huburt.imagepicker

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import com.huburt.picker.ImagePicker
import com.huburt.picker.bean.ImageItem
import com.huburt.picker.util.Utils
import com.huburt.picker.view.GridSpacingItemDecoration
import kotlinx.android.synthetic.main.activity_main.*

/**
 * Created by hubert
 * <p>
 * Created on 2017/10/31.
 * <p>
 *
 */
class MainActivity : AppCompatActivity(), ImagePicker.OnPickImageResultListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //使用自定义默认参数或者默认参数,并清除Application启动之后选择图片缓存
        ImagePicker.defaultConfig()
        //临时改变选择图片参数
//        ImagePicker.limit(12);
        //默认不裁剪
        cb_crop.setOnCheckedChangeListener({ _, isChecked -> ImagePicker.isCrop(isChecked) })
        cb_multi.isChecked = true//默认是多选
        cb_multi.setOnCheckedChangeListener { _, isChecked -> ImagePicker.multiMode(isChecked) }
        btn_pick.setOnClickListener {
            //选择图片，第二次进入会自动带入之前选择的图片（未重置图片参数）
            ImagePicker.pick(this@MainActivity, this@MainActivity)
        }
        btn_camera.setOnClickListener {
            //直接打开相机
            ImagePicker.camera(this@MainActivity, this@MainActivity)
        }
        recycler_view.layoutManager = GridLayoutManager(this, 3)
        val imageAdapter = ImageAdapter(ArrayList())
        imageAdapter.listener = object : ImageAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                //回顾已选择图片，可以删除
                ImagePicker.review(this@MainActivity, position, this@MainActivity)
            }
        }
        recycler_view.addItemDecoration(GridSpacingItemDecoration(3, Utils.dp2px(this, 2f), false))
        recycler_view.adapter = imageAdapter
    }

    override fun onImageResult(imageItems: ArrayList<ImageItem>) {
        (recycler_view.adapter as ImageAdapter).updateData(imageItems)
    }

    override fun onDestroy() {
        super.onDestroy()
        ImagePicker.clear()//清除缓存已选择的图片
    }
}
