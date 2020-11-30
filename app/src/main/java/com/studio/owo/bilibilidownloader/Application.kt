package com.studio.owo.bilibilidownloader

import android.content.Context

class Application : android.app.Application() {

    companion object {
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()
        context = this.applicationContext
    }

}