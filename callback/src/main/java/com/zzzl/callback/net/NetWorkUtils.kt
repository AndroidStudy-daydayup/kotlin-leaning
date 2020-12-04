package com.zzzl.callback.net

import android.os.Handler
import android.util.Log
import com.zzzl.callback.database.DataBaseUtils

object NetWorkUtils {

    private val TAG = "NetWorkUtils"

    fun initNetWorkUtils(){
        // 设置参数
    }

    var i =0

    fun requestApi(callback: NetWorkCallback){
        Log.d(TAG,"==>requestApi()==>")
        // 判断 dbCallback 如何回调
        // nwCallback.onNetSuccess
        // nwCallback.onNetFail

        i++
        // 模拟网络访问
        Handler().postDelayed({
            if(i % 2 == 0){
                callback.onNetSuccess("网络任务 成功")
            }else{
                callback.onNetFail("网络任务 失败")
            }
        },3000)
    }
}