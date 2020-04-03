package com.example.mvvm_example.utility

import com.example.mvvm_example.BuildConfig

object LogPrinter {

    fun printMessage(tag: String, message: String) {
        if (BuildConfig.DEBUG) println("TAG --- $tag ---> $message")
    }
}