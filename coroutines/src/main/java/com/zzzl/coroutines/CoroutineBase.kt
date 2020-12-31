package com.zzzl.coroutines

import kotlinx.coroutines.*

class KotlinCoroutine {


}

/**
 * 官方示例
 * https://www.kotlincn.net/docs/reference/coroutines/basics.html
 */
fun main() {
    // 协程基础
//    test1()
//    test2()
//    test3()
//    test4()
//    test5()
//    test6()
//    test7()
//    test8()
}

/**
 * 全局协程像守护线程
 */
fun test8() {
    runBlocking {
        GlobalScope.launch {
            repeat(1000){
                i ->
                println("I'm sleeping $i")
                delay(500L)
            }
        }
        delay(1300L)
    }

    /**
     * 输出结果
     * I'm sleeping 0
     * I'm sleeping 1
     * I'm sleeping 2
     *
     * 在 GlobalScope 中启动的活动协程并不会使进程保活。它们就像守护线程。
     *
     */
}

/**
 * 协程很轻
 */
fun test7() {
    /**
     * 它启动了 10 万个协程，并且在 5 秒钟后，每个协程都输出一个点。
     * 现在，尝试使用线程来实现。会发生什么？（很可能你的代码会产生某种内存不足的错误）
     */
    runBlocking {
        repeat(100_000) {
            launch {
                delay(5000L)
                println(".")
            }
        }
    }
}

/**
 * 提取函数重构
 */
fun test6() {

    /**
     * 在协程内部可以像普通函数一样使用挂起函数，
     * 不过其额外特性是，同样可以使用其他挂起函数（如本例中的 delay）来挂起协程的执行。
     */
    // 意思就是 挂起函数中可以使用 delay 来挂起当前协程的执行，
    // 因为delay() 是一个挂起函数。
    runBlocking {
        launch {
            doWorld()
        }
        println("Hello，")
    }
}

suspend fun doWorld() {
    delay(1_000L)
    println("World!")
}

/**
 * 作用域构建器
 */
fun test5() {
    /**
     * 除了由不同的构建器提供协程作用域之外，
     * 还可以使用 coroutineScope 构建器声明自己的作用域。
     * 它会创建一个协程作用域并且在所有已启动子协程执行完毕之前不会结束。
     */

    /**
     * runBlocking 与 coroutineScope 可能看起来很类似，
     * 因为它们都会等待其协程体以及所有子协程结束。
     * 主要区别在于，
     *      runBlocking 方法会阻塞当前线程来等待，
     *      而 coroutineScope 只是挂起，会释放底层线程用于其他用途。
     *
     *      由于存在这点差异，runBlocking 是常规函数，而 coroutineScope 是挂起函数。
     */

    runBlocking { // this: CoroutineScope
        launch {
            println("====>>between launch and coroutineScope code0")
            delay(200L)
            println("Task from runBlocking")
        }
        println("====>>between launch and coroutineScope code1")
        coroutineScope { // 创建一个协程作用域
            println("====>>between launch and coroutineScope code2")
            launch {
                delay(500L)
                println("Task from nested launch")
            }
            println("====>>between launch and coroutineScope code3")
            delay(100L)
            println("Task from coroutine scope") // 这一行会在内嵌 launch 之前输出
        }
        println("====>>between launch and coroutineScope code4")
        println("Coroutine scope is over") // 这一行在内嵌 launch 执行完毕后才输出
    }

    /**
     * 输出结果：
     *  Task from coroutine scope
    Task from runBlocking
    Task from nested launch
    Coroutine Scope is  over

    从这里就能看出：
    runBlocking
    最后一行的 「Coroutine Scope is  over」 最后输出
    说明：runBlocking 是等待子协程（launch{} 和 coroutineScope{}）的时候，是阻塞当前协程的。

    coroutineScope
    coroutineScope域中，
    延迟了100毫秒的「Task from coroutine scope」比延迟了500毫秒的「Task from nested launch」先输出
    说明：coroutineScope 在等待子协程的时候，是挂起了所有子协程，不阻塞当前协程。
     */

}

suspend fun te() {

}

/**
 * 结构化的并发
 * 协程的实际使用还有一些需要改进的地方。
 * 当我们使用 GlobalScope.launch 时，我们会创建一个顶层协程。
 * 虽然它很轻量，但它运行时仍会消耗一些内存资源。如果我们忘记保持对新启动的协程的引用，它还会继续运行。
 * 如果协程中的代码挂起了会怎么样（例如，我们错误地延迟了太长时间），
 * 如果我们启动了太多的协程并导致内存不足会怎么样？
 * 必须手动保持对所有已启动协程的引用并 join 之很容易出错。
 */
fun test4() {
    /**
     * 有一个更好的解决办法。我们可以在代码中使用结构化并发。
     * 我们可以在执行操作所在的指定作用域内启动协程，
     * 而不是像通常使用线程（线程总是全局的）那样在 GlobalScope 中启动。
     */

    // 其实就是直接使用 launch{} 来启动协程
    runBlocking {
        launch {
            delay(1_000L)
            println("World!")
        }
        println("Hello,")
    }
}

/**
 * 等待一个作业
 * 延迟一段时间来等待另一个协程运行并不是一个好的选择。
 * 让我们显式（以非阻塞方式）等待所启动的后台 Job 执行结束：
 */
fun test3() {
    runBlocking {
        val job = GlobalScope.launch { // 启动一个新协程并保持对这个作业的引用
            delay(1000L)
            println("World!")
        }
        println("Hello,")
        job.join() // 等待直到子协程执行结束
    }
    // 现在，结果仍然相同，但是主协程与后台作业的持续时间没有任何关系了。好多了。
}

/**
 * 这里的 runBlocking<Unit> { …… } 作为用来启动顶层主协程的适配器。
 * 我们显式指定了其返回类型 Unit，因为在 Kotlin 中 main 函数必须返回 Unit 类型。
 */
fun test2() = runBlocking<Unit> { // 开始执行主协程
    GlobalScope.launch { // 在后台启动一个新的协程并继续
        delay(1000L)
        println("World!")
    }
    println("Hello,") // 主协程在这里会立即执行
    delay(2000L)      // 延迟 2 秒来保证 JVM 存活
}


private fun test1() {
    GlobalScope.launch { // 在后台启动一个新的协程并继续
        delay(1000L) // 非阻塞的等待 1 秒钟（默认时间单位是毫秒）
        println("World!") // 在延迟后打印输出
    }
    println("Hello,") // 协程已在等待时主线程还在继续
    Thread.sleep(2000L) // 阻塞主线程 2 秒钟来保证 JVM 存活

    /**
     * 本质上，协程是轻量级的线程。 它们在某些 CoroutineScope 上下文中与 launch 协程构建器 一起启动。 这里我们在 GlobalScope 中启动了一个新的协程，这意味着新协程的生命周期只受整个应用程序的生命周期限制。

    可以将 GlobalScope.launch { …… } 替换为 thread { …… }，并将 delay(……) 替换为 Thread.sleep(……) 达到同样目的。 试试看（不要忘记导入 kotlin.concurrent.thread）。

    如果你首先将 GlobalScope.launch 替换为 thread，编译器会报以下错误：

    Error: Kotlin: Suspend functions are only allowed to be called from a coroutine or another suspend function
    这是因为 delay 是一个特殊的 挂起函数 ，它不会造成线程阻塞，但是会 挂起 协程，并且只能在协程中使用。
     */
}