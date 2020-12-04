package com.zzzl.callback.database

import android.os.Handler
import android.util.Log

object DataBaseUtils {

    private val TAG = "DataBaseUtils"

    lateinit var dbCallback: DBCallback

    fun initDB(callback: DBCallback){
        this.dbCallback = callback
    }

    private fun simulateDbSuccess(onSuccess: () -> Unit){
        simulateDelay {
            dbCallback.onDBSuccess("db 成功")
            onSuccess()
        }
    }


    private fun simulateDbError(onError: () -> Unit){
        simulateDelay {
            dbCallback.onDBFail("db 错误")
            onError()
        }

    }

    private fun simulateDelay(doWork: () -> Unit){
        Handler().postDelayed({
            doWork()
        },3000)
    }

    var i = 0
    fun requestDB(onSuccess: () -> Unit, onError: () -> Unit ){
        i++
        Log.d(TAG,"==>requestDB()==>")
        if( i % 2 == 0){
            simulateDbSuccess(onSuccess)
        }else{
            simulateDbError(onError)
        }
    }

}