package com.zzzl.coroutines.flow1

class CoroutineFlow{


}

fun main() {

    /**
     * 异步流
     * 挂起函数可以异步的返回单个值，但是该如何异步返回多个计算好的值呢？这正是 Kotlin 流（Flow）的用武之地。
     */
    // 意思就是字面意思

    // 表示多个值
    simple().forEach { v ->
        println(v)
    }

    // 序列
    fun simple(): Sequence<Int> = sequence {
        for (i in 1..3){
            Thread.sleep(100)
            yield(i)
        }
    }
}

fun simple(): List<Int> = listOf(1,2,3)
