package com.example.mvvm_example.datamanager

import com.example.mvvm_example.model.LoginResponse
import com.example.mvvm_example.networkadapter.api.apirequest.ApiRequest
import com.example.mvvm_example.preferences.PreferenceRequest

interface DataManager : ApiRequest, PreferenceRequest {

    fun updateUserData(loginResponse: LoginResponse)
}