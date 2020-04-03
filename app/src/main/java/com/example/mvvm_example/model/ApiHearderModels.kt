package com.example.mvvm_example.model

import com.example.mvvm_example.BuildConfig
import com.example.mvvm_example.networkadapter.apiconstants.ApiConstants
import com.google.gson.annotations.SerializedName

class ApiHeader {
}

class PublicApiHeader {

    @SerializedName(ApiConstants.ApiKey)
    val apiKey: String? = BuildConfig.API_KEY
        get() = field ?: BuildConfig.API_KEY
}

class ProtectedApiHeader {

    @SerializedName(ApiConstants.ApiKey)
    val apiKey: String? = BuildConfig.API_KEY
        get() = field ?: BuildConfig.API_KEY

    @SerializedName(ApiConstants.UserId)
    var userId: Long? = 0L
        get() = field ?: 0L

    @SerializedName(ApiConstants.AccessToken)
    val accessToken: String? = ""
        get() = field ?: ""
}
