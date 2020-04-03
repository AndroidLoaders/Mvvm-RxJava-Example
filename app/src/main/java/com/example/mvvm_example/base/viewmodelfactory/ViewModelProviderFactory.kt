package com.example.mvvm_example.base.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner

open class ViewModelProviderFactory(private val factory: ViewModel) :
    ViewModelProvider.NewInstanceFactory() {

    companion object {
        inline fun <reified VM : ViewModel> getViewModel(
            lifecycleOwner: ViewModelStoreOwner, factory: VM
        ): VM =
            ViewModelProvider(
                lifecycleOwner, ViewModelProviderFactory(factory)
            )[VM::class.java]
    }

    override fun <VM : ViewModel> create(modelClass: Class<VM>): VM = factory as VM

    /*override fun <T : ViewModel> create(viewModelClass: Class<T>): T {
        if (viewModelClass.isAssignableFrom(LoginViewModel::class.java))
            return LoginViewModel() as T
        else if (viewModelClass.isAssignableFrom(HomeActivityViewModel::class.java))
            return HomeActivityViewModel() as T

        LogPrinter.printMessage(TAG, "Unknown ViewModel class : ${viewModelClass.name}")
        throw IllegalArgumentException("$TAG --- Unknown ViewModel class : ${viewModelClass.name}")
    }*/
}