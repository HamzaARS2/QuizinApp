package com.reddevx.quizin.ui.fragments.explore.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.reddevx.quizin.R
import com.reddevx.quizin.data.models.LatestQuiz

class LatestQuizzesAdapter(
    private val latestQuizzes: ArrayList<LatestQuiz> = arrayListOf(),
    val mListener: LatestQuizListener
) : RecyclerView.Adapter<LatestQuizzesAdapter.LatestQuizzesHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LatestQuizzesHolder {
        return LatestQuizzesHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.latest_quiz_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: LatestQuizzesHolder, position: Int) {
        val latestQuiz = latestQuizzes[position]
        val category = latestQuiz.category

        holder.apply {
            latestQuizNameTv.text = latestQuiz.name
            Glide.with(itemView).load(category.categoryLogo).into(latestQuizLogoImg)
            Glide.with(itemView).load(category.itemBg).into(latestQuizBgImg)

        }
    }

    override fun getItemCount(): Int = latestQuizzes.size


    fun setLatestQuizzes(latestQuizList: ArrayList<LatestQuiz>) {
        latestQuizzes.addAll(latestQuizList)
        notifyItemRangeInserted(0, itemCount)
    }


    inner class LatestQuizzesHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val latestQuizNameTv: TextView = itemView.findViewById(R.id.latest_quiz_name_tv)
        val latestQuizLogoImg: ImageView = itemView.findViewById(R.id.latest_quiz_category_logo_img)
        val latestQuizBgImg: ImageView = itemView.findViewById(R.id.latest_quiz_bg_image)
        private val latestQuizPlayBtn: Button = itemView.findViewById(R.id.latest_quiz_play_btn)

        init {
            latestQuizPlayBtn.setOnClickListener {
                mListener.onLatestQuizPlayClick(latestQuizzes[adapterPosition])
            }
        }

    }

    interface LatestQuizListener {
        fun onLatestQuizPlayClick(latestQuiz: LatestQuiz)
    }


}