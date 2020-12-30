package com.zzzl.kotlinbasic

class KotlinActivity : BaseActivity<EmptyPresenter>() {
    override fun aaa() {
    }

    override fun bbb() {
    }

    override fun getPresenter(): EmptyPresenter {
        return EmptyPresenter()
    }

}