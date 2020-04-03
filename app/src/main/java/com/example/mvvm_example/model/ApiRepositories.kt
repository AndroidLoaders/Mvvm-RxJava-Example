package com.example.mvvm_example.model

import com.example.mvvm_example.networkadapter.apiconstants.ApiConstants
import com.google.gson.annotations.SerializedName

class QuestionsRepository : ApiStatus() {

    @SerializedName(ApiConstants.Data)
    val questionsList: MutableList<Question>? = mutableListOf()
        get() = field ?: mutableListOf()
}

class OptionsRepository : ApiStatus() {

    @SerializedName(ApiConstants.Data)
    val optionsList: MutableList<Option>? = mutableListOf()
        get() = field ?: mutableListOf()
}