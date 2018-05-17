package com.huburt.imagepicker

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import com.huburt.picker.facade.Harry
import com.huburt.picker.ui.widget.GridSpacingItemDecoration
import com.huburt.picker.util.Utils
import kotlinx.android.synthetic.main.activity_main.*

/**
 * Created by hubert
 * <p>
 * Created on 2017/10/31.
 * <p>
 *
 */
class MainActivity : AppCompatActivity() {

    private val adapter: ImageAdapter = ImageAdapter(ArrayList())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btn_pick.setOnClickListener {
            Harry.with(this).forResult(123)
        }
        btn_camera.setOnClickListener {
            //直接打开相机
            Harry.with(this).openCamera(123)
        }
        recycler_view.layoutManager = GridLayoutManager(this, 3)
        val imageAdapter = adapter
        imageAdapter.listener = object : ImageAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {

            }
        }
        recycler_view.addItemDecoration(GridSpacingItemDecoration(3, Utils.dp2px(this, 2f), false))
        recycler_view.adapter = imageAdapter
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 123 && resultCode == Harry.RESULT_OK) {
            val result = Harry.obtainResult(data!!)
            adapter.updateData(result!!)
        }
    }

}
