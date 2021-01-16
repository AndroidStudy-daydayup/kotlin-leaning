package com.zzzl.coroutines

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.android.synthetic.main.activity_second.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SecondActivity: AppCompatActivity() {

    private val TAG = "SecondActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        lifecycleScope.launch {
            Log.d(TAG, "当前线程0: ${Thread.currentThread().name}")
            withContext(Dispatchers.IO){
                Log.d(TAG, "当前线程1.1: ${Thread.currentThread().name}")
                delay(3_000)
                Log.d(TAG, "当前线程1.2: ${Thread.currentThread().name}")
            }
            Log.d(TAG, "当前线程1.3: ${Thread.currentThread().name}")
            editTextTextPersonName.setText("1")
            withContext(Dispatchers.IO){
                Log.d(TAG, "当前线程2.1: ${Thread.currentThread().name}")
                delay(3_000)
                Log.d(TAG, "当前线程2.2: ${Thread.currentThread().name}")
            }
            Log.d(TAG, "当前线程2.3: ${Thread.currentThread().name}")
            editTextTextPersonName.setText("2")
            withContext(Dispatchers.IO){
                Log.d(TAG, "当前线程3.1: ${Thread.currentThread().name}")
                delay(3_000)
                Log.d(TAG, "当前线程3.2: ${Thread.currentThread().name}")
            }
            Log.d(TAG, "当前线程3.2: ${Thread.currentThread().name}")
            editTextTextPersonName.setText("3")
            withContext(Dispatchers.IO){
                Log.d(TAG, "当前线程4.1: ${Thread.currentThread().name}")
                delay(3_000)
                Log.d(TAG, "当前线程4.2: ${Thread.currentThread().name}")
            }
            Log.d(TAG, "当前线程4.3: ${Thread.currentThread().name}")
            editTextTextPersonName.setText("4")
        }
    }
}