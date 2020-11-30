package com.studio.owo.bilibilidownloader.core.api.`interface`

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface BiliBiliApiService {

    @GET("x/web-interface/view?")
    fun getAvVideoInfo(@Query("aid") id: Int) : Call<ResponseBody>

    @GET("x/web-interface/view?")
    fun getBvVideoInfo(@Query("bvid") id: String) : Call<ResponseBody>

    @GET("x/player/playurl?")
    fun getVideoPlayUrl(
        @Query("bvid") id: String,
        @Query("cid") cid: Int,
        @Query("qn") qn: Int,
        @Query("fnval") fnval: Int = 0,
        @Query("fnver") fnver: Int = 0,
        @Query("fourk") fourk: Int = 0,
    ) : Call<ResponseBody>

    @GET("x/player/playurl?")
    fun getVideoPlayUrl(
        @Query("bvid") id: String,
        @Query("cid") cid: Int
    ) : Call<ResponseBody>



}