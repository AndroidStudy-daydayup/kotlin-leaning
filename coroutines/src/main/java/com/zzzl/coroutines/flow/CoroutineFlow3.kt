package com.zzzl.coroutines.flow3

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

class CoroutineFlow3{
}

suspend fun simple(): List<Int> {
    delay(1000) // 假装我们在这里做了一些异步的事情
    return listOf(1, 2, 3)
}

fun main() = runBlocking<Unit> {
    simple().forEach { value -> println(value) }

    /**
     * 这段代码将会在等待一秒之后打印数字。
     */
}

