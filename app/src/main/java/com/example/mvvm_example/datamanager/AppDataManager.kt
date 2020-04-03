package com.example.mvvm_example.datamanager

import com.example.mvvm_example.model.LoginResponse
import com.example.mvvm_example.model.OptionsRepository
import com.example.mvvm_example.model.QuestionsRepository
import com.example.mvvm_example.networkadapter.api.apirequest.ApiRequest
import com.example.mvvm_example.preferences.PreferenceRequest
import io.reactivex.Single

class AppDataManager(
    private val apiRequest: ApiRequest, private val preferences: PreferenceRequest
) : DataManager {

    // ApiRequest Callbacks
    override fun googleLogin(): Single<LoginResponse> = apiRequest.googleLogin()
    override fun facebookLogin(): Single<LoginResponse> = apiRequest.facebookLogin()
    override fun serverLogin(): Single<LoginResponse> = apiRequest.serverLogin()
    override fun getQuestionsList(): Single<QuestionsRepository> = apiRequest.getQuestionsList()
    override fun getOptionsList(): Single<OptionsRepository> = apiRequest.getOptionsList()

    // PreferenceRequest Callbacks
    override fun isLogin(): Boolean = preferences.isLogin()

    override fun updateUserData(loginResponse: LoginResponse) {
        setAccessToken(loginResponse.accessToken!!)
        setUserId(loginResponse.userId!!)
        setUserName(loginResponse.userName!!)
        setEmailId(loginResponse.emailId!!)
    }

    override fun getUserId(): String = preferences.getUserId()

    override fun setUserId(userId: String) = preferences.setUserId(userId)

    override fun getUserName(): String = preferences.getUserName()

    override fun setUserName(userName: String) = preferences.setUserName(userName)

    override fun getEmailId(): String = preferences.getEmailId()

    override fun setEmailId(emailId: String) = preferences.setEmailId(emailId)

    override fun getAccessToken(): String = preferences.getAccessToken()

    override fun setAccessToken(accessToken: String) = preferences.setAccessToken(accessToken)
}