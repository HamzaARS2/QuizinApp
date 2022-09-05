package com.reddevx.quizin.ui.fragments.avatars

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.reddevx.quizin.R
import com.reddevx.quizin.data.models.Avatar
import de.hdodenhof.circleimageview.CircleImageView

class AvatarsAdapter(
    private val avatars: ArrayList<Avatar> = arrayListOf(),
    val mListener: AvatarClickListener
) :
    RecyclerView.Adapter<AvatarsAdapter.AvatarsHolder>() {

    private var lastSelectedPos = RecyclerView.NO_POSITION
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AvatarsHolder {
        return AvatarsHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.avatar_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: AvatarsHolder, position: Int) {
        val avatar = avatars[position]
        holder.apply {
            loadAvatar(avatar.avatarUrl)
            setImageBoarder(avatar.selected)
        }


    }

    override fun getItemCount(): Int = avatars.size

    fun setAvatars(avatarList: ArrayList<Avatar>) {
        avatars.addAll(avatarList)
        notifyItemRangeInserted(0, itemCount)
    }

    fun addAvatar(avatar: Avatar) {
        avatars.add(0, avatar)
        notifyItemInserted(0)
    }

    inner class AvatarsHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        RequestListener<Drawable> {
        val avatarImg: CircleImageView = itemView.findViewById(R.id.avatar_item_circle_image)
        private val avatarProgress: ProgressBar = itemView.findViewById(R.id.avatar_item_progress)

        init {
            itemView.setOnClickListener {
                val avatar = avatars[adapterPosition]
                setAvatarState(avatar)
                mListener.onAvatarSelected(avatar)
            }
        }

        fun loadAvatar(avatarUrl: String) {
            Glide
                .with(itemView)
                .load(avatarUrl)
                .listener(this)
                .into(avatarImg)
        }

        fun setImageBoarder(selected: Boolean) {
            if (selected)
                avatarImg.borderColor = Color.WHITE
            else
                avatarImg.borderColor = Color.TRANSPARENT
        }

        private fun setAvatarState(avatar: Avatar) {
            avatar.selected = true
            if (lastSelectedPos != RecyclerView.NO_POSITION && lastSelectedPos != adapterPosition) {
                avatars[lastSelectedPos].selected = false
                notifyItemChanged(lastSelectedPos)
            }
            notifyItemChanged(adapterPosition)
            lastSelectedPos = adapterPosition
        }

        override fun onResourceReady(
            resource: Drawable?,
            model: Any?,
            target: Target<Drawable>?,
            dataSource: DataSource?,
            isFirstResource: Boolean
        ): Boolean {
            avatarProgress.visibility = View.INVISIBLE
            avatarImg.visibility = View.VISIBLE
            return false
        }

        override fun onLoadFailed(
            e: GlideException?,
            model: Any?,
            target: Target<Drawable>?,
            isFirstResource: Boolean
        ): Boolean {
            return false
        }


    }

    interface AvatarClickListener {
        fun onAvatarSelected(avatar: Avatar)
    }


}