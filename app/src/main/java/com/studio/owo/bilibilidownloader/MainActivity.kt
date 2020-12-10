package com.studio.owo.bilibilidownloader

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.studio.owo.bilibilidownloader.ui.VideoInfoActivity


class MainActivity : AppCompatActivity() {



    //var transaction = supportFragmentManager.beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


    }

    override fun onStart() {
        super.onStart()
        VideoInfoActivity.launchActivity(this,"AV23339072")
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
                //toast("程序需要读写权限来读取缓存和导出歌曲")
                Snackbar.make(this.findViewById(R.id.main_layout),
                    "程序需要读写权限来读取缓存和导出歌曲", Snackbar.LENGTH_INDEFINITE)
                    .show()
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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode) {
            REQUEST_PERMISSION_CODE_WRITE_EXTERNAL_STORAGE -> {
                // 判断用户是否同意了请求
                if (grantResults.size == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //
                } else {
                    // 未同意的情况
                    if (ActivityCompat.shouldShowRequestPermissionRationale(
                            this,
                            Manifest.permission.CALL_PHONE
                        )
                    ) {
                        // 给用于予以权限解释, 对于已经拒绝过的情况，先提示申请理由，再进行申请
                        toast("程序需要读写权限保证正常运行")
                    } else {
                        // 用户勾选了不再提醒，引导用户进入设置界面进行开启权限
                        Snackbar.make(this.findViewById(R.id.main_layout), "需要打开权限才能使用该功能，您也可以前往设置->应用->开启权限",
                            Snackbar.LENGTH_INDEFINITE)
                            .setAction("确定") {
                                /*val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                intent.data = Uri.parse("package:$packageName")
                                startActivityForResult(intent,REQUEST_SETTINGS_CODE)*/
                            }
                            .show()
                    }
                }
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }



}