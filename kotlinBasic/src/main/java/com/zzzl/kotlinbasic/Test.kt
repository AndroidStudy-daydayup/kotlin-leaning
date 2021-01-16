package com.zzzl.kotlinbasic

fun main() {
//    dataClassCopy()

    testRunCatching()
}

private fun dataClassCopy() {
    // data class copy() 的用法
    val user = User("张三", 18)
    println(user)
    val userChange = user.copy(age = 19)
    println(userChange)
}


fun testRunCatching(){
    // 如果直接错误，程序就终止了。
//    1/0

    // 使用 runCatch 可以捕获异常
    runCatching {
        1/0
    }.onSuccess {
        println("this is onSuccess")
    }.onFailure {
        println("this is onFailure")
        it.printStackTrace()
    }
    // 捕获之后这行还是会执行
    println("this is end")
}