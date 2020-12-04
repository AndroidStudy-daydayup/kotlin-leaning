package com.zzzl.callback

import com.zzzl.callback.database.DBCallback
import com.zzzl.callback.database.DataBaseUtils
import com.zzzl.callback.net.NetWorkCallback
import com.zzzl.callback.net.NetWorkUtils
import com.zzzl.callback.other_server.ImageDownloadCallback
import com.zzzl.callback.other_server.ImageLoadCallback
import com.zzzl.callback.other_server.ImageService
import java.io.File

object SomeThingManager {


    init {

    }

    fun initService(dbCallback: DBCallback){
        DataBaseUtils.initDB(dbCallback)
        NetWorkUtils.initNetWorkUtils()
        ImageService.initImageService()
    }

    fun requestDBData(onSuccess: () -> Unit, onError: () -> Unit){
        DataBaseUtils.requestDB(onSuccess = onSuccess, onError = onError)
    }

    fun requestNetWorkData(callback: NetWorkCallback){
        NetWorkUtils.requestApi(callback)
    }

    fun downloadImage(callback: ImageDownloadCallback){
        ImageService.downloadImage(callback)
    }

    fun loadImage(imageFile: File, callback: ImageLoadCallback){
        ImageService.loadImage(imageFile,callback)
    }


}