package com.huburt.imagepicker

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import com.huburt.library.ImagePicker
import com.huburt.library.bean.ImageItem
import com.huburt.library.util.Utils
import com.huburt.library.view.GridSpacingItemDecoration

class MainActivity : AppCompatActivity(), ImagePicker.OnPickImageResultListener {
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ImagePicker.prepare().limit(8)//初始化选择图片参数，会一直保留直到下次调用prepare或resetConfig方法
        findViewById(R.id.tv).setOnClickListener({
            //选择图片，第二次进入会自动带入之前选择的图片（未重置图片参数）
            ImagePicker.pick(this@MainActivity, this@MainActivity)
        })

        recyclerView = findViewById(R.id.recycler_view) as RecyclerView
        recyclerView.layoutManager = GridLayoutManager(this, 3)
        val imageAdapter = ImageAdapter(ArrayList())
        imageAdapter.listener = object : ImageAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                //回顾已选择图片，可以删除
                ImagePicker.review(this@MainActivity, position, this@MainActivity)
            }
        }
        recyclerView.addItemDecoration(GridSpacingItemDecoration(3, Utils.dp2px(this, 2f), false))
        recyclerView.adapter = imageAdapter
    }

    override fun onImageResult(imageItems: ArrayList<ImageItem>) {
        (recyclerView.adapter as ImageAdapter).updateData(imageItems)
    }

}
