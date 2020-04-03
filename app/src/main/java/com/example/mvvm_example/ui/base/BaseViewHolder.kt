package com.example.mvvm_example.ui.base

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.mvvm_example.model.Question

abstract class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    abstract fun loadData(question: Question)
}