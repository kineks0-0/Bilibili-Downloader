package com.studio.owo.bilibilidownloader.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentTransaction
import com.studio.owo.bilibilidownloader.R
import com.studio.owo.bilibilidownloader.core.api.`interface`.BiliBiliApiService
import kotlinx.android.synthetic.main.activity_video_info.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class VideoInfoActivity : AppCompatActivity() {

    companion object {

        /*fun LaunchActivity(This: AppCompatActivity, aid: Int) {
            val intent = Intent(This,VideoInfoActivity::class.java)
            intent.putExtra("Type","AV")
            intent.putExtra("AvID",aid)
            This.startActivity(intent)
        }

        fun LaunchActivity(This: AppCompatActivity, bvid: String) {
            val intent = Intent(This,VideoInfoActivity::class.java)
            intent.putExtra("Type","BV")
            intent.putExtra("BvID",bvid)
            This.startActivity(intent)
        }

        fun launchActivity(This: AppCompatActivity, id: String) {
            val intent = Intent(This,VideoInfoActivity::class.java)
            intent.putExtra("Type","ID")
            intent.putExtra("ID"  , id)
            This.startActivity(intent)
        }*/

        fun launchActivity(This: Activity, id: String) {
            val intent = Intent(This,VideoInfoActivity::class.java)
            intent.putExtra("Type","ID")
            intent.putExtra("ID"  , id)
            This.startActivity(intent)
        }

    }



    private val videoInfoFragment by lazy { videoInfo_Fragment as VideoInfoFragment }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_info)



        val videoID: String = intent.getStringExtra("ID") ?: "AV23339072"
        /*supportFragmentManager.beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
            .add(R.id.main_layout, videoInfoFragment, videoID ).addToBackStack(videoID)
            .commit()*/

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.bilibili.com/") //设置网络请求的Url地址
            .addConverterFactory(GsonConverterFactory.create()) //设置数据解析器
            .build()
        val biliApiService = retrofit.create(BiliBiliApiService::class.java)

        val call = if (videoID[0] == 'A' || videoID[0] == 'a') {
            biliApiService.getAvVideoInfo(videoID.substring(2,videoID.length))
        } else {
            biliApiService.getBvVideoInfo(videoID)
        }
        videoInfoFragment.onResult(call)

    }



}