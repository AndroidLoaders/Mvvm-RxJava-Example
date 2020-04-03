package com.example.mvvm_example.model

import com.example.mvvm_example.networkadapter.apiconstants.ApiConstants
import com.google.gson.annotations.SerializedName

abstract class ApiStatus {

    @SerializedName(ApiConstants.StatusCode)
    var status: String? = "0"
        get() = field ?: "0"
        set(value) {
            field = value ?: "0"
        }

    @SerializedName(ApiConstants.Message)
    var message: String? = ""
        get() = field ?: ""
        set(value) {
            field = value ?: "0"
        }
}

class LoginResponse : ApiStatus() {

    @SerializedName(ApiConstants.AccessToken)
    val accessToken: String? = ""
        get() = field ?: ""

    @SerializedName(ApiConstants.FacebookProfilePic)
    val facebookProfilePic: String? = ""
        get() = field ?: ""

    @SerializedName(ApiConstants.GoogleProfilePic)
    val googleProfilePic: String? = ""
        get() = field ?: ""

    @SerializedName(ApiConstants.ServerProfilePic)
    val serverProfilePic: String? = ""
        get() = field ?: ""

    @SerializedName(ApiConstants.EmailId)
    val emailId: String? = ""
        get() = field ?: ""

    @SerializedName(ApiConstants.UserId)
    val userId: String? = ""
        get() = field ?: ""

    @SerializedName(ApiConstants.UserName)
    val userName: String? = ""
        get() = field ?: ""
}

class Question {

    val title: String? = ""
        get() = field ?: ""

    val description: String? = ""
        get() = field ?: ""

    val author: String? = ""
        get() = field ?: ""

    @SerializedName(ApiConstants.ImageUrl)
    val imageUrl: String? = ""
        get() = field ?: ""

    @SerializedName(ApiConstants.ProjectUrl)
    val projectUrl: String? = ""
        get() = field ?: ""

    var optionsList: MutableList<Option> = mutableListOf()
        set(value) {
            field.clear()
            field.addAll(value)
        }

    /*override fun equals(anyObject: Any?): Boolean {
        if (this === anyObject) return true

        if (anyObject !is Questions) return false

        val repo: Questions = anyObject as Questions

        if (projectUrl != repo.projectUrl) {
            return false
        }
        if (imageUrl != repo.imageUrl) {
            return false
        }
        return if (title != repo.title) {
            false
        } else description == repo.description
    }

    override fun hashCode(): Int {
        var result = projectUrl.hashCode()
        result = 31 * result + imageUrl.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + description.hashCode()
        return result
    }*/
}

class Option {

    val optionId: Int? = -1
        get() = field ?: -1

    @SerializedName(ApiConstants.Title)
    val optionName: String? = ""
        get() = field ?: ""

    @SerializedName(ApiConstants.Description)
    val description: String? = ""
        get() = field ?: ""
}