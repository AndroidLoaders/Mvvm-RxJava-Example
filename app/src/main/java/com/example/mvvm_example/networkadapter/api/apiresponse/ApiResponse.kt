package com.example.mvvm_example.networkadapter.api.apiresponse

import com.example.mvvm_example.model.ApiStatus

interface ApiResponse<ResponseClass : ApiStatus> {
    fun onSuccess(apiName: String, apiResponse: ResponseClass)
    fun onFailed(apiName: String, throwable: Throwable)
}