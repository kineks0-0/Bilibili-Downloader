package com.studio.owo.bilibilidownloader.ui

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.MenuItem
import com.google.android.material.snackbar.Snackbar
import com.studio.owo.bilibilidownloader.R
import com.studio.owo.bilibilidownloader.core.api.`interface`.BiliBiliApiService
import kotlinx.android.synthetic.main.activity_video_info.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class VideoInfoActivity : AppCompatActivity() {

    companion object {

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
        topAppBar.setOnMenuItemClickListener {
            videoInfoFragment.onOptionsItemSelected(it)
        }

        val videoID: String = intent.getStringExtra("ID") ?: "AV23339072"
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

    /*override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        when(keyCode) {
            KeyEvent.KEYCODE_BACK -> {
                //videoInfoFragment.
                this.finish()
                return true
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return videoInfoFragment.onOptionsItemSelected(item)
    }*/

}