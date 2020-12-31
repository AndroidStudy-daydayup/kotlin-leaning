package com.zzzl.coroutines.flow5

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun simple(): Flow<Int> = flow {
    println("Flow started")
    for (i in 1..3){
        delay(100)
        emit(i)
    }
}

fun main() {

    // 流是冷的
    /**
     *  Flow 是一种类似于序列的冷流 —
     *  这段 flow 构建器中的代码直到流被收集的时候才运行。这在以下的示例中非常明显：
     */

    runBlocking {
        println("Calling simple function...")
        val flow = simple()
        println("Calling collect...")
        flow.collect { v -> println(v) }
        println("Calling collect again...")
        flow.collect { v -> println(v) }
    }
}
