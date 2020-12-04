package com.zzzl.kotlinstudy

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.edit
import com.zzzl.callback.KotlinCallbackActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    fun goKotlinCallback(view: View) {
        startActivity(Intent(MainActivity@this,KotlinCallbackActivity::class.java))
    }

}

//var ManagerCallback: (code:Int, key:String) -> Unit

//class Manager(){
//
//    private lateinit var managerCallback: (Int, String) -> Unit
//
//    fun setListener(callback: (code:Int, key:String) -> Unit){
//        this.managerCallback = callback
//    }
//
//    fun onSuccess(){
//        managerCallback(0,"成功")
//    }
//
//    fun onFail(){
//        managerCallback(1,"失败")
//    }
//}
