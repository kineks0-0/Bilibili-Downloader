package com.studio.owo.bilibilidownloader.core.api.dataclass

import com.google.gson.annotations.SerializedName

data class VideoInfo(
    val aid: Int,
    val bvid: String,
    val cid: Int,
    val copyright: Int,
    @SerializedName("ctime")
    val createTime: Int,
    val desc: String,
    val dimension: Dimension,
    val duration: Int,
    val dynamic: String,
    val mission_id: Int,
    val no_cache: Boolean,
    val owner: Owner,
    val pages: List<Page>,
    val pic: String,
    @SerializedName("pubdate")
    val publicDate: Int,
    val rights: Rights,
    val staff: List<Staff>,
    val stat: Stat,
    val state: Int,
    val subtitle: Subtitle,
    val tid: Int,
    val title: String,
    @SerializedName("tname")
    val tagName: String ,
    val user_garb: UserGarb,
    val videos: Int
)

/*data class Data(
)*/

data class Dimension(
    val height: Int,
    val rotate: Int,
    val width: Int
)

data class Owner(
    val face: String,
    val mid: Int,
    val name: String
)

data class Page(
    val cid: Int,
    val dimension: DimensionX,
    val duration: Int,
    val from: String,
    val page: Int,
    val part: String,
    val vid: String,
    @SerializedName("weblink")
    val webLink: String
)

data class Rights(
    @SerializedName("autoplay")
    val autoPlay: Int,
    val bp: Int,
    val clean_mode: Int,
    val download: Int,
    val elec: Int,
    val hd5: Int,
    val is_cooperation: Int,
    val is_stein_gate: Int,
    val movie: Int,
    val no_background: Int,
    val no_reprint: Int,
    val pay: Int,
    val ugc_pay: Int,
    val ugc_pay_preview: Int
)

data class Staff(
    val face: String,
    val follower: Int,
    val label_style: Int,
    val mid: Int,
    val name: String,
    val official: Official,
    val title: String,
    val vip: Vip
)

data class Stat(
    val aid: Int,
    val argue_msg: String,
    val coin: Int,
    val danmaku: Int,
    val dislike: Int,
    val evaluation: String,
    val favorite: Int,
    val his_rank: Int,
    val like: Int,
    val now_rank: Int,
    val reply: Int,
    val share: Int,
    val view: Int
)

data class Subtitle(
    val allow_submit: Boolean,
    val list: List<Any>
)

data class UserGarb(
    val url_image_ani_cut: String
)

data class DimensionX(
    val height: Int,
    val rotate: Int,
    val width: Int
)

data class Official(
    val desc: String,
    val role: Int,
    val title: String,
    val type: Int
)

data class Vip(
    val status: Int,
    val theme_type: Int,
    val type: Int,
    val vip_pay_type: Int
)
/*
data class VideoInfo(

    @SerializedName("bvid")
    val bvid: String ,
    @SerializedName("aid")
    val aid: Int ,
    //@SerializedName("videos")
    val videos: Int ,
    //@SerializedName("tid")
    val tid: Int ,
    @SerializedName("tname")
    val tagName: String ,
    val copyright: Int ,

    @SerializedName("pic")
    val picture: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("pubdate")
    val publicDate: Int,

)*/
