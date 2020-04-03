package com.example.mvvm_example.ui.splash

import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.example.mvvm_example.R
import com.example.mvvm_example.base.extensions.getTargetIntent
import com.example.mvvm_example.base.extensions.rx.autoDispose
import com.example.mvvm_example.datamanager.AppDataManager
import com.example.mvvm_example.datamanager.DataManager
import com.example.mvvm_example.networkadapter.api.apimanager.ApiManager
import com.example.mvvm_example.networkadapter.retrofit.RetrofitClient
import com.example.mvvm_example.preferences.PreferenceManager
import com.example.mvvm_example.ui.home.HomeActivity
import com.example.mvvm_example.ui.login.LoginActivity
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import java.util.concurrent.TimeUnit

class SplashActivity : AppCompatActivity() {

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window?.run {
            setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        setContentView(R.layout.activity_splash)

        Observable.timer(2000L, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { navigateToNextActivity() }
            .autoDispose(compositeDisposable)
    }

    override fun onDestroy() {
        compositeDisposable.dispose()
        compositeDisposable.clear()
        super.onDestroy()
    }

    private fun navigateToNextActivity() {
        val dataManager: DataManager = AppDataManager(
            ApiManager.getApiManager(RetrofitClient.createApiClient(application)),
            PreferenceManager.getPreferenceManager(application)
        )
        startActivity(
            getTargetIntent(
                if (dataManager.isLogin()) HomeActivity::class.java
                else LoginActivity::class.java
            )
        )
        /*else {
            val sharedView: View = findViewById(R.id.rootLayout)
            val sharedElement = Pair.create<View, String>(sharedView, sharedView.transitionName)
            val activityOptionsCompat =
                ActivityOptionsCompat.makeSceneTransitionAnimation(this, sharedElement)
            startActivity(LoginActivity.getIntent(this), activityOptionsCompat.toBundle())
        }*/
        finish()
    }
}