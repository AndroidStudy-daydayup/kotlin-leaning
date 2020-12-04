package com.zzzl.callback;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import kotlin.Unit;
import kotlin.jvm.functions.Function0;

public class JavaActivity extends AppCompatActivity {

    private static final String TAG = "JavaActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_java_apply_kotlin_callback);
    }

    public void javaDb(View v){
        // java 调用 kotlin 高阶
        SomeThingManager.INSTANCE.requestDBData(new Function0<Unit>() {
            @Override
            public Unit invoke() {
                Log.d(TAG, "invoke: onSuccess");
                return null;
            }
        }, new Function0<Unit>() {
            @Override
            public Unit invoke() {
                Log.d(TAG, "invoke: onError");
                return null;
            }
        });
    }
}
