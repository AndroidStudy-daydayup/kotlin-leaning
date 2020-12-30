package com.zzzl.kotlinbasic;

import androidx.appcompat.app.AppCompatActivity;

public abstract class BaseActivity<T extends BasePresenter> extends AppCompatActivity {

    abstract void aaa();

    abstract void bbb();

    abstract T getPresenter();
}
