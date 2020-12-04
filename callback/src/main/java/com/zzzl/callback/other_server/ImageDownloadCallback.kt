package com.zzzl.callback.other_server

import android.graphics.Bitmap
import java.io.File

interface ImageDownloadCallback {
    fun onImageDownloadComplete(imageFile: File)
    fun onImageDownloadError(m: String)
    fun onImageDownloadStart()
}