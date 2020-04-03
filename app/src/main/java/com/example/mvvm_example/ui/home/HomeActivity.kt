package com.example.mvvm_example.ui.home

import android.graphics.drawable.Animatable
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.example.mvvm_example.R
import com.example.mvvm_example.adapter.QuestionsAdapter
import com.example.mvvm_example.base.extensions.getViewModel
import com.example.mvvm_example.base.extensions.rx.autoDispose
import com.example.mvvm_example.ui.base.BaseActivity
import com.example.mvvm_example.utility.LogPrinter
import kotlinx.android.synthetic.main.activity_main.*

class HomeActivity : BaseActivity<HomeViewModel>() {

    private val TAG: String = HomeActivity::class.java.simpleName

    private val viewModel by lazy { getViewModel { HomeViewModel.getInstance(dataManager) } }
    private val adapter by lazy { QuestionsAdapter(viewModel) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        rvList.adapter = adapter

        subscribeToDataStream()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getQuestionsListWithOptions()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.menu_animate_icons, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        item.icon?.run {
            if (this is Animatable) this.start()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun getLayoutId(): Int = R.layout.activity_main

    private fun subscribeToDataStream() {
        viewModel.isLoadingData().subscribe {
            progressBar.visibility = if (it) View.VISIBLE else View.GONE
        }.autoDispose(disposables)

        /** This is multiple subscription to Data i.e. getQuestionsData() is also
         *  subscribed from QuestionsAdapter.kt class in init{} block means that
         *  response of change in data will be received Here as well as in QuestionsAdapter
         *  class too
         * */
        viewModel.getQuestionsData().subscribe {
            LogPrinter.printMessage(TAG, it.size.toString())
        }.autoDispose(disposables)
    }

    override fun getViewModelData(): HomeViewModel = viewModel
}
