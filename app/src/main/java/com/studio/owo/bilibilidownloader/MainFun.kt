package com.studio.owo.bilibilidownloader

import android.content.Context
import android.os.Handler
import android.widget.Toast


val handler = Handler(getApplicationContext().mainLooper)
const val REQUEST_PERMISSION_CODE_WRITE_EXTERNAL_STORAGE = 10

fun test() {}

fun getApplicationContext() : Context = Application.context
fun toast(text: String) = post { Toast.makeText(getApplicationContext(),text,Toast.LENGTH_SHORT).show() }
fun post(run: Runnable) {
    handler.post(run)
}