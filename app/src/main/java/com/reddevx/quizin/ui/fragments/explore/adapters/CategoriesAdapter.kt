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
import com.reddevx.quizin.data.models.Category

class CategoriesAdapter(
    private val categories: ArrayList<Category> = arrayListOf(),
    val mListener: ExploreCategoryListener
) : RecyclerView.Adapter<CategoriesAdapter.CategoriesHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesHolder {
        return CategoriesHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.category_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: CategoriesHolder, position: Int) {
        val category = categories[position]
        holder.apply {
            categoryNameTv.text = category.name
            Glide.with(itemView).load(category.categoryLogo).into(categoryLogoImg)
            Glide.with(itemView).load(category.itemBg).into(categoryBgImg)
        }
    }

    override fun getItemCount(): Int = categories.size

    fun setCategories(categoryList: ArrayList<Category>) {
        categories.addAll(categoryList)
        notifyItemRangeInserted(0, itemCount)
    }

    inner class CategoriesHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val categoryNameTv: TextView = itemView.findViewById(R.id.quiz_item_name)
        val categoryLogoImg: ImageView = itemView.findViewById(R.id.category_item_logo)
        val categoryBgImg: ImageView = itemView.findViewById(R.id.quiz_item_bg_image)
        private val categoryExploreBtn: Button = itemView.findViewById(R.id.category_explore_btn)

        init {
            categoryExploreBtn.setOnClickListener {
                mListener.onExploreCategoryClick(categories[adapterPosition])
            }
        }

    }


    interface ExploreCategoryListener {
        fun onExploreCategoryClick(category: Category)
    }
}