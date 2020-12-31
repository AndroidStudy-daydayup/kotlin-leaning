package com.zzzl.coroutines

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // 开始协程

        /**
         *
         * 协程是什么
         *      协程是一种在程序中处理并发任务的方案；也是这种方案的一个组件。
         *      就像是 Adapter ，比如 RecyclerView 的 Adapter。Adapter 那个类即是适配的方案，也是适配的组件本身。
         *
         *      官方解释
         *      协程是一种并发设计模式，您可以在 Android 平台上使用它来简化异步执行的代码。
         *
         *      就像是 java 用什么来处理并发
         *          java 里面用线程来处理并发，使用线程就是一个方案，而线程本身也是 java 的一个类。
         *
         *      而协程，就想线程一样。和线程是同级别的。线程也是解决并发的方案。但厉害的是，线程不存在并行的概念。
         *          并发和并行，不是一个概念。
         *          并发宏观一点，两件完整的事情同时做。 Concurrency
         *          并行微观一点，两件事同一时刻同时做。 ParallelSM
         *
         *       协程不需要关系并发和并行的区别
         *          因为协程不可避免的要面对多线程出现的安全问题
         *
         *        但是协程本质还是线程
         *        java groovy kotlin 底层都要使用 jvm 的线程机制
         *
         *  kotlin 的协程是什么
         *      就是一个线程框架
         *      kotlin 的协程，实际上说的是，kotlin 在 jvm 上的协程。
         *      kotlin 是一个多平台的，kotlin 在其他平台上的协程，就不一定是协程了。
         *
         * 凡事问一句「本质是什么」
         *
         * 学技术是需要本质的
         */


        // 终于到写代码了

        // 1、 区别
//        // 开启一个协程
//        GlobalScope.launch {
//            println("Coroutine Camp 协程 ${Thread.currentThread().name}")
//        }

//        // 使用 Thread
//        Thread{
//            println("Coroutine Camp Thread ${Thread.currentThread().name}")
//        }.start()
//        // 更加简单的写法
//        thread{
//            println("Coroutine Camp thread ${Thread.currentThread().name}")
//        }


        // 2、应用场景
        // 前台后台任务
        //ioCode1()
//        uiCode1()
//        ioCode2()
//        uiCode2()
//        ioCode3()
//        uiCode3()
//        ioCode4()
//        uiCode4()
//        ioCode5()
//        uiCode5()

        // 前台后台一直切换线程（切3次）
//        thread {
//            ioCode1()
//            runOnUiThread{
//                uiCode1()
//                thread {
//                    ioCode2()
//                    runOnUiThread{
//                        uiCode2()
//                        thread {
//                            ioCode3()
//                            runOnUiThread {
//                                uiCode3()
//                            }
//                        }
//                    }
//                }
//            }
//        }

        // 3、协程
        // 用协程来解决
        // ioCode 这些方法要使用 suspend 来挂起
        // launcher() 可以添加参数的，默认是后台线程 DefaultDispatcher-worker-1。可以添加指定线程，比如 Main 线程。
        GlobalScope.launch(Dispatchers.Main) {
            ioCode1()
            uiCode1()
            ioCode2()
            uiCode2()
            ioCode3()
            uiCode3()
        }

        /**
         * 用 launch() 开启一段协程
         *      GlobalScope.launch() 是完整的写法， launch 是顶部函数
         * 一般来说指定 Main 线程
         *      launch(Dispatchers.Main){}
         *
         * 协程额外的天然优势：性能
         *      工作耗时：放到后台
         *      工作特殊：放到指定线程————一般来说是主线程
         *
         * 更新 ui 操作是容易看出来的
         *      比如 TextView.setText()
         *
         * 但是，其他很多函数，是不容易知道是否是耗时代码的。
         *      尤其是很多方法并不是你自己写的。
         *      所以这时候，一个方法是否放到主线程中，就很难判断了。
         *
         *  用协程来处理
         *      从主线程开启一个协程，更新 UI 的代码一路写下来就可以了。
         *      把其他代码，可能耗时不明显的和耗时很长的，全部挂起到其他线程。
         *      自动切回到主线程的操作，是不用你管的。协程自动完成。
         *
         *  而且，更加安全的是。加上 suspend 之后，方法必须要在协程里调用。
         *      如果一个函数加了 suspend 关键字，
         *      那么这个方法必须要在协程里调用或者在另外一个 suspend 关键字修饰的函数中。
         *      否则 编译期就会报错。提醒你这个函数需要写在协程里。
         *
         *  学东西学到本质，就很爽。
         */


        /**
         * suspend 并不是用来切线程的
         *      关键作用：标记和提醒
         */


        /**
         * 除了 launch{} 创建一个协程
         * async{} 也可以创建一个协程
         * 官方解释：另一个启动协程的方法是 async {}。它类似于 launch {}，但返回一个 Deferred<T> 实例，它拥有一个 await() 函数来返回协程执行的结果。Deferred<T> 是一个非常基础的 future（还支持完全成熟的 JDK future，但是现在我们将局限于我们自己的 Deferred）。
         * https://www.kotlincn.net/docs/tutorials/coroutines/coroutines-basic-jvm.html
         *
         * suspend 函数，会在协程中立即挂起。
         *      挂起就是从当前线程脱离了（切到指定线程）
         *      协程最厉害的地方是，挂起函数执行完毕后，会把线程切回来。
         *
         *      所以，所谓的挂起，其实就是一个「稍后」会被「自动」切回来的线程切换。
         *      切回来的动作，叫做 resume （恢复）
         *
         *  为什么 suspend 函数必须在协程里或者 suspend 中调用？
         *       因为 suspend 需要 resume（恢复）（也就是线程切换）
         *       而 resume（恢复）这个功能，是协程的。或者你写在另外的 suspend 函数中，而这个 suspend 函数，还是终究还是需要在协程里。
         *
         *  suspend 并不是线程切换的点，并不是发生在你外部这个挂起函数被调用的时候
         *       而是你挂起函数中的 withContext(Dispatchers.io) 这个挂起函数中。
         *       当然，如果你再往代码里面钻一下，你肯定会发现，这个 withContext() 也不是切换线程的点，而是它其中的另外一个函数。但这并不重要了。
         *       这一切都为了说明，suspend 并不是在切换线程。而是做了一个标记，标记这个函数是需要切线程并且要恢复线程（切回来）的。
         *
         *       所以，真正的挂起动作，实际上是 kotlin 自带的挂起函数来做的，比如 withContext()。
         *
         *       suspend 的定位，不是用来「挂起函数」，而是「定义」这个函数是一个挂起函数。
         *
         *       总的来说，suspend 本质只是一个提醒。
         *              提醒这个函数，是一个自动放到后台的耗时操作，需要使用协程进行线程切换。
         *              为了编码安全，写上 suspend 用来标记这是一个挂起函数，需要用在协程里面，不写在协程中这个函数就会编译期报错。
         *
         *  什么使用使用 suspend ？
         *      原则：耗时，就定义为 suspend 。（特殊情况，等待。）
         *
         *  delay(10_000) 也是挂起函数
         *      等待10秒，再执行下面的代码。
         *
         *  线程切换，都是非阻塞式的。
         *  协程是「看起来阻塞，但实际上是非阻塞式」的写法而已。
         *
         *  在 kotlin 里，协程就是基于线程而实现的一套更上层的工具 API
         *          类似于 Java 自带的 Executor 系列 API，以及 Android 的 Handler 系列 API。
         *
         *  kotlin 官方错误
         *      kotlin 官方中讲到，协程是一种轻量级线程。同时执行 10w 个线程，程序跑步起来（内存溢出），而跑 10w 个协程，就可以。
         *      这种说法，是完全错误的。是有误导性的。
         *
         *      因为这是很狡猾的。官方代码中对比的是 Java 的 Thread 和 kotlin 的协程。
         *          这是偷换概念，Java 中也有相对于线程的管理 API，那就是 Executor 系列 API。
         *          而 kotlin 的协程 应该和 Executor 进行对比，不应该和 Thread 来进行对比。
         *          线程池重复利用了线程，所以比每次都新开线程肯定性能高很多。
         *          kotlin 的协程内部就有线程池，直接拿线程池和每次都开线程来比较，这是不公平的。
         *          这也许是官方的猫腻，故意这么说的。
         *
         */
    }

    private fun uiCode1() {
        println("Coroutine Camp uiCode1 ${Thread.currentThread().name}")
    }
    private fun uiCode2() {
        println("Coroutine Camp uiCode2 ${Thread.currentThread().name}")
    }
    private fun uiCode3() {
        println("Coroutine Camp uiCode3 ${Thread.currentThread().name}")
    }
    private fun uiCode4() {
        println("Coroutine Camp uiCode4 ${Thread.currentThread().name}")
    }
    private fun uiCode5() {
        println("Coroutine Camp uiCode5 ${Thread.currentThread().name}")
    }


    suspend fun ioCode1() {
        withContext(Dispatchers.IO){
            println("Coroutine Camp ioCode1 ${Thread.currentThread().name}")
        }
    }
    suspend fun ioCode2() {
        withContext(Dispatchers.IO){
            println("Coroutine Camp ioCode2 ${Thread.currentThread().name}")
        }
    }
    suspend fun ioCode3() {
        withContext(Dispatchers.IO){
            println("Coroutine Camp ioCode3 ${Thread.currentThread().name}")
        }
    }
    suspend fun ioCode4() {
        println("Coroutine Camp ioCode4 ${Thread.currentThread().name}")
    }
    suspend fun ioCode5() {
        println("Coroutine Camp ioCode5 ${Thread.currentThread().name}")
    }

    fun coroutine(view: View) {
        startActivity(Intent(this,CoroutinesActivity().javaClass))
    }

    fun coroutineAction(view: View) {
        startActivity(Intent(this,CoroutineActionActivity().javaClass))
    }


}