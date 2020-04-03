package com.example.mvvm_example.networkadapter.retrofit

import android.app.Application
import com.example.mvvm_example.BuildConfig
import com.example.mvvm_example.networkadapter.api.apirequest.ApiInterface
import com.google.gson.GsonBuilder
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

object RetrofitClient {

    private val TAG: String = RetrofitClient::class.java.simpleName

    private const val REQUEST_TIMEOUT = 15L

    private var apiInterface: ApiInterface? = null
    private var httpClient: OkHttpClient? = null

    fun createApiClient(application: Application): ApiInterface = initClient(application)

    /*val apiClient: ApiInterface
        get() = initClient()*/

    private fun initClient(application: Application): ApiInterface {
        if (httpClient == null) setupOkHttp(application)

        if (apiInterface == null) {
            apiInterface = Retrofit.Builder().baseUrl(BuildConfig.BASE_URL)
                .client(httpClient!!)
                .addConverterFactory(
                    GsonConverterFactory.create(GsonBuilder().setLenient().create())
                )
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createAsync())
                .build()
                .create(ApiInterface::class.java)
        }

        return apiInterface!!
    }

    private fun setupOkHttp(application: Application) {
        val cacheSize = 10 * 1024 * 1024 // 10 MiB
        val cacheDir = File(application.cacheDir, "HttpCache")
        val cache = Cache(cacheDir, cacheSize.toLong())
        // TODO : Replace sample_certificate.pem with your server public
        //  certificate in raw resource and uncomment .setupNetworkSecurity(context)
        val httpBuilder = OkHttpClient().newBuilder()
            .readTimeout(20, TimeUnit.SECONDS)
            .connectTimeout(20, TimeUnit.SECONDS)
            //.writeTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
            //.setupNetworkSecurity(context)
            .cache(cache)

        if (BuildConfig.DEBUG) {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

            httpBuilder.addInterceptor(loggingInterceptor)
                .addInterceptor { chain ->
                    val original = chain.request()
                    val request = original.newBuilder()
                        /*.addHeader("Accept", "application/json")
                        .addHeader("Request-Type", "Android")
                        .addHeader("Content-Type", "application/json")
                        .addHeader("User-Agent", "Android")*/
                        .build()
                    chain.proceed(request)
                }
        }
        httpClient = httpBuilder.build()
    }

    private fun initOkHttp() {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        val httpBuilder = OkHttpClient().newBuilder()
            .connectTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
            .addInterceptor(interceptor).addInterceptor { chain ->
                val original = chain.request()
                val request = original.newBuilder()
                    /*.addHeader("Accept", "application/json")
                    .addHeader("Request-Type", "Android")
                    .addHeader("Content-Type", "application/json")
                    .addHeader("User-Agent", "Android")*/
                    .build()
                chain.proceed(request)
            }

        httpClient = httpBuilder.build()
    }
}