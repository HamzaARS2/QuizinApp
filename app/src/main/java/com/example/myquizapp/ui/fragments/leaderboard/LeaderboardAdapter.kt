package com.example.myquizapp.ui.fragments.leaderboard

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myquizapp.R
import com.example.myquizapp.data.models.User
import de.hdodenhof.circleimageview.CircleImageView

class LeaderboardAdapter(
    private val users: MutableList<User> = mutableListOf(),
    val mListener: LeaderboardUserClickListener
) : RecyclerView.Adapter<LeaderboardAdapter.LeaderboardHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): LeaderboardAdapter.LeaderboardHolder {
        return LeaderboardHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.leaderboard_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: LeaderboardAdapter.LeaderboardHolder, position: Int) {
        holder.loadUserData(users[position], position + 3)
    }

    override fun getItemCount(): Int = users.size

    fun setLeaderboardUsers(rankedUsers: MutableList<User>) {
        users.addAll(rankedUsers)
        notifyItemRangeInserted(0, itemCount)
    }

    inner class LeaderboardHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val userRankTv: TextView = itemView.findViewById(R.id.leaderboard_item_rank_tv)
        private val userAvatarImg: CircleImageView =
            itemView.findViewById(R.id.leaderboard_item_user_avatar_img)
        private val usernameTv: TextView = itemView.findViewById(R.id.leaderboard_item_username_tv)
        private val userTrophiesTv: TextView =
            itemView.findViewById(R.id.leaderboard_item_user_trophies)
        private val userLevelTv: TextView =
            itemView.findViewById(R.id.leaderboard_item_user_level_tv)

        init {
            itemView.setOnClickListener {
                mListener.onUserClicked(users[adapterPosition])
            }
        }

        @SuppressLint("SetTextI18n")
        fun loadUserData(user: User, position: Int) {
            userRankTv.text = (position.inc()).toString()
            usernameTv.text = user.username
            userTrophiesTv.text = user.trophies.toString()
            userLevelTv.text = user.level.toString()
            Glide.with(itemView).load(user.avatar).placeholder(R.drawable.avatar_placeholder)
                .into(userAvatarImg)
        }


    }

    interface LeaderboardUserClickListener {
        fun onUserClicked(user: User)
    }

}

