package com.studio.owo.bilibilidownloader.ui

import android.os.Bundle
import android.os.Environment
import android.os.Environment.getExternalStoragePublicDirectory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.studio.owo.bilibilidownloader.R
import com.studio.owo.bilibilidownloader.core.api.TResult
import com.studio.owo.bilibilidownloader.core.api.`interface`.BiliBiliApiService
import com.studio.owo.bilibilidownloader.core.api.dataclass.VideoInfo
import com.studio.owo.bilibilidownloader.core.api.dataclass.VideoPlayUrl
import com.studio.owo.bilibilidownloader.core.net.HttpUtil
import com.studio.owo.bilibilidownloader.toast
import kotlinx.android.synthetic.main.fragment_video_info.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.IOException
import java.lang.reflect.Type


class VideoInfoFragment : Fragment() {

    private val rdp by lazy { 10 }
    private val options: RequestOptions by lazy {
        RequestOptions
            .bitmapTransform(RoundedCorners(rdp))
            .placeholder(R.drawable.view_background)
            .error(R.drawable.view_background2)
    }
    private val requestManager: RequestManager by lazy { Glide.with(this) }

    lateinit var videoInfoResult: VideoInfo


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_video_info, container, false)//super.onCreateView(inflater, container, savedInstanceState)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()

        downloadButton.setOnClickListener {
            val retrofit = Retrofit.Builder()
                .baseUrl("https://api.bilibili.com/") //设置网络请求的Url地址
                .addConverterFactory(GsonConverterFactory.create()) //设置数据解析器
                .build()
            val biliApiService = retrofit.create(BiliBiliApiService::class.java)

            val call = biliApiService.getVideoPlayUrl(videoInfoResult.bvid, videoInfoResult.cid)

            call.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    //Log.d(this@VideoInfoFragment::class.java.simpleName,response.body()!!.string())
                    val json = Gson()
                    val userType: Type = object : TypeToken<TResult<VideoPlayUrl>>() {}.type
                    val infoResult: TResult<VideoPlayUrl> = json.fromJson(
                        response.body()!!.string(), userType
                    )
                    val playUrl = infoResult.data
                    Log.d(this@VideoInfoFragment::class.java.simpleName, playUrl.toString())
                    Log.d(this@VideoInfoFragment::class.java.simpleName, playUrl.dUrl[0].url)
                    Log.d(this@VideoInfoFragment::class.java.simpleName, "UserAgent\n" + HttpUtil.getUserAgent())

                    /*post {
                        val request = DownloadManager.Request(Uri.parse(playUrl.dUrl[0].url))
                        //val request = DownloadManager.Request(Uri.parse("https://material.io/resources/icons/static/ic_icons_192px_light.svg"))

                        request.addRequestHeader("User-Agent", HttpUtil.getUserAgent())
                        request.addRequestHeader("referer","https://www.bilibili.com/")
                        request.addRequestHeader("origin","https://www.bilibili.com/")
                        //request.addRequestHeader("cookie","")

                        //设置在什么网络情况下进行下载
                        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI)
                        //设置通知栏标题
                        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                        request.setVisibleInDownloadsUi(true)
                        request.setTitle("下载")
                        request.setDescription("正在下载" + videoInfoResult.bvid )
                        //request.setAllowedOverRoaming(false)
                        //设置文件存放目录
                        /*request.setDestinationInExternalFilesDir(
                            getApplicationContext(),
                            Environment.DIRECTORY_MOVIES,
                            videoInfoResult.bvid// + " - " + videoInfoResult.title + ".flv"
                        )
                        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).mkdir()*/
                        request.setDestinationInExternalPublicDir( Environment.DIRECTORY_DOWNLOADS , videoInfoResult.bvid )
                        //request.setDestinationUri(Uri.fromFile(File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),videoInfoResult.bvid)))

                        val downManager = getApplicationContext().getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                        val id = downManager.enqueue(request)
                        toast("Downloading $id")
                    }*/

                    HttpUtil.sendHttpRequest(playUrl.dUrl[0].url,"https://www.bilibili.com/video/" + videoInfoResult.aid , object : okhttp3.Callback {
                        override fun onFailure(call: okhttp3.Call, e: IOException) {
                            Log.e(
                                this@VideoInfoFragment::class.java.simpleName,
                                e.message,
                                e
                            )
                        }

                        override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {

                            val dirMovies: File =
                                getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES)
                            dirMovies.mkdirs()
                            val outFile = File(
                                dirMovies,
                                videoInfoResult.bvid + " - " + videoInfoResult.title + ".mp4"
                            )

                            outFile.writeBytes(response.body()!!.bytes())
                            toast("Downloaded")
                        }

                    })

                    HttpUtil.sendHttpRequest(videoInfoResult.pic,"https://www.bilibili.com/video/" + videoInfoResult.aid , object : okhttp3.Callback {
                        override fun onFailure(call: okhttp3.Call, e: IOException) {
                            Log.e(
                                this@VideoInfoFragment::class.java.simpleName,
                                e.message,
                                e
                            )
                        }

                        override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {

                            val dirMovies: File =
                                getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                            dirMovies.mkdirs()
                            val outFile = File(
                                dirMovies,
                                videoInfoResult.bvid + " - " + videoInfoResult.title + ".jpg"
                            )

                            outFile.writeBytes(response.body()!!.bytes())
                            toast("Downloaded")
                        }

                    })

                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.e(
                        this@VideoInfoFragment::class.java.simpleName,
                        t.message,
                        t
                    )
                }
            })
            /*requestManager
                .load(videoInfoResult.pic)
                .apply(options)
                .into(it as ImageView)*/
        }
    }

    fun onResult(call: Call<ResponseBody>) {
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                //toast(response.body()!!.string())
                //toast(response.toString())
                val json = Gson()
                val userType: Type = object : TypeToken<TResult<VideoInfo>>() {}.type
                val infoResult: TResult<VideoInfo> = json.fromJson(
                    response.body()!!.string(),
                    userType
                )
                videoInfoResult =
                    infoResult.data//gson.fromJson(response.body()!!.string(), VideoInfo::class.java)
                //toast(videoInfoResult.toString())
                requestManager
                    .load(videoInfoResult.pic)
                    .apply(options)
                    .into(this@VideoInfoFragment.ImageView)
                this@VideoInfoFragment.TitleTextView.text = videoInfoResult.title
                this@VideoInfoFragment.Title2TextView.text = videoInfoResult.owner.name

            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e(
                    this@VideoInfoFragment::class.java.simpleName,
                    t.message,
                    t
                )
            }
        })
    }
}