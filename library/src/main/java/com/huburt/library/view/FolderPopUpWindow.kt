package com.huburt.library.view

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.AdapterView
import android.widget.BaseAdapter
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.PopupWindow

import com.huburt.library.R

class FolderPopUpWindow(context: Context, adapter: BaseAdapter
) : PopupWindow(context), View.OnClickListener {

    private val listView: ListView
    private var onItemClickListener: OnItemClickListener? = null
    private val masker: View
    private val marginView: View
    private var marginPx: Int = 0
    private var outSet: AnimatorSet? = null
    private var enterSet: AnimatorSet? = null

    init {

        val view = View.inflate(context, R.layout.pop_folder, null)
        masker = view.findViewById(R.id.masker)
        masker.setOnClickListener(this)
        marginView = view.findViewById(R.id.margin)
        marginView.setOnClickListener(this)
        listView = view.findViewById(R.id.listView) as ListView
        listView.adapter = adapter

        contentView = view
        width = ViewGroup.LayoutParams.MATCH_PARENT  //如果不设置，就是 AnchorView 的宽度
        height = ViewGroup.LayoutParams.MATCH_PARENT
        isFocusable = true
        isOutsideTouchable = true
        setBackgroundDrawable(ColorDrawable(0))
        animationStyle = 0
        view.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                view.viewTreeObserver.removeOnGlobalLayoutListener(this)
                Log.e("hubert", "view created")
                val maxHeight = view.height * 5 / 8
                val realHeight = listView.height
                val listParams = listView.layoutParams
                listParams.height = if (realHeight > maxHeight) maxHeight else realHeight
                listView.layoutParams = listParams
                val marginParams = marginView.layoutParams as LinearLayout.LayoutParams
                marginParams.height = marginPx
                marginView.layoutParams = marginParams

                initEnterSet()
                enterSet?.start()
            }
        })
        listView.onItemClickListener = AdapterView.OnItemClickListener { adapterView, v, position, l -> onItemClickListener?.onItemClick(adapterView, v, position, l) }
    }

    private fun initEnterSet() {
        val alpha = ObjectAnimator.ofFloat(masker, "alpha", 0f, 1f)
        val translationY = ObjectAnimator.ofFloat(listView, "translationY", listView.height.toFloat(), 0f)
        enterSet = AnimatorSet()
        enterSet!!.duration = 400
        enterSet!!.playTogether(alpha, translationY)
        enterSet!!.interpolator = AccelerateDecelerateInterpolator()
    }

    override fun showAtLocation(parent: View, gravity: Int, x: Int, y: Int) {
        super.showAtLocation(parent, gravity, x, y)
        enterSet?.start()
    }

    override fun dismiss() {
        exitAnimator()
    }

    private fun exitAnimator() {
        if (outSet == null) {
            val alpha = ObjectAnimator.ofFloat(masker, "alpha", 1f, 0f)
            val translationY = ObjectAnimator.ofFloat(listView, "translationY", 0f, listView.height.toFloat())
            outSet = AnimatorSet()
            outSet!!.duration = 300
            outSet!!.playTogether(alpha, translationY)
            outSet!!.interpolator = AccelerateDecelerateInterpolator()
            outSet!!.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationStart(animation: Animator) {
                    listView.visibility = View.VISIBLE
                }

                override fun onAnimationEnd(animation: Animator) {
                    super@FolderPopUpWindow.dismiss()
                }
            })
        }
        //防止重复点击
        if (outSet!!.isRunning) {
            return
        }
        outSet!!.start()
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.onItemClickListener = listener
    }

    fun setSelection(selection: Int) {
        listView.setSelection(selection)
    }

    fun setMargin(marginPx: Int) {
        this.marginPx = marginPx
    }

    override fun onClick(v: View) {
        dismiss()
    }

    interface OnItemClickListener {
        fun onItemClick(adapterView: AdapterView<*>, view: View, position: Int, l: Long)
    }
}
