# ImagePicker

## 前言
用Kotlin实现的一个仿微信的图片选择库

这是一个我在学习Kotlin过程中的一个练手项目，写这个的库的起因是在使用原作者的库时发现了库的bug，查看github发现作者已经声明不在维护这个库了。非常遗憾，只能clone了代码，自己修复bug。正巧当时正在学习Kotlin，于是便决定用Kotlin复写一下这个库，当做是学习练手。

首先感谢一下原作者：
[https://github.com/jeasonlzy/ImagePicker](https://github.com/jeasonlzy/ImagePicker)

## 效果

![sample1](https://github.com/huburt-Hu/ImagePicker/raw/master/screen/device-2017-11-01-111126.png)
![sample2](https://github.com/huburt-Hu/ImagePicker/raw/master/screen/device-2017-10-16-095841.png)
![sample3](https://github.com/huburt-Hu/ImagePicker/raw/master/screen/device-2017-11-01-111209.png)


## 原理细节
想了解库中的实现原理和细节，请戳：[Kotlin 实战翻译 —— 仿微信图片选择开源库ImagePicker](http://www.jianshu.com/p/8561b1d1f763)

## 此库依赖

```
    compile 'com.android.support:appcompat-v7:25.3.1'
    compile 'com.android.support:recyclerview-v7:25.3.1'
    compile 'com.android.support:support-v4:25.3.1'
    compile 'com.github.chrisbanes:PhotoView:1.3.1'
```

## 集成
> 项目build.gradle添加

```
allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}
```
> module的build.gradle添加

```
dependencies {
	compile 'com.github.huburt-Hu:ImagePicker:1.0'
}
```
此库中有着对com.android.support的依赖，如果你的项目中也使用了这些库，建议排除我的库中的引用，避免依赖冲突，可通过如下配置（ps：确保你的项目中有appcompat-v7，recyclerview-v7，support-v4，否者会报异常，inflate xml 找不到对应类）：

```
dependencies {
	compile('com.github.huburt-Hu:ImagePicker:1.0') {
        exclude group: 'com.android.support'
    }
}
```
> 根据自己的图片框架实现 com.huburt.library.loader.ImageLoader 接口

下面是Glide实现的示例

```
import android.app.Activity
import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.huburt.library.R
import com.huburt.library.loader.ImageLoader
import java.io.File

class GlideImageLoader : ImageLoader {

    override fun displayImage(activity: Activity, path: String, imageView: ImageView, width: Int, height: Int) {
        Glide.with(activity)                             //配置上下文
                .load(Uri.fromFile(File(path)))      //设置图片路径(fix #8,文件名包含%符号 无法识别和显示)
                .error(R.drawable.ic_default_image)           //设置错误图片
                .placeholder(R.drawable.ic_default_image)     //设置占位图片
                .into(imageView)
    }

    override fun displayImagePreview(activity: Activity, path: String, imageView: ImageView, width: Int, height: Int) {
        Glide.with(activity)                             //配置上下文
                .load(Uri.fromFile(File(path)))      //设置图片路径(fix #8,文件名包含%符号 无法识别和显示)
                .into(imageView)
    }

    override fun clearMemoryCache() {

    }
}
```
> 在Application的onCreate中初始化

```
		//初始化图片加载器
        ImagePicker.init(GlideImageLoader())
        //设置全局自定义默认配置
        ImagePicker
                .limit(9)//选择图片数量限制，默认9张
                .isCrop(true)//拍照后是否裁剪，默认false
                .showCamera(true)//列表是否显示拍照，默认true
                .multiMode(true)//false单选/true多选，默认true
                .saveAsDefault()//保存为自定义
```

> 使用

1.选择配置，可以省略，使用临时配置（如有）或者默认

```
		//使用自定义默认参数或者默认参数
        ImagePicker.defaultConfig();
        //使用临时图片参数,有效期持续到下次调用defaultConfig()或者clear()方法
//        ImagePicker.limit(12);
```

2.调用选择图片

Kotlin调用

```
ImagePicker.pick(this, object : ImagePicker.OnPickImageResultListener {
                override fun onImageResult(imageItems: ArrayList<ImageItem>) {
                    //imageItems只会size为0，不会为null
                    }
                }
            })
```
java调用

```
ImagePicker.pick(MainJavaActivity.this, new ImagePicker.OnPickImageResultListener() {
                    @Override
                    public void onImageResult(@NotNull ArrayList<ImageItem> imageItems) {

                    }
                });
```
3.直接打开相机，一次只能获取1张图片

```
ImagePicker.camera(context,listener）//参数同pick
```
4.回顾已选择图片，可以删除，listener返回删除后的集合

```
ImagePicker.review(context,position,listener)
```


**listener返回ImageItem集合，ImageItem包含了图片的信息：**

| 字段     |  含义    |
| ------- | :--------:|
| path    |  图片的路径 |
| name     | 图片的名字 |
| size     | 图片的大小 |
| width    | 图片的宽度 |
| height    | 图片的高度 |
| mimeType   | 图片的类型 |


## License

 ```
Copyright 2017 huburt-Hu

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```