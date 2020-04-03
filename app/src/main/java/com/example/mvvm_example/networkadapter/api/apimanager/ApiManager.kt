package com.example.mvvm_example.networkadapter.api.apimanager

import com.example.mvvm_example.base.extensions.rx.subscribeAndObserve
import com.example.mvvm_example.base.extensions.rx.subscribeAndObserveWithDelaySubscription
import com.example.mvvm_example.model.LoginResponse
import com.example.mvvm_example.model.OptionsRepository
import com.example.mvvm_example.model.QuestionsRepository
import com.example.mvvm_example.networkadapter.api.apirequest.ApiInterface
import com.example.mvvm_example.networkadapter.api.apirequest.ApiRequest
import io.reactivex.Single

class ApiManager private constructor(private val apiClient: ApiInterface) : ApiRequest {

    companion object {
        fun getApiManager(apiClient: ApiInterface): ApiManager = ApiManager(apiClient)
    }

    override fun googleLogin(): Single<LoginResponse> =
        apiClient.googleLogin().subscribeAndObserveWithDelaySubscription()

    override fun facebookLogin(): Single<LoginResponse> =
        apiClient.facebookLogin().subscribeAndObserveWithDelaySubscription()

    override fun serverLogin(): Single<LoginResponse> =
        apiClient.serverLogin().subscribeAndObserveWithDelaySubscription()

    override fun getQuestionsList(): Single<QuestionsRepository> =
        apiClient.getQuestionsList().subscribeAndObserveWithDelaySubscription()

    override fun getOptionsList(): Single<OptionsRepository> =
        apiClient.getOptionsList().subscribeAndObserve()
}