package com.studio.owo.bilibilidownloader.core.api.dataclass

import com.google.gson.annotations.SerializedName

data class VideoPlayUrl(
    @SerializedName("accept_description")
    val acceptDescription: List<String>,
    @SerializedName("accept_format")
    val acceptFormat: String,
    @SerializedName("accept_quality")
    val acceptQuality: List<Int>,
    @SerializedName("durl")
    val dUrl: List<DUrl>,
    val format: String,
    val from: String,
    val message: String,
    val quality: Int,
    val result: String,
    val seek_param: String,
    val seek_type: String,
    val support_formats: List<SupportFormat>,
    @SerializedName("timelength")
    val timeLength: Int,
    @SerializedName("video_codecid")
    val videoCodeCid: Int
)

data class DUrl(
    val ahead: String,
    @SerializedName("backup_url")
    val backupUrl: List<String>,
    val length: Int,
    val order: Int,
    val size: Int,
    val url: String,
    @SerializedName("vhead")
    val vHead: String
)

data class SupportFormat(
    @SerializedName("display_desc")
    val displayDesc: String,
    val format: String,
    @SerializedName("new_description")
    val newDescription: String,
    val quality: Int,
    val superscript: String
)