package com.zzzl.callback.net

interface NetWorkCallback {
    fun onNetSuccess(data: String)
    fun onNetFail(m: String)
}