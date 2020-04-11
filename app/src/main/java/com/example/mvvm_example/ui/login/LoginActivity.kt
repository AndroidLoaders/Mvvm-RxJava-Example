package com.example.mvvm_example.ui.login

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import com.example.mvvm_example.R
import com.example.mvvm_example.base.extensions.getTargetIntent
import com.example.mvvm_example.base.extensions.getViewModel
import com.example.mvvm_example.base.extensions.rx.autoDispose
import com.example.mvvm_example.base.extensions.rx.onTextChanges
import com.example.mvvm_example.base.extensions.rx.throttleClicks
import com.example.mvvm_example.ui.base.BaseActivity
import com.example.mvvm_example.ui.home.HomeActivity
import com.example.mvvm_example.utility.BaseUtility
import com.example.mvvm_example.utility.LogPrinter
import com.google.android.material.snackbar.Snackbar
import com.mikhaellopez.rxanimation.RxAnimation
import com.mikhaellopez.rxanimation.fadeOut
import com.mikhaellopez.rxanimation.scale
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_login.*
import java.util.*
import java.util.concurrent.TimeUnit

class LoginActivity : BaseActivity<LoginViewModel>() {

    private val TAG: String = LoginActivity::class.java.simpleName

    companion object {
        private const val ANIMATION_DURATION_600: Long = 600L
    }

    private lateinit var viewModel: LoginViewModel

    /**
     *  1). To understand this demo application, you can refer this demo :
     *      https://github.com/MindorksOpenSource/android-mvvm-architecture and
     *      https://github.com/MindorksOpenSource
     *
     *  2). For various types of TextInpuLayout example go to :
     *      https://github.com/journaldev/journaldev/tree/master/Android/AndroidMaterialTextFields
     *
     *  3). For all kind of android examples go to :
     *      https://github.com/journaldev/journaldev/tree/master/Android
     *
     *  4). Go and watch MotionLayout examples :
     *      https://github.com/Hariofspades/MotionLayoutExperiments
     * */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        RxAnimation.sequentially(
            cardView.scale(1f, ANIMATION_DURATION_600),
            btnLogin.scale(1f, ANIMATION_DURATION_600)
        ).doOnComplete {
            initViews()
            subscribeDataStream()
        }.subscribe().autoDispose(disposables)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        LogPrinter.printMessage(TAG, "onNewIntent --> called")
    }


    override fun getLayoutId(): Int = R.layout.activity_login

    override fun getViewModelData(): LoginViewModel {
        viewModel = getViewModel { LoginViewModel.getInstance(dataManager) }
        //viewModel = ViewModelProviderFactory.getViewModel(this, LoginViewModel())
        return viewModel
    }

    private fun subscribeDataStream() {
        viewModel.isLoadingData().subscribe { isLoading(it) }.autoDispose(disposables)
        viewModel.getLoginResponse()
            .subscribe {
                if (it.status!!.toLowerCase(Locale.ENGLISH) == "success") navigateToNextActivity()
                else Snackbar.make(rootLayout, R.string.something_went_wrong, Snackbar.LENGTH_SHORT)
                    .show()
            }
            .autoDispose(disposables)
    }

    private fun initViews() {
        tietEmailId.onTextChanges {
            if (it.isNotEmpty() && tilEmailId.isErrorEnabled) tilEmailId.error = ""
        }.autoDispose(disposables)

        tietPassword.onTextChanges {
            if (it.isNotEmpty() && tilPassword.isErrorEnabled) tilPassword.error = ""
        }.autoDispose(disposables)

        btnLogin.throttleClicks().subscribe {
            if (validateTextFields())
                viewModel.initiateServerLogin(
                    tietEmailId.text.toString(), tietPassword.text.toString()
                )
        }.autoDispose(disposables)

        /** Observable.interval() will repeat task after every provided Period */
        Observable.interval(5000L, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
            .subscribe {
                val random = Random()
                val textColor = Color.argb(
                    255, random.nextInt(256), random.nextInt(256),
                    random.nextInt(256)
                )
                tvTitle.fadeOut(800L, AccelerateDecelerateInterpolator(), reverse = true)
                    .doOnSubscribe {
                        Observable.timer(
                            700L, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread()
                        ).subscribe { tvTitle.setTextColor(textColor) }
                            .autoDispose(disposables)
                    }.subscribe()
                    .autoDispose(disposables)
            }
            .autoDispose(disposables)
    }

    private fun validateTextFields(): Boolean =
        if (tietEmailId.text.toString().isEmpty()) {
            tilEmailId.error = getString(R.string.validate_empty_email_id)
            if (tilPassword.isErrorEnabled) tilPassword.error = ""
            //tilEmailId.isErrorEnabled = true
            //if (tilPassword.isErrorEnabled) tilPassword.isErrorEnabled = false
            false
        } else if (!BaseUtility.validateEmailFormat(tietEmailId.text.toString())) {
            tilEmailId.error = getString(R.string.validate_email_id)
            if (tilPassword.isErrorEnabled) tilPassword.error = ""
            //tilEmailId.isErrorEnabled = true
            //if (tilPassword.isErrorEnabled) tilPassword.isErrorEnabled = false
            false
        } else if (tietPassword.text.toString().isEmpty()) {
            tilPassword.error = getString(R.string.validate_password)
            if (tilEmailId.isErrorEnabled) tilEmailId.error = ""
            //tilPassword.isErrorEnabled = true
            //if (tilEmailId.isErrorEnabled) tilEmailId.isErrorEnabled = false
            false
        } else if (tietPassword.text.toString().length < 6) {
            tilPassword.error = getString(R.string.validate_password_length)
            if (tilEmailId.isErrorEnabled) tilEmailId.error = ""
            //tilPassword.isErrorEnabled = true
            //if (tilEmailId.isErrorEnabled) tilEmailId.isErrorEnabled = false
            false
        } else {
            /*tilEmailId.isErrorEnabled = false
            tilPassword.isErrorEnabled = false*/
            tilEmailId.error = ""
            tilPassword.error = ""
            true
        }

    private fun isLoading(isLoading: Boolean) {
        if (isLoading) {
            btnLogin.scale(0f, ANIMATION_DURATION_600).doOnComplete {
                btnLogin.visibility = View.GONE
                progressBar.visibility = View.VISIBLE
            }.subscribe().autoDispose(disposables)
        } else {
            progressBar.visibility = View.GONE
            btnLogin.visibility = View.VISIBLE
            btnLogin.scale(1f, ANIMATION_DURATION_600).subscribe()
                .autoDispose(disposables)
        }
    }

    private fun navigateToNextActivity() {
        startActivity(getTargetIntent(HomeActivity::class.java))
        //finish()
    }
}
