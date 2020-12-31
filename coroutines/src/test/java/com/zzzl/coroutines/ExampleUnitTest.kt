package com.zzzl.coroutines

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun testMySuspendingFunction(){
        println("====> start")
        runBlocking<Unit> {
            // 这里我们可以使用任何喜欢的断言风格来使用挂起函数
            doIoWork()
            println("====> runBlocking is ok")
        }

    }

    suspend fun doIoWork(){
        withContext(Dispatchers.IO){
            delay(5_000L)
            println("====> doIoWork is ok")
        }
    }
}