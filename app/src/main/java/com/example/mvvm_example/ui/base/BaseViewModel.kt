package com.example.mvvm_example.ui.base

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject

abstract class BaseViewModel : ViewModel() {

    protected val disposables: CompositeDisposable = CompositeDisposable()

    protected val isLoading: PublishSubject<Boolean> = PublishSubject.create()
    protected val isError: PublishSubject<Throwable> = PublishSubject.create()

    fun autoDispose(dataStream: Disposable) {
        if (!disposables.isDisposed) disposables.add(dataStream)
    }

    override fun onCleared() {
        disposables.clear()
        super.onCleared()
    }
}