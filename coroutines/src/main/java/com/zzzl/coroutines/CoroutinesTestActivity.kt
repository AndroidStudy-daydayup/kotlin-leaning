package com.zzzl.coroutines

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.*
import java.util.concurrent.atomic.AtomicLong

class CoroutinesTestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coroutine_test)
        println("OK let's go")

//        // 启动一个协程
//        GlobalScope.launch {
//            delay(3_000)
//            println("Hello")
//        }
//        Thread.sleep(4000)
//        println("Stop")
    }
}

fun main() {
    println("Ok let's go")
    printCurrentThreadName()
//    testCoroutinePerformance()

    coroutineAsyncTest()
}

fun printCurrentThreadName(){
//    val stackTrace = Throwable().stackTrace
//    for( i in Throwable().stackTrace.indices){
//        println("index=$i----------------------------------")
//        println("className=" + stackTrace[i].className + "----------------------------------")
//        println("fileName=" + stackTrace[i].fileName)
//        println("methodName=" + stackTrace[i].methodName)
//        println("lineNumber=" + stackTrace[i].lineNumber)
//    }
//    println("当前函数名称 ${Thread.currentThread().stackTrace[0].methodName}")
    println("当前线程名称 ${Thread.currentThread().name}")
}

private fun coroutineAsyncTest() {
    println("====>>coroutineAsyncTest()")
    // async{} 可以返回一个 Deferred 对象，泛型是要返回的值。用 get() 获取。

    val deferred = (1..1_000_000).map { n ->
        GlobalScope.async {
            delay(3_000)
            // 返回 Deferred 对象，泛型是 Int，值是 n。
            n
        }
    }
    // 这样写不可以，编辑器会提示你，await() 是一个挂起函数，需要在协程中或挂起函数中调用。
//    val sum = deferred.sumOf { it.await().toLong() }

    // 放入挂起函数中即可
    runBlocking {
        printCurrentThreadName()
        val sum = deferred.sumOf { it.await().toLong() }
        println("Sum:$sum")
    }
}

/**
 * 测试协程性能
 */
fun testCoroutinePerformance() {
    println("====>>testCoroutinePerformance()")

//    // 这样非常耗时，非常占用线程资源。
//    // 很久才能完成
//    val c = AtomicLong()
//    for (i in 1..1_000_000L) {
//        thread(start = true) {
//            c.addAndGet(i)
//        }
//    }
//    println(c.get())

    // 使用协程来做同样的事
    // 很快就能完成
    val c = AtomicLong()
    for(i in 1..1000_000L){
        GlobalScope.launch {
            c.addAndGet(i)
        }
    }
    println(c.get())
}