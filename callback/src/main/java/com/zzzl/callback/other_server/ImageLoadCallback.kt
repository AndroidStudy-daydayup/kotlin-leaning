package com.zzzl.callback.other_server

import android.graphics.Bitmap
import java.io.File

interface ImageLoadCallback {
    fun onImageLoadComplete()
    fun onImageLoadError(m: String)
    fun onImageLoadStart()
}