package com.zzzl.callback.database

interface DBCallback {
    fun onDBSuccess(data: String)
    fun onDBFail(m: String)
}