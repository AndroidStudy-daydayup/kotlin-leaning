package com.zzzl.callback

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.zzzl.callback.database.DBCallback
import com.zzzl.callback.net.NetWorkCallback
import com.zzzl.callback.other_server.ImageDownloadCallback
import com.zzzl.callback.other_server.ImageLoadCallback
import java.io.File

class KotlinCallbackActivity: AppCompatActivity() {

    private val TAG = "KotlinCallbackActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kotlin_callback)
        // java 式回调
        SomeThingManager.initService(object : DBCallback{
            override fun onDBSuccess(data: String) {
                Log.d(TAG,"==>onDBSuccess()==>$data")
                showMessage(data)
            }

            override fun onDBFail(m: String) {
                Log.d(TAG,"==>onDBFail()==>$m")
            }

        })
    }

    fun net(v: View){
        // java 式回调
        SomeThingManager.requestNetWorkData(object : NetWorkCallback{
            override fun onNetSuccess(data: String) {
                Log.d(TAG,"==>onNetSuccess()==>$data")
                showMessage(data)
            }

            override fun onNetFail(m: String) {
                Log.d(TAG,"==>onNetFail()==>$m")
                showMessage(m)
            }

        })
    }

    fun db(v: View){
        // 简单高阶函数回调
        SomeThingManager.requestDBData({
            Log.d(TAG,"==>db()==>onSuccess")
        },{
            Log.d(TAG,"==>db()==>onError")
        })
    }

    fun imageDownload(v: View){
        SomeThingManager.downloadImage(object : ImageDownloadCallback{
            override fun onImageDownloadComplete(imageFile: File) {
                Log.d(TAG,"==>onImageDownloadComplete()==>${imageFile.absolutePath}")
            }

            override fun onImageDownloadError(m: String) {
                Log.d(TAG,"==>onImageDownloadError()==>${m}")
                showMessage(m)
            }

            override fun onImageDownloadStart() {
                Log.d(TAG,"==>onImageDownloadStart()==>")
            }

        })
    }

    fun imageLoad(v: View){
        SomeThingManager.loadImage(File("----- 文件路径 ----"),object : ImageLoadCallback{
            override fun onImageLoadComplete() {
                Log.d(TAG,"==>onImageLoadComplete()==>")
            }

            override fun onImageLoadError(m: String) {
                Log.d(TAG,"==>onImageLoadError()==>$m")
                showMessage(m)
            }

            override fun onImageLoadStart() {
                Log.d(TAG,"==>onImageLoadStart()==>")
            }

        })
    }

    fun otherService(v: View){
        // java 调用 kotlin
        startActivity(Intent(KotlinCallbackActivity@this,JavaActivity::class.java))
    }

    fun showMessage(message: String){
        Toast.makeText(KotlinCallbackActivity@ this, message, Toast.LENGTH_SHORT).show()
    }
}