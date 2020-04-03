package com.example.mvvm_example.ui.login

import com.example.mvvm_example.base.extensions.rx.autoDispose
import com.example.mvvm_example.datamanager.DataManager
import com.example.mvvm_example.model.LoginResponse
import com.example.mvvm_example.model.ServerLoginRequest
import com.example.mvvm_example.ui.base.BaseViewModel
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject

class LoginViewModel private constructor(private val dataManager: DataManager) : BaseViewModel() {

    private val TAG: String = LoginViewModel::class.java.simpleName

    companion object {
        @Volatile
        private var instance: LoginViewModel? = null

        @Synchronized
        fun getInstance(dataManager: DataManager): LoginViewModel =
            instance ?: synchronized(this) {
                if (instance == null) instance = LoginViewModel(dataManager)
                instance!!
            }
    }

    private val loginResponse: PublishSubject<LoginResponse> = PublishSubject.create()

    fun isLoadingData(): PublishSubject<Boolean> = isLoading
    fun getLoginResponse(): PublishSubject<LoginResponse> = loginResponse

    // ServerLogin Api Call
    fun initiateServerLogin(emailId: String, password: String) {
        val serverRequest = ServerLoginRequest.getInstance()
        serverRequest.emailId = emailId
        serverRequest.password = password

        dataManager.serverLogin()
            .doOnSubscribe { isLoading.onNext(true) }
            .doOnSuccess { dataManager.updateUserData(it) }
            .subscribe({
                // TODO : Successful Api Response
                isLoading.onNext(true)
                loginResponse.onNext(it)
            }, {
                // TODO : Error in Api Response
                isLoading.onNext(false)
                loginResponse.onNext(LoginResponse())
            })
            .autoDispose(disposables)
    }
}