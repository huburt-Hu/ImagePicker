package com.huburt.library.ui

import android.support.v7.app.AppCompatActivity
import android.widget.Toast

/**
 * Created by hubert
 *
 * Created on 2017/10/12.
 */
open class BaseActivity : AppCompatActivity() {
    fun showToast(charSequence: CharSequence) {
        Toast.makeText(this, charSequence, Toast.LENGTH_SHORT).show()
    }
}