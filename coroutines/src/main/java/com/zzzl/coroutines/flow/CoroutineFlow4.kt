package com.zzzl.coroutines.flow4

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


fun simple(): Flow<Int> = flow { // 流构建器
    for (i in 1..3) {
        delay(100) // 假装我们在这里做了一些有用的事情
        emit(i) // 发送下一个值
    }
}


fun main() {

    // 流
    /**
     * 使用 List 结果类型，意味着我们只能一次返回所有值。
     * 为了表示异步计算的值流（stream），我们可以使用 Flow 类型（正如同步计算值会使用 Sequence 类型）：
     */

    runBlocking {
        // 启动并发的协程以验证主线程并未阻塞
        launch {
            for (k in 1..3) {
                println("I'm not blocked $k")
                delay(100)
            }
        }
        // 收集这个流
        simple().collect { value -> println(value) }
    }

    /**
     * 注意使用 Flow 的代码与先前示例的下述区别：

        名为 flow 的 Flow 类型构建器函数。
        flow { ... } 构建块中的代码可以挂起。
        函数 simple 不再标有 suspend 修饰符。
        流使用 emit 函数 发射 值。
        流使用 collect 函数 收集 值。
        我们可以在 simple 的 flow { ... } 函数体内使用 Thread.sleep 代替 delay 以观察主线程在本案例中被阻塞了。
     */

}

