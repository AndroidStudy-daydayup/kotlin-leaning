package com.zzzl.kotlinbasic

// abstract 抽象类
// open var name  这时候才可以重写，否则字段是 final 不可以重写。
abstract class Person(open var name: String,open var age: Int) {}

// 集成抽象类
data class Student(override var name: String, override var age: Int, var grade: Int): Person(name,age){}

// 集成抽象类
data class Worker(override var name: String, override var age: Int, var lengthOfService: Int): Person(name,age){}

data class User(val name: String, val age: Int){}
