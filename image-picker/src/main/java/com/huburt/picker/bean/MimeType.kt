/*
 * Copyright (C) 2014 nohana, Inc.
 * Copyright 2017 Zhihu Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an &quot;AS IS&quot; BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.huburt.picker.bean

import android.support.v4.util.ArraySet

import java.util.Arrays
import java.util.EnumSet

/**
 * MIME Type enumeration to restrict selectable media on the selection activity. Matisse only supports images and
 * videos.
 *
 *
 * Good example of mime types Android supports:
 * https://android.googlesource.com/platform/frameworks/base/+/refs/heads/master/media/java/android/media/MediaFile.java
 */
enum class MimeType constructor(
        private val mMimeTypeName: String,
        private val mExtensions: Set<String>
) {

    // ============== images ==============
    JPEG("image/jpeg", setOf(
            "jpg",
            "jpeg"
    )),
    PNG("image/png", setOf(
            "png"
    )),
    GIF("image/gif", setOf(
            "gif"
    )),
    BMP("image/x-ms-bmp", setOf(
            "bmp"
    )),
    WEBP("image/webp", setOf(
            "webp"
    )),

    // ============== videos ==============
    MPEG("video/mpeg", setOf(
            "mpeg",
            "mpg"
    )),
    MP4("video/mp4", setOf(
            "mp4",
            "m4v"
    )),
    QUICKTIME("video/quicktime", setOf(
            "mov"
    )),
    THREEGPP("video/3gpp", setOf(
            "3gp",
            "3gpp"
    )),
    THREEGPP2("video/3gpp2", setOf(
            "3g2",
            "3gpp2"
    )),
    MKV("video/x-matroska", setOf(
            "mkv"
    )),
    WEBM("video/webm", setOf(
            "webm"
    )),
    TS("video/mp2ts", setOf(
            "ts"
    )),
    AVI("video/avi", setOf(
            "avi"
    ));

    override fun toString(): String {
        return mMimeTypeName
    }

    companion object {

        fun ofAll(): Set<MimeType> {
            return EnumSet.allOf(MimeType::class.java)
        }

        fun of(type: MimeType, vararg rest: MimeType): Set<MimeType> {
            return EnumSet.of(type, *rest)
        }

        fun ofImage(): Set<MimeType> {
            return EnumSet.of(JPEG, PNG, GIF, BMP, WEBP)
        }

        fun ofVideo(): Set<MimeType> {
            return EnumSet.of(MPEG, MP4, QUICKTIME, THREEGPP, THREEGPP2, MKV, WEBM, TS, AVI)
        }
    }
}
