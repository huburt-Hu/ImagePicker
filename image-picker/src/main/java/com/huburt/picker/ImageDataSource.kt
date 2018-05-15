package com.huburt.picker

import android.database.Cursor
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.FragmentActivity
import android.support.v4.app.LoaderManager
import android.support.v4.content.CursorLoader
import android.support.v4.content.Loader

import com.huburt.picker.bean.ImageFolder
import com.huburt.picker.bean.ImageItem

import java.io.File
import java.util.ArrayList

class ImageDataSource(private val activity: FragmentActivity) : LoaderManager.LoaderCallbacks<Cursor> {

    private val IMAGE_PROJECTION = arrayOf(//查询图片需要的数据列
            MediaStore.Images.Media.DISPLAY_NAME, //图片的显示名称  aaa.jpg
            MediaStore.Images.Media.DATA, //图片的真实路径  /storage/emulated/0/pp/downloader/wallpaper/aaa.jpg
            MediaStore.Images.Media.SIZE, //图片的大小，long型  132492
            MediaStore.Images.Media.WIDTH, //图片的宽度，int型  1920
            MediaStore.Images.Media.HEIGHT, //图片的高度，int型  1080
            MediaStore.Images.Media.MIME_TYPE, //图片的类型     image/jpeg
            MediaStore.Images.Media.DATE_ADDED)    //图片被添加的时间，long型  1450518608
    private var loadedListener: OnImagesLoadedListener? = null                     //图片加载完成的回调接口
    private val imageFolders = ArrayList<ImageFolder>()   //所有的图片文件夹
    private var currentMode: Int? = null

    fun loadImage(loadedListener: OnImagesLoadedListener) {
        loadImage(null, loadedListener)
    }

    /**
     * @param path           指定扫描的文件夹目录，可以为 null，表示扫描所有图片
     * @param loadedListener 图片加载完成的监听
     */
    fun loadImage(path: String?, loadedListener: OnImagesLoadedListener) {
        this.loadedListener = loadedListener
        destroyLoader()

        val loaderManager = activity.supportLoaderManager
        val bundle = Bundle()
        if (path == null) {
            currentMode = LOADER_ALL
            loaderManager.initLoader(LOADER_ALL, bundle, this)//加载所有的图片
        } else {
            currentMode = LOADER_CATEGORY
            //加载指定目录的图片
            bundle.putString("path", path)
            loaderManager.initLoader(LOADER_CATEGORY, bundle, this)
        }
    }

    override fun onCreateLoader(id: Int, args: Bundle): Loader<Cursor>? {
        var cursorLoader: CursorLoader? = null
        //扫描所有图片
        if (id == LOADER_ALL)
            cursorLoader = CursorLoader(activity, MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    IMAGE_PROJECTION, null, null, IMAGE_PROJECTION[6] + " DESC")
        //扫描某个图片文件夹
        if (id == LOADER_CATEGORY)
            cursorLoader = CursorLoader(activity, MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    IMAGE_PROJECTION, IMAGE_PROJECTION[1] + " like '%" + args.getString("path") + "%'",
                    null, IMAGE_PROJECTION[6] + " DESC")

        return cursorLoader
    }

    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {
        imageFolders.clear()
        if (data != null) {
            val allImages = ArrayList<ImageItem>()   //所有图片的集合,不分文件夹
            while (data.moveToNext()) {
                //查询数据
                val imageName = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[0]))
                val imagePath = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[1]))

                val file = File(imagePath)
                if (!file.exists() || file.length() <= 0) {
                    continue
                }

                val imageSize = data.getLong(data.getColumnIndexOrThrow(IMAGE_PROJECTION[2]))
                val imageWidth = data.getInt(data.getColumnIndexOrThrow(IMAGE_PROJECTION[3]))
                val imageHeight = data.getInt(data.getColumnIndexOrThrow(IMAGE_PROJECTION[4]))
                val imageMimeType = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[5]))
                val imageAddTime = data.getLong(data.getColumnIndexOrThrow(IMAGE_PROJECTION[6]))
                //封装实体
                val imageItem = ImageItem(imagePath)
                imageItem.size = imageSize
                imageItem.addTime = imageAddTime
                imageItem.height = imageHeight
                imageItem.width = imageWidth
                imageItem.mimeType = imageMimeType
                allImages.add(imageItem)
                //根据父路径分类存放图片
                val imageFile = File(imagePath)
                val imageParentFile = imageFile.parentFile
                val imageFolder = ImageFolder(imageParentFile.name, imageParentFile.absolutePath)

                if (!imageFolders.contains(imageFolder)) {
                    val images = ArrayList<ImageItem>()
                    images.add(imageItem)
                    imageFolder.cover = imageItem
                    imageFolder.images = images
                    imageFolders.add(imageFolder)
                } else {
                    imageFolders[imageFolders.indexOf(imageFolder)].images.add(imageItem)
                }
            }
            //防止没有图片报异常
            if (data.count > 0 && allImages.size > 0) {
                //构造所有图片的集合
                val allImagesFolder = ImageFolder(activity.resources.getString(R.string.ip_all_images), "/")
                allImagesFolder.cover = allImages[0]
                allImagesFolder.images = allImages
                imageFolders.add(0, allImagesFolder)  //确保第一条是所有图片
            }
        }

        //回调接口，通知图片数据准备完成
        loadedListener?.onImagesLoaded(imageFolders)
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
        println("--------")

    }

    fun destroyLoader() {
        if (currentMode != null) {
            activity.supportLoaderManager.destroyLoader(currentMode!!)
        }
    }

    /**
     * 所有图片加载完成的回调接口
     */
    interface OnImagesLoadedListener {
        fun onImagesLoaded(imageFolders: List<ImageFolder>)
    }

    companion object {

        val LOADER_ALL = 0         //加载所有图片
        val LOADER_CATEGORY = 1    //分类加载图片
    }
}