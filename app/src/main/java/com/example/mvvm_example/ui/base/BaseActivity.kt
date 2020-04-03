package com.example.mvvm_example.ui.base

import android.content.Context
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.example.mvvm_example.datamanager.AppDataManager
import com.example.mvvm_example.datamanager.DataManager
import com.example.mvvm_example.networkadapter.api.apimanager.ApiManager
import com.example.mvvm_example.networkadapter.retrofit.RetrofitClient
import com.example.mvvm_example.preferences.PreferenceManager
import io.reactivex.disposables.CompositeDisposable

abstract class BaseActivity<VM : BaseViewModel> : AppCompatActivity() {

    protected val disposables: CompositeDisposable by lazy { CompositeDisposable() }

    protected val dataManager: DataManager by lazy {
        AppDataManager(
            ApiManager.getApiManager(RetrofitClient.createApiClient(application)),
            PreferenceManager.getPreferenceManager(application)
        )
    }

    private lateinit var viewModel: VM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        setContentView(getLayoutId())

        viewModel = getViewModelData()
    }

    override fun onDestroy() {
        if (!disposables.isDisposed) disposables.dispose()
        disposables.clear()
        super.onDestroy()
    }

    fun hideKeyboard() {
        currentFocus?.let {
            val imManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
            imManager?.run { hideSoftInputFromWindow(it.windowToken, 0) }
        }
    }

    abstract fun getLayoutId(): Int
    abstract fun getViewModelData(): VM
}