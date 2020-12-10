package com.studio.owo.bilibilidownloader.ui

import android.R.attr.*
import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.ClipData
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.text.InputType
import android.util.Log
import android.view.*
import android.webkit.MimeTypeMap
import android.widget.EditText
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.studio.owo.bilibilidownloader.R
import com.studio.owo.bilibilidownloader.core.api.*
import com.studio.owo.bilibilidownloader.core.api.`interface`.BiliBiliApiService
import com.studio.owo.bilibilidownloader.core.api.dataclass.*
import com.studio.owo.bilibilidownloader.getApplicationContext
import kotlinx.android.synthetic.main.activity_video_info.*
import kotlinx.android.synthetic.main.fragment_video_info.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type


class VideoInfoFragment : Fragment() {

    private val rdp by lazy { 10 }
    private val optionsRadius: RequestOptions by lazy {
        RequestOptions
            .bitmapTransform(RoundedCorners(rdp))
            .placeholder(R.drawable.view_background_radius)
            .error(R.drawable.view_background2_radius)
    }
    private val options: RequestOptions by lazy {
        RequestOptions
            .placeholderOf(R.drawable.view_background)
            .error(R.drawable.view_background2)
    }
    private val requestManager: RequestManager by lazy { Glide.with(this) }

    lateinit var videoInfoResult: VideoInfo
    lateinit var resultInfo: TResult<VideoInfo>

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://api.bilibili.com/") //设置网络请求的Url地址
        .addConverterFactory(GsonConverterFactory.create()) //设置数据解析器
        .build()
    private val biliApiService = retrofit.create(BiliBiliApiService::class.java)
    private val json = Gson()


    private var downloadManagerQueryID = -1L
    private val downManager by lazy { getApplicationContext().getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_video_info, container, false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()



        /*shareButton.setOnLongClickListener {
            MaterialAlertDialogBuilder(it.context)
                .setTitle("VideoInfoResult To String")
                .setMessage(videoInfoResult.toString())
                .setNegativeButton("取消") { dialog, _ -> dialog.dismiss() }
                .setPositiveButton("拷贝") { dialog, which ->
                    dialog.dismiss()
                    //获取剪贴板管理器：
                    val cm =
                        getApplicationContext().getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
                    // 创建普通字符型ClipData
                    val mClipData = ClipData.newPlainText("Label", videoInfoResult.toString())
                    // 将ClipData内容放到系统剪贴板里。
                    cm.setPrimaryClip(mClipData)
                    Snackbar.make(it, "Text Copied", Snackbar.LENGTH_SHORT).show()
                }
                .show()
            true
        }*/



        extendedFab.setOnClickListener {
            val call = biliApiService.getVideoPlayUrl(videoInfoResult.bvid, videoInfoResult.cid)
            call.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    val userType: Type = object : TypeToken<TResult<VideoPlayUrl>>() {}.type
                    val resultInfo: TResult<VideoPlayUrl> = json.fromJson(response.body()!!.string(), userType)
                    val playUrl = resultInfo.data

                    Snackbar.make(
                        it ,
                        "Downloading Video - " + videoInfoResult.title,
                        Snackbar.LENGTH_LONG
                    )
                        .setAction("CANCEL") {
                            if (downloadManagerQueryID == -1L) return@setAction
                            try {
                                downManager.remove(downloadManagerQueryID)
                            } catch (e: Exception) {
                                Log.e(this@VideoInfoFragment::class.simpleName, e.message, e)
                            }

                        }
                        .show()

                    //Log.d(this@VideoInfoFragment::class.java.simpleName, playUrl.toString())
                    //Log.d(this@VideoInfoFragment::class.java.simpleName, playUrl.dUrl[0].url)
                    //Log.d(this@VideoInfoFragment::class.java.simpleName, "UserAgent\n" + HttpUtil.getUserAgent())
                    val url = playUrl.dUrl[0].url
                    val request = DownloadManager.Request(Uri.parse(url))

                    request.addRequestHeader("User-Agent", "Bilibili Freedoooooom/MarkII")
                    request.addRequestHeader("referer", "https://www.bilibili.com/")
                    request.addRequestHeader("origin", "https://www.bilibili.com/")
                    //request.addRequestHeader("cookie","")

                    val fileExtension = MimeTypeMap.getFileExtensionFromUrl(url)
                    request.setMimeType(
                        MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension)
                    )

                    //request.setMimeType("application/vnd.android.package-archive");
                    //设置在什么网络情况下进行下载
                    //request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI)
                    //设置通知栏标题
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                    //request.setTitle("下载")
                    //request.setDescription("正在下载" + videoInfoResult.bvid )
                    //request.setAllowedOverRoaming(false)
                    //设置文件存放目录
                    request.setDestinationInExternalPublicDir(
                        Environment.DIRECTORY_MOVIES,
                        videoInfoResult.owner.name
                                + " - " + videoInfoResult.title
                                + "_" + videoInfoResult.bvid
                                + "_" + playUrl.format
                                + "." + fileExtension
                    )
                    downloadManagerQueryID = downManager.enqueue(request)
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.e(this@VideoInfoFragment::class.java.simpleName, t.message, t)
                }
            })
        }


        videoPicturesImageView.setOnClickListener {

        }
        videoPicturesImageView.setOnLongClickListener {
            Snackbar.make(
                it ,
                "保存封面?",
                Snackbar.LENGTH_LONG
            )
                .setAction("保存") {
                    val url = videoInfoResult.pic
                    val fileExtension = MimeTypeMap.getFileExtensionFromUrl(url)
                    val request = DownloadManager.Request(Uri.parse(url))
                    request.addRequestHeader("User-Agent", "Bilibili Freedoooooom/MarkII")
                    request.addRequestHeader("referer", "https://www.bilibili.com/")
                    request.addRequestHeader("origin", "https://www.bilibili.com/")
                    request.setMimeType(
                        MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension)
                    )
                    //设置在什么网络情况下进行下载
                    //request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI)
                    //设置通知栏标题
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                    //request.setTitle("下载")
                    //request.setDescription("正在下载" + videoInfoResult.bvid )
                    //request.setAllowedOverRoaming(false)
                    //设置文件存放目录
                    //Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).mkdir()
                    request.setDestinationInExternalPublicDir(
                        Environment.DIRECTORY_PICTURES,
                        videoInfoResult.owner.name
                                + " - " + videoInfoResult.title
                                + " _" + videoInfoResult.bvid
                                + "." + fileExtension
                    )

                    downloadManagerQueryID = downManager.enqueue(request)
                    Snackbar.make(
                        it ,
                        "下载中",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
                .show()
            true
        }


    }






    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.search -> {
                val container = ConstraintLayout(view!!.context)
                val lp = ConstraintLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                lp.setMargins(40, 0, 40, 0)
                val input = EditText(view!!.context)
                input.layoutParams = lp
                input.gravity = Gravity.TOP //or Gravity.LEFT
                input.inputType =
                    InputType.TYPE_TEXT_FLAG_CAP_SENTENCES or InputType.TYPE_TEXT_FLAG_MULTI_LINE
                input.setLines(1)
                input.maxLines = 1
                input.setText(videoInfoResult.bvid)//"lastDateValue"
                container.addView(input, lp)

                //(editText.layoutParams as MarginLayoutParams).setMargins(50)editText.requestLayout()

                MaterialAlertDialogBuilder(view!!.context)
                    .setTitle("BiliBili Video ID 跳转")
                    .setMessage("输入AV号或者BV号")
                    .setNegativeButton("取消") { _ , _ -> }
                    .setPositiveButton("确认") { dialog, _ ->
                        dialog.dismiss()

                        if (input.text.isEmpty()) {
                            Snackbar.make(this.view!! ,"错误：请先输入BiliBili Video ID",Snackbar.LENGTH_SHORT).show()
                            return@setPositiveButton
                        }
                        val inputText = input.text.toString()
                        VideoInfoActivity.launchActivity(activity!!,inputText)
                    }
                    .setView(container)
                    .show()
            }
            R.id.info -> {

                val stringBuilder by lazy {
                    StringBuilder()
                        .append(
                            "请求状态  :  " +
                                    resultInfo.code + " " + resultInfo.massages
                                    + "  （" +
                                    when (resultInfo.code) {
                                        0 -> "成功"
                                        -400 -> "请求错误"
                                        -403 -> "权限不足"
                                        -404 -> "无视频"
                                        62002 -> "稿件不可见"
                                        else -> "未知"
                                    } + ")" +


                                    "\n视频标题  :  " + videoInfoResult.title +
                                    "\n视频分区  :  " + videoInfoResult.tagName + " - " + videoInfoResult.tid +
                                    "\n视频时长  :  " + videoInfoResult.duration +
                                    "\n视频发布  :  " + videoInfoResult.publicDate +
                                    "\n视频创建  :  " + videoInfoResult.createTime +
                                    "\n视频分P   :  " + videoInfoResult.videos +
                                    "\n视频类型  :  " + videoInfoResult.copyright + " " + when (videoInfoResult.copyright) {
                                1 -> "原创"
                                else -> "转载"
                            } +
                                    "\n视频UP主  :  " + videoInfoResult.owner.name + "  (mid:" + videoInfoResult.owner.mid + ")" +
                                    "\n视频状态  :  " + videoInfoResult.state + "  (" +
                                    when (videoInfoResult.state) {
                                        0 -> "开放浏览"
                                        1 -> "橙色通过"
                                        -1 -> "待审"
                                        -2 -> "被打回"
                                        -3 -> "网警锁定"
                                        -4 -> "被锁定"
                                        -5 -> "管理员锁定 (可浏览)"
                                        -6 -> "修复待审"
                                        -7 -> "暂缓待审"
                                        -8 -> "补档待审"
                                        -9 -> "等待转码"
                                        -10 -> "延迟审核"
                                        -11 -> "视频源待修"
                                        -12 -> "转储失败"
                                        -13 -> "允许评论待审"
                                        -14 -> "临时回收站"
                                        -15 -> "分发中"
                                        -16 -> "转码失败"
                                        -20 -> "创建未提交"
                                        -30 -> "创建已提交"
                                        -40 -> "定时发布"
                                        -100 -> "用户删除"
                                        else -> "未知"
                                    } + ")" +

                                    "\n视频简介  :  \n\n" + videoInfoResult.desc + "\n\n\n" +
                                    "\n视频属性  :  " +
                                    "\n视频封面  :  " + videoInfoResult.pic +
                                    "\n" +
                                    "\n" +
                                    "\n"


                        )
                }

                MaterialAlertDialogBuilder(view!!.context)
                    .setTitle("视频数据")
                    .setMessage(stringBuilder)
                    /*.setNeutralButton("R.string.cancel") { dialog, which ->
                        // Respond to neutral button press
                    }*/
                    .setNegativeButton("ok") { dialog, which ->

                    }
                    .setPositiveButton("copy") { dialog, which ->
                        dialog.dismiss()
                        //获取剪贴板管理器：
                        val cm =
                            getApplicationContext().getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
                        // 创建普通字符型ClipData
                        val mClipData = ClipData.newPlainText("Label", stringBuilder)
                        // 将ClipData内容放到系统剪贴板里。
                        cm.setPrimaryClip(mClipData)
                        Snackbar.make(view!!, "Text Copied", Snackbar.LENGTH_SHORT).show()
                    }
                    .show()
            }
            R.id.share -> {
                Snackbar.make(view!!,item.title,Snackbar.LENGTH_SHORT).show()
            }
            R.id.more -> {
                Snackbar.make(view!!,item.title,Snackbar.LENGTH_SHORT).show()
            }
            else -> {Snackbar.make(view!!,"Unknown",Snackbar.LENGTH_SHORT).show()}

        }
        return true
    }





    fun onResult(call: Call<ResponseBody>) {
        call.enqueue(object : Callback<ResponseBody> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {

                val userType: Type = object : TypeToken<TResult<VideoInfo>>() {}.type
                val rawJSON = response.body()!!.string()
                resultInfo = json.fromJson(
                    rawJSON,
                    userType
                )
                if (resultInfo.data == null) {
                    Snackbar.make(
                        this@VideoInfoFragment.view!! ,
                        "加载错误 Code " + resultInfo.code,
                        Snackbar.LENGTH_SHORT
                    ).setAction("RawJSON") {
                        MaterialAlertDialogBuilder(it.context)
                            .setTitle("RawJSON")
                            .setMessage(rawJSON)
                            .setNegativeButton("取消") { dialog, _ -> dialog.dismiss() }
                            .setPositiveButton("拷贝") { dialog, which ->
                                dialog.dismiss()
                                //获取剪贴板管理器：
                                val cm =
                                    getApplicationContext().getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
                                // 创建普通字符型ClipData
                                val mClipData = ClipData.newPlainText("Label", rawJSON)
                                // 将ClipData内容放到系统剪贴板里。
                                cm.setPrimaryClip(mClipData)
                                Snackbar.make(it, "Text Copied", Snackbar.LENGTH_SHORT).show()
                            }
                            .show()
                    }.show()
                    videoInfoResult = VideoInfo(
                        -1,"", -1, -1, -1, "", Dimension(-1, -1, -1),
                        -1, "", -1, false, Owner("", -1, ""), listOf(), "", -1,
                        Rights(-1, -1, -1, -1, -1, -1, -1, -1, -1, 1, -1, -1, -1, -1),
                        listOf(Staff("", -1, -1, -1, "", Official("", -1, "", -1), "", Vip(-1, -1, -1, -1))),
                        Stat(-1, "", -1, -1, -1, "", -1, -1, -1, -1, -1, -1, -1),
                        -1, Subtitle(false, listOf()), -1, "", "", UserGarb(""), -1)

                    this@VideoInfoFragment.TitleTextView.text = "videoInfoResult.title"
                    this@VideoInfoFragment.ownerName.text = "videoInfoResult.owner.name"
                    this@VideoInfoFragment.IntroductionTextView.text = "videoInfoResult.desc"
                    return
                }
                videoInfoResult = resultInfo.data
                requestManager
                    .load(videoInfoResult.pic)
                    .apply(options)
                    .into(this@VideoInfoFragment.videoPicturesImageView)
                this@VideoInfoFragment.TitleTextView.text = videoInfoResult.title
                this@VideoInfoFragment.ownerName.text = videoInfoResult.owner.name
                this@VideoInfoFragment.IntroductionTextView.text = videoInfoResult.desc
                //(activity as VideoInfoActivity).topAppBar.title = videoInfoResult.title
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