package com.example.mvvm_example.model

import com.example.mvvm_example.networkadapter.apiconstants.ApiConstants
import com.google.gson.annotations.SerializedName

class FacebookLoginRequest private constructor() {

    companion object {
        private val facebookRequest by lazy { FacebookLoginRequest() }
        fun getInstance(): FacebookLoginRequest = facebookRequest
    }

    @SerializedName(ApiConstants.FacebookId)
    var facebookId: String? = "0"
        get() = field ?: "0"
        set(value) {
            field = value ?: "0"
        }

    @SerializedName(ApiConstants.FacebookAccessToken)
    var facebookAccessToken: String? = "0"
        get() = field ?: "0"
        set(value) {
            field = value ?: "0"
        }

    override fun equals(anyObejct: Any?): Boolean {
        if (this === anyObejct) return true

        if (anyObejct == null || javaClass != anyObejct.javaClass) return false

        val otherObject = anyObejct as FacebookLoginRequest

        if (if (facebookId != null) facebookId != otherObject.facebookId else otherObject.facebookId != null)
            return false

        return if (facebookAccessToken != null) facebookAccessToken == otherObject.facebookAccessToken
        else otherObject.facebookAccessToken == null
    }

    override fun hashCode(): Int {
        var result = if (facebookId != null) facebookId.hashCode() else 0
        result = 31 * result +
                (if (facebookAccessToken != null) facebookAccessToken.hashCode() else 0)
        return result
    }
}

class GoogleLoginRequest private constructor() {

    companion object {
        private val googleRequest by lazy { GoogleLoginRequest() }
        fun getInstance(): GoogleLoginRequest = googleRequest
    }

    @SerializedName(ApiConstants.GoogleId)
    var googleId: String? = "0"
        get() = field ?: "0"
        set(value) {
            field = value ?: "0"
        }

    @SerializedName(ApiConstants.GoogleToken)
    var googleToken: String? = "0"
        get() = field ?: "0"
        set(value) {
            field = value ?: "0"
        }

    override fun equals(anyObejct: Any?): Boolean {
        if (this === anyObejct) return true

        if (anyObejct == null || javaClass != anyObejct.javaClass) return false

        val otherObject = anyObejct as GoogleLoginRequest

        if (if (googleId != null) googleId != otherObject.googleId else otherObject.googleId != null)
            return false

        return if (googleToken != null) googleToken == otherObject.googleToken
        else otherObject.googleToken == null
    }

    override fun hashCode(): Int {
        var result = if (googleId != null) googleId.hashCode() else 0
        result = 31 * result + if (googleToken != null) googleToken.hashCode() else 0
        return result
    }
}

class ServerLoginRequest {

    companion object {
        private val serverRequest by lazy { ServerLoginRequest() }
        fun getInstance(): ServerLoginRequest = serverRequest
    }

    @SerializedName(ApiConstants.EmailId)
    var emailId: String? = ""
        get() = field ?: ""
        set(value) {
            field = value ?: ""
        }

    @SerializedName(ApiConstants.Password)
    var password: String? = "0"
        get() = field ?: "0"
        set(value) {
            field = value ?: "0"
        }

    override fun equals(anyObejct: Any?): Boolean {
        if (this === anyObejct) return true

        if (anyObejct == null || javaClass != anyObejct.javaClass) return false

        val otherObject = anyObejct as ServerLoginRequest

        if (if (emailId != null) emailId != otherObject.emailId else otherObject.emailId != null)
            return false

        return if (password != null) password == otherObject.password else otherObject.password == null
    }

    override fun hashCode(): Int {
        var result = if (emailId != null) emailId.hashCode() else 0
        result = 31 * result + if (password != null) password.hashCode() else 0
        return result
    }
}