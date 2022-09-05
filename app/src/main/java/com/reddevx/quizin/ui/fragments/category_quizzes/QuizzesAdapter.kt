package com.reddevx.quizin.ui.fragments.category_quizzes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.reddevx.quizin.R
import com.reddevx.quizin.data.models.Category
import com.reddevx.quizin.data.models.Quiz

class QuizzesAdapter(
    private val quizzes: ArrayList<Quiz> = arrayListOf(),
    private val category: Category,
    val mListener: QuizPlayListener
) : RecyclerView.Adapter<QuizzesAdapter.QuizzesHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuizzesHolder {
        return QuizzesHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.quiz_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: QuizzesHolder, position: Int) {
        val quiz = quizzes[position]
        holder.apply {
            quizNameTv.text = quiz.name
            Glide.with(itemView).apply {
                load(category.categoryLogo).into(quizLogoImg)
                load(category.itemBg).into(quizBgImage)
            }
        }
    }

    override fun getItemCount(): Int = quizzes.size

    fun setQuizzes(quizList: ArrayList<Quiz>) {
        quizzes.addAll(quizList)
        notifyItemRangeInserted(0, itemCount)
    }

    inner class QuizzesHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val quizNameTv: TextView = itemView.findViewById(R.id.quiz_item_name)
        val quizLogoImg: ImageView = itemView.findViewById(R.id.quiz_item_logo)
        val quizBgImage: ImageView = itemView.findViewById(R.id.quiz_item_bg_image)
        private val quizPlayBtn: Button = itemView.findViewById(R.id.quiz_item_play_btn)

        init {
            quizPlayBtn.setOnClickListener {
                mListener.onQuizPlayClick(quizzes[adapterPosition])
            }
        }
    }

    interface QuizPlayListener {
        fun onQuizPlayClick(quiz: Quiz)
    }
}