package com.example.mvvm_example.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.graphics.ColorUtils
import androidx.recyclerview.widget.RecyclerView
import com.example.mvvm_example.R
import com.example.mvvm_example.model.Question
import com.example.mvvm_example.ui.base.BaseViewHolder
import com.example.mvvm_example.ui.home.HomeViewModel
import com.example.mvvm_example.utility.BaseUtility
import com.example.mvvm_example.utility.ImageProcessor
import kotlinx.android.synthetic.main.list_item_question.view.*

class QuestionsAdapter(viewModel: HomeViewModel) : RecyclerView.Adapter<BaseViewHolder>() {

    private val dataList: MutableList<Question> = mutableListOf()

    private val alphaColor = ColorUtils.setAlphaComponent(Color.BLACK, 128)

    init {
        val disposable = viewModel.getQuestionsData().subscribe { setDataList(it) }
        viewModel.autoDispose(disposable)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val customView: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_question, parent, false)
        return QuestionsViewHolder(customView)
    }

    override fun getItemCount(): Int = dataList.size

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) =
        holder.loadData(dataList[position])

    private fun setDataList(dataList: MutableList<Question>) {
        this.dataList.clear()
        this.dataList.addAll(dataList)
        notifyDataSetChanged()
    }

    inner class QuestionsViewHolder(itemView: View) : BaseViewHolder(itemView) {

        private val ivImage: ImageView = itemView.ivImage
        private val tvTitle: TextView = itemView.tvTitle
        private val tvDescription: TextView = itemView.tvDescription
        private val tvProjectLink: TextView = itemView.tvProjectLink

        init {
            //ivImage.setColorFilter(alphaColor)
            tvProjectLink.setOnClickListener {
                val position = adapterPosition
                val question = dataList[position]
                BaseUtility.navigateToUrl(it.context, question.projectUrl!!)
            }
        }

        override fun loadData(question: Question) {
            tvTitle.text = question.title
            tvDescription.text = question.description
            tvProjectLink.text = "Link : ${question.projectUrl}"

            ImageProcessor.loadThumbnailFromUrl(question.imageUrl!!, ivImage)
        }
    }
}