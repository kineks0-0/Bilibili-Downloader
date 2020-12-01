package com.studio.owo.bilibilidownloader

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.studio.owo.bilibilidownloader.core.api.`interface`.BiliBiliApiService
import com.studio.owo.bilibilidownloader.ui.VideoInfoFragment
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : AppCompatActivity() {


    private  val videoInfoFragment by lazy { VideoInfoFragment() }

    //var transaction = supportFragmentManager.beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //supportFragmentManager.beginTransaction().add(VideoInfoFragment(),"VideoInfo").commit()addToBackStack("VideoInfo").
        val id = "AV23339072"
        supportFragmentManager.beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
            .add(R.id.main_layout, videoInfoFragment, id).addToBackStack(id)
            .commit()


        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.bilibili.com/") //设置网络请求的Url地址
            .addConverterFactory(GsonConverterFactory.create()) //设置数据解析器
            .build()
        val biliApiService = retrofit.create(BiliBiliApiService::class.java)

        //val call = biliApiService.getBvVideoInfo("BV16Z4y137j8")
        val call = biliApiService.getAvVideoInfo(23339072)
        videoInfoFragment.onResult(call)

    }


    override fun onResume() {
        super.onResume()

        // 判断是否需要运行时申请权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED) {
            // 判断是否需要对用户进行提醒，用户点击过拒绝&&没有勾选不再提醒时进行提示
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )) {
                // 给用于予以权限解释, 对于已经拒绝过的情况，先提示申请理由，再进行申请
                toast("程序需要读写权限来读取缓存和导出歌曲")
                requestPermissions(
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    REQUEST_PERMISSION_CODE_WRITE_EXTERNAL_STORAGE
                )
            } else {
                // 无需说明理由的情况下，直接进行申请。如第一次使用该功能（第一次申请权限），用户拒绝权限并勾选了不再提醒
                // 将引导跳转设置操作放在请求结果回调中处理
                requestPermissions(
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    REQUEST_PERMISSION_CODE_WRITE_EXTERNAL_STORAGE
                )
            }
        }/* else {
            // 拥有权限直接进行功能调用
            //refreshFruits(mainViewPageHome.recyclerView.adapter as NeteaseMusicSongAdapter)
           val s = StringBuilder()
            MusicFileProvider.NeteaseMusicCacheFolder.listFiles().forEach {
                if (it!=null) s.append(it.name).append("\n")
            }
            CoreApplication.toast("Size: " + s)
        }*/



    }

    /*private var mContent: Fragment? = null
    fun switchContent(from: Fragment, to: Fragment) {

        if (mContent == null||mContent!!.id != to.id) {
            mContent = to
            val transaction: FragmentTransaction =
                supportFragmentManager.beginTransaction()/*.setCustomAnimations(
                    android.R.anim.slide_in_left, android.R.anim.slide_out_right
                )*/
            if (!to.isAdded) {    // 先判断是否被add过
                transaction.hide(from).add(R.id.main_layout, to)
                    .commit() // 隐藏当前的fragment，add下一个到Activity中
            } else {
                transaction.hide(from).show(to).commit() // 隐藏当前的fragment，显示下一个
            }
        }
    }

    fun backContent(from: Fragment){
        if (mContent==null) return
        val transaction: FragmentTransaction =
            supportFragmentManager.beginTransaction()/*.setCustomAnimations(
                android.R.anim.slide_in_left, android.R.anim.slide_out_right
            )*/
        transaction.show(mContent!!).commit() // 隐藏当前的fragment，显示下一个
    }*/

            override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode) {
            REQUEST_PERMISSION_CODE_WRITE_EXTERNAL_STORAGE -> {
                // 判断用户是否同意了请求
                if (grantResults.size == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //refreshFruits(mainViewPageHome.recyclerView.adapter as NeteaseMusicSongAdapter)
                } else {
                    // 未同意的情况
                    if (ActivityCompat.shouldShowRequestPermissionRationale(
                            this,
                            Manifest.permission.CALL_PHONE
                        )
                    ) {
                        // 给用于予以权限解释, 对于已经拒绝过的情况，先提示申请理由，再进行申请
                        toast("程序需要读写权限保证正常运行")
                    }/* else {
                        // 用户勾选了不再提醒，引导用户进入设置界面进行开启权限
                        /*Snackbar.make(view, "需要打开权限才能使用该功能，您也可以前往设置->应用。。。开启权限",
                            Snackbar.LENGTH_INDEFINITE)
                            .setAction("确定") {
                                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                intent.data = Uri.parse("package:$packageName")
                                startActivityForResult(intent,REQUEST_SETTINGS_CODE)
                            }
                            .show()*/
                    }*/
                }
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }



}