package com.example.mvvm_example.networkadapter.apiconstants

import com.example.mvvm_example.BuildConfig

object ApiProvider {

    private const val FACEBOOK_LOGIN = "588d15d3100000ae072d2944"
    const val ApiFacebookLogin = BuildConfig.BASE_URL + FACEBOOK_LOGIN

    private const val GOOGLE_LOGIN = "588d14f4100000a9072d2943"
    const val ApiGoogleLogin = BuildConfig.BASE_URL + GOOGLE_LOGIN

    private const val SERVER_LOGIN = "588d15f5100000a8072d2945"
    const val ApiServerLogin = BuildConfig.BASE_URL + SERVER_LOGIN

    private const val GET_QUESTIONS_LIST = "5926c34212000035026871cd"
    const val ApiGetQuestionsList = BuildConfig.BASE_URL + GET_QUESTIONS_LIST
}