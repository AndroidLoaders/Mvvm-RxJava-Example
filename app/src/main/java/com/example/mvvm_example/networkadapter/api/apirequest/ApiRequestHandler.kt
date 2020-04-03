package com.example.mvvm_example.networkadapter.api.apirequest

import com.example.mvvm_example.BuildConfig
import com.example.mvvm_example.model.LoginResponse
import com.example.mvvm_example.model.OptionsRepository
import com.example.mvvm_example.model.QuestionsRepository
import com.example.mvvm_example.networkadapter.apiconstants.ApiConstants
import com.example.mvvm_example.networkadapter.apiconstants.ApiProvider
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.HeaderMap

interface ApiInterface {

    @GET(ApiProvider.ApiGoogleLogin)
    fun googleLogin(@HeaderMap headerParams: Map<String, String> = getHeaders()):
            Single<LoginResponse>

    @GET(ApiProvider.ApiFacebookLogin)
    fun facebookLogin(@HeaderMap headerParams: Map<String, String> = getHeaders()):
            Single<LoginResponse>

    @GET(ApiProvider.ApiServerLogin)
    fun serverLogin(@HeaderMap headerParams: Map<String, String> = getHeaders()):
            Single<LoginResponse>

    @GET(ApiProvider.ApiGetQuestionsList)
    fun getQuestionsList(): Single<QuestionsRepository>

    @GET(ApiProvider.ApiGetQuestionsList)
    fun getOptionsList(): Single<OptionsRepository>

    private fun getHeaders(): Map<String, String> {
        val headersMap: HashMap<String, String> = HashMap()
        headersMap[ApiConstants.AccessToken] = ""
        headersMap[ApiConstants.ApiKey] = BuildConfig.API_KEY
        headersMap[ApiConstants.UserName] = ""
        return headersMap
    }
}

interface ApiRequest {
    // Login Services
    fun googleLogin(/*@Body googleLoginRequest: GoogleLoginRequest*/): Single<LoginResponse>
    fun facebookLogin(/*@Body facebookLoginRequest: FacebookLoginRequest*/): Single<LoginResponse>
    fun serverLogin(/*@Body serverLoginRequest: ServerLoginRequest*/): Single<LoginResponse>

    // Get Questions List
    fun getQuestionsList(): Single<QuestionsRepository>

    // Get Options For Each Question
    fun getOptionsList(): Single<OptionsRepository>
}