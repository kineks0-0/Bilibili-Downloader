package com.studio.owo.bilibilidownloader.core.api

data class TResult<T>(val code: Int, val massages: String, val data: T)
