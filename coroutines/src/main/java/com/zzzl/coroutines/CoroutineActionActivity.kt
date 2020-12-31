package com.zzzl.coroutines

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableField
import androidx.lifecycle.*
import io.reactivex.Single
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_coroutine_action.*
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path


class CoroutineActionActivity : AppCompatActivity() {

    lateinit var binding: com.zzzl.coroutines.databinding.ActivityCoroutineActionBinding

    // Retrofit 对协程的支持 (2.6以后加入了对协程的支持)
    val retrofit = Retrofit.Builder()
        .baseUrl("https://api.github.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
        .build()
    val service = retrofit.create(GitHubService::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_coroutine_action)

        // 昨天是理论，今天是实战。


        // 单独用 Retrofit 的写法
//        doOrigin()

        // 协程做
//        doWithCoroutine()

        // RxJava
//        doWithRxJava()

        // 同时做两个请求
//        doTwoWorkOnlyRetrofit()
//        doTwoWorkWithRxJava()
//        doTwoWorkWithCoroutine()


        // 协程泄漏
        // 当页面有协程执行，用户突然退出页面，如果协程不取消，它会继续执行到任务完成，而这个执行是不必要的，甚至会因为页面销毁导致程序崩溃，这时候就是协程泄漏。
        // 协程的泄漏，本质上就是线程的泄漏。线程运转占用内存，就属于内存泄漏。
        /**
         * 协程泄漏和内存泄漏
         * asyncTask 和 onCLick 的对比
         *
         * 所以项目中使用协程的时候，都不会使用 GlobalScope.launch{} 因为 GlobalScope 创建的是全局协程，不能取消。
         * 我们都会使用自定义的 Scope，或者 Android 为我们提供的 Scope，比如 MainScope。
         */

        // 上面 doWithCoroutine() 方法里，只写了请求，没有写取消请求。
        // 这里演示了协程的取消，来避免泄漏。
//        cancelCoroutine()
//        cancelTowCoroutine()


        // ktx 提供的 lifecycleScope ，跟随生命周期。
//         lifecycleScope()

        // ViewMode 和 协程的组合使用
        viewModelWithCoroutine()

        // LiveData 和 协程的组合使用
        liveDataWithCoroutine()
    }

    private fun liveDataWithCoroutine() {
//        // liveData 中已经写好了配合协程的扩展函数 liveData{}
        binding.viewModeLiveData = ViewModelProvider(this).get(RengViewModelLiveData::class.java)
        binding.lifecycleOwner = this
    }


    private fun cancelTowCoroutine() {
        // 因为协程不在合适的地方取消，也会产生内存泄漏。所以我们要关注一下如何取消协程。

        // Scope 就是范围，就是协程活动的空间。
        // 调用 Scope.cancel() 来取消这个空间内的所有协程。（但是 GlobalScope 是不能取消的。）

        // 可以创建一个主线程的 Scope，MainScope（Android 提供）。// 或者自定义 Scope
        val scope = MainScope()
        // 主线程的 Scope 就不需要加 Dispatchers.Main 了。
        scope.launch {
            val resp = service.listReposKT("rengwuxian")
            textView3.text = resp[0].name
        }

        // 创建第二个协程
        scope.launch {
            val resp = service.listReposKT("rengwuxian")
            textView3.text = resp[0].name
        }

        // 创建第三个协程
        scope.launch {
            val resp = service.listReposKT("rengwuxian")
            textView3.text = resp[0].name
        }

        // 取消操作，会把所有用 scope 创建的协程都取消掉。
        // 你把 scope 换成成员变量，然后放到 onDestroy() 里统一取消。
//        scope.cancel()

        // 这个叫做结构化并发（Structure Concurrency）


        // 但是依然，还得自己创建一个对象 scope。这还是有点麻烦。
        // 这时候你需要 Google 提供的组件了 lifecycleScope（ktx 库中的）

    }

    private fun doTwoWorkWithCoroutine() {
        // 使用协程同时请求两个接口，然后合并结果显示
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val one = async { service.listReposKT("rengwuxian") }
                val two = async { service.listReposKT("google") }
                // async 返回一个 Deferred 对象，使用 await() 方法获取返回值
                // await() 是一个挂起函数，挂起函数相当于立即返回。等待子协程回来，然后继续执行。这个过程不卡主线程的。
                // 等第一个 await() 回来结果，再拼接 -> ，然后又遇到第二个 await() ，继续挂起（代码相当于立即返回），等待子协程回来，继续拼接完整字符串。
                // 然后赋值给 textView3.text。
                textView3.text = "${one.await()[0].name} -> ${two.await()[0].name}"
            } catch (e: Exception) {
                textView3.text = e.message
            }
        }

//        // 如果第二个请求需要第一个请求的结果作为参数
//        GlobalScope.launch(Dispatchers.Main) {
//            try {
//                val one = service.listReposKT("rengwuxian")
//                val two = service.listReposKT(one[0].name)
//                // async 返回一个 Deferred 对象，使用 await() 方法获取返回值
//                // await() 是一个挂起函数，挂起函数相当于立即返回。等待子协程回来，然后继续执行。这个过程不卡主线程的。
//                // 等第一个 await() 回来结果，再拼接 -> ，然后又遇到第二个 await() ，继续挂起（代码相当于立即返回），等待子协程回来，继续拼接完整字符串。
//                // 然后赋值给 textView3.text。
//                textView3.text = "${one[0].name} -> ${two[0].name}"
//            } catch (e: Exception) {
//                textView3.text = e.message
//            }
//        }
    }

    private fun doTwoWorkWithRxJava() {
        // 使用 rxjava 的操作符 zip 合并两个请求
        val disposable: Disposable
        disposable = Single.zip(
            service.listReposRxJava("rengwuxian"),
            service.listReposRxJava("google"),
            { list1, list2 ->
                "${list1[0].name} -> ${list2[0].name}"
            })
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                    combined ->
                textView3.text = combined
            }

//        disposable.dispose()
    }

    private fun doTwoWorkOnlyRetrofit() {
        // 单独使用 Retrofit
        // 在 callback 中再次请求。
        // 麻烦又耗时。

        service.listRepos("rengwuxian")
            .enqueue(object : Callback<List<Repo>?> {
                override fun onResponse(call: Call<List<Repo>?>, response: Response<List<Repo>?>) {
                    val name = response.body()?.get(0)?.name
                    service.listRepos("google")
                        .enqueue(object : Callback<List<Repo>?> {
                            override fun onResponse(
                                call: Call<List<Repo>?>,
                                response: Response<List<Repo>?>
                            ) {
                                textView3.text = "${name} -> ${response.body()?.get(0)?.name}"
                            }

                            override fun onFailure(call: Call<List<Repo>?>, t: Throwable) {
                                textView3.text = t.message
                            }
                        })
                }

                override fun onFailure(call: Call<List<Repo>?>, t: Throwable) {
                    textView3.text = t.message
                }

            })
    }

    private fun lifecycleScope() {
        // LifecycleScope 更加牛逼，无与伦比。(ktx 库中的)
        lifecycleScope.launch {
            // 这里的协程会自动在 lifecycle 生命周期中自动取消
        }

        lifecycleScope.launch {
            try {
                val resp = service.listReposKT("rengwuxian")
                textView3.text = resp[0].name
            } catch (e: Exception) {
                // 用 try catch 来捕获异常
                textView3.text = e.message
            }
        }

        lifecycleScope.launch {
            try {
                val resp = service.listReposKT("rengwuxian")
                textView3.text = resp[0].name
            } catch (e: Exception) {
                // 用 try catch 来捕获异常
                textView3.text = e.message
            }
        }

        lifecycleScope.launch {
            try {
                val resp = service.listReposKT("rengwuxian")
                textView3.text = resp[0].name
            } catch (e: Exception) {
                // 用 try catch 来捕获异常
                textView3.text = e.message
            }
        }

        // 自动在生命周期中开启协程
        lifecycleScope.launchWhenCreated { }
        lifecycleScope.launchWhenResumed { }
        lifecycleScope.launchWhenStarted { }
    }

    private fun viewModelWithCoroutine() {
        // ViewModeScope
        binding.viewMode  = ViewModelProvider(this)[RengViewModel::class.java]
    }

    var job: Job? = null
    private fun cancelCoroutine() {
        // 可以用 Job 来取消（Job 关注的是运行相关的事，所以不是用 Coroutine 来命名。）
        job = GlobalScope.launch(Dispatchers.Main) {
            try {
                val resp = service.listReposKT("rengwuxian")
                textView3.text = resp[0].name
            } catch (e: Exception) {
                // 用 try catch 来捕获异常
                textView3.text = e.message
            }
        }
        // 看 onDestroy() 方法中的取消
    }

    override fun onDestroy() {
        super.onDestroy()
//        if(job!!.isCancelled){
//            job?.cancel()
//        }
    }

    private fun doWithRxJava() {
        service.listReposRxJava("rengwuxian")
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : SingleObserver<List<Repo>> {
                override fun onSubscribe(d: Disposable) {

                }

                override fun onSuccess(t: List<Repo>) {
                    textView3.text = t[0].name
                }

                override fun onError(e: Throwable) {
                    textView3.text = e.message
                }

            })

//        // RxJava 的取消
//        var disposable:Disposable? = null
//
//        service.listReposRxJava("rengwuxian")
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe(object: SingleObserver<List<Repo>>{
//                override fun onSubscribe(d: Disposable) {
//                    disposable = d
//                }
//
//                override fun onSuccess(t: List<Repo>) {
//                    textView3.text = t[0].name
//                }
//
//                override fun onError(e: Throwable) {
//                    textView3.text = e.message
//                }
//
//            })
//        disposable?.dispose()
//        // 当然，你也可以使用 CompositeDisposable 整合所有的请求
    }

    private fun doWithCoroutine() {
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val resp = service.listReposKT("rengwuxian")
                textView3.text = resp[0].name
            } catch (e: Exception) {
                // 用 try catch 来捕获异常
                textView3.text = e.message
            }
        }
    }

    /**
     * Retrofit 原本的请求
     */
    private fun doOrigin() {
        service.listRepos("rengwuxian")
            .enqueue(object : Callback<List<Repo>> {
                override fun onResponse(call: Call<List<Repo>>, response: Response<List<Repo>>) {
                    textView3.text = response.body()!![0].name
                }

                override fun onFailure(call: Call<List<Repo>>, t: Throwable) {
                    t.printStackTrace()
                    textView3.text = t.message
                }

            })

        // 开启子线程
//        thread {
//            service.listRepos("rengwuxian")
//                .enqueue(object: Callback<List<Repo>>{
//                    override fun onResponse(call: Call<List<Repo>>, response: Response<List<Repo>>) {
//                        runOnUiThread{
//                            textView3.setText(response.body()!![0].name)
//                        }
//                    }
//
//                    override fun onFailure(call: Call<List<Repo>>, t: Throwable) {
//                        t.printStackTrace()
//                    }
//
//                })
//        }

//        // 如果你需要取消
//        val call = service.listRepos("rengwuxian")
//        call.enqueue(object: Callback<List<Repo>>{
//                override fun onResponse(call: Call<List<Repo>>, response: Response<List<Repo>>) {
//                    textView3.text = response.body()!![0].name
//                }
//
//                override fun onFailure(call: Call<List<Repo>>, t: Throwable) {
//                    t.printStackTrace()
//                    textView3.text = t.message
//                }
//
//            })
//        call.cancel()
    }
}

interface GitHubService {
    @GET("users/{user}/repos")
    fun listRepos(@Path("user") user: String): Call<List<Repo>>

    // 添加了 suspend 挂起关键字，自动放放到后台执行，而且不需要 Call 了。
    @GET("users/{user}/repos")
    suspend fun listReposKT(@Path("user") user: String): List<Repo>

    @GET("users/{user}/repos")
    fun listReposRxJava(@Path("user") user: String): Single<List<Repo>> // 当然你也可以返回 Observable。 因为网络请求是单次的，所以可以 Single 是合适的。
}

data class Repo(val name: String){
}

/**
 * ViewMode 中的 LiveData
 */
class RengViewModel: ViewModel(){

    // Retrofit 对协程的支持 (2.6以后加入了对协程的支持)
    val retrofit = Retrofit.Builder()
        .baseUrl("https://api.github.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
        .build()
    val service = retrofit.create(GitHubService::class.java)

    var respo =  ObservableField<Repo>()

    init {
        respo.set(Repo("点击那个 Button "))
    }

    fun getRespoName(){
        viewModelScope.launch {
            respo.set(service.listReposKT("rengwuxian")[0])
            println("viewMode 的 respo：-> ${respo.get()}")
        }
    }
}

/**
 * ViewMode 中的 LiveData
 */
class RengViewModelLiveData: ViewModel(){
    // liveDataScope 创建的协程（这是 LiveData 对协程支持的扩展函数）
    var repos = liveData {
        emit(listResposKt()[0])
    }

    suspend fun listResposKt(): List<Repo>{
        // Retrofit 对协程的支持 (2.6以后加入了对协程的支持)
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .build()
        val service = retrofit.create(GitHubService::class.java)

        return service.listReposKT("rengwuxian")
    }

    fun gogogo(){

    }
}