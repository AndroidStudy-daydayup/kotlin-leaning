package com.zzzl.callback.other_server

import android.os.Handler
import com.zzzl.callback.net.NetWorkCallback
import com.zzzl.callback.net.NetWorkUtils
import java.io.File

object ImageService {

    fun initImageService(){
        // 初始化配置
    }

    var i = 0
    fun downloadImage(callback: ImageDownloadCallback){
        i++
        callback.onImageDownloadStart()
        // 模拟
        NetWorkUtils.requestApi(object: NetWorkCallback {
            override fun onNetSuccess(data: String) {
                if(i % 4 == 0){
                    // 保存图片
                    callback.onImageDownloadComplete(File("====== 我是路径 ====="))
                }else{
                    callback.onImageDownloadError("保存图片，其他错误。")
                }
            }

            override fun onNetFail(m: String) {
                // 可能因为网络问题保存错误
                callback.onImageDownloadError(m)
            }

        })
    }

    fun loadImage(imageFile: File,callback: ImageLoadCallback){
        callback.onImageLoadStart()
        // 模拟
        if(i % 4 == 0){
            Handler().postDelayed({
                callback.onImageLoadComplete()
            },2000)
        }else{
            Handler().postDelayed({
                callback.onImageLoadError("加载图片错误")
            },2000)
        }
    }

}