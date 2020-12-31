package com.zzzl.coroutines.flow2

class CoroutineFlow2{

}

fun main() {

    // 序列
    simple().forEach {
        v ->
        println(v)
    }

    /**
     * 这段代码输出相同的数字，但在打印每个数字之前等待 100 毫秒。
     */
}

fun simple(): Sequence<Int> = sequence {
    for (i in 1..3){
        Thread.sleep(100)
        yield(i)
    }
}