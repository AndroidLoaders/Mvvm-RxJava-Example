package com.example.mvvm_example.ui.home

import com.example.mvvm_example.base.extensions.rx.autoDispose
import com.example.mvvm_example.datamanager.DataManager
import com.example.mvvm_example.model.Question
import com.example.mvvm_example.model.QuestionsRepository
import com.example.mvvm_example.ui.base.BaseViewModel
import com.example.mvvm_example.utility.LogPrinter
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.Observer
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Function
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject

class HomeViewModel private constructor(private val dataManager: DataManager) : BaseViewModel() {

    companion object {
        @Volatile
        private var instance: HomeViewModel? = null

        @Synchronized
        fun getInstance(dataManager: DataManager): HomeViewModel =
            instance ?: synchronized(this) {
                instance ?: HomeViewModel(dataManager).also { instance = it }
            }
    }

    private val TAG: String = HomeViewModel::class.java.simpleName

    private val questionsList: BehaviorSubject<MutableList<Question>> = BehaviorSubject.create()

    fun isLoadingData(): PublishSubject<Boolean> = isLoading
    fun getQuestionsData(): BehaviorSubject<MutableList<Question>> = questionsList

    private fun getQuestionListObserver(): Observable<QuestionsRepository> {
        val questionObservable = dataManager.getQuestionsList().toObservable()
        val subscriber = questionObservable.doOnSubscribe { isLoading.onNext(true) }
            //.doAfterTerminate { isLoading.onNext(false) }
            .subscribeWith(object : Observer<QuestionsRepository> {
                override fun onSubscribe(disposable: Disposable) = autoDispose(disposable)

                override fun onError(throwable: Throwable) = isError.onNext(throwable)

                override fun onComplete() = isLoading.onNext(false)

                override fun onNext(questionRepo: QuestionsRepository) {
                    questionRepo.questionsList?.let { questionsList.onNext(it) }
                }
            })
        return questionObservable
    }

    private fun getOptionsForEachQuestion(question: Question): Single<Question> {
        val optionsObservable = dataManager.getOptionsList()
        return optionsObservable.map {
            question.optionsList = it.optionsList!!
            LogPrinter.printMessage(TAG, "Map -- ${question.title}")
            question
        }
    }

    internal fun getQuestionsListWithOptions() {
        /**
         * getQuestionListObserver().toObservable()
         *    .flatMap { questionRepo -> Observable.just(questionRepo.questionsList) }
         *    .flatMap { questionsList -> Observable.fromIterable(questionsList) }
         *    .flatMap { question ->
         *        LogPrinter.printMessage(TAG, "flatMap -- ${question.title}")
         *        getOptionsForEachQuestion(question).toObservable()
         *    }
         *    .subscribe().autoDispose(disposables)
         * */
        getQuestionListObserver()
            .flatMap(object :
                Function<QuestionsRepository, ObservableSource<MutableList<Question>>> {
                override fun apply(questionRepo: QuestionsRepository): ObservableSource<MutableList<Question>> {
                    return Observable.just(questionRepo.questionsList)
                }
            })
            .flatMap(object : Function<MutableList<Question>, ObservableSource<Question>> {
                override fun apply(questionsList: MutableList<Question>): ObservableSource<Question> {
                    return Observable.fromIterable(questionsList)
                }
            })
            .flatMap(object : Function<Question, ObservableSource<Question>> {
                override fun apply(question: Question): ObservableSource<Question> {
                    LogPrinter.printMessage(TAG, "flatMap -- ${question.title}")
                    return getOptionsForEachQuestion(question).toObservable()
                }
            })
            .subscribe({}, {
                isLoadingData().onNext(false)
                LogPrinter.printMessage(TAG, "onError ---> ${it.message ?: ""}")
            }).autoDispose(disposables)
    }
}