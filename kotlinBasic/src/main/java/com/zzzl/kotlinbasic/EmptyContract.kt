package com.zzzl.kotlinbasic

interface EmptyContract {
    class View: IView{}
    class Mode: IMode() {
        override fun modeTest() {
        }
    }
}