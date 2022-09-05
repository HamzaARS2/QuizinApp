package com.reddevx.quizin.ui.fragments.profile

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.reddevx.quizin.R
import com.reddevx.quizin.data.models.User
import com.reddevx.quizin.databinding.FragmentProfileBinding


class ProfileFragment : Fragment() {


    private val binding by lazy {
        FragmentProfileBinding.inflate(layoutInflater)
    }

    private val viewModel: ProfileViewModel by activityViewModels()

    private val args: ProfileFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!args.currentUserProfile) {
            binding.apply {
                profileLeaderboardCv.visibility = View.GONE
                profileInviteCv.visibility = View.GONE
            }
        }
        viewModel.userLiveData.observe(viewLifecycleOwner) {
            loadUserProfileData(it)
        }
        viewModel.postUserData(args.userData)

        binding.materialToolbar.apply {
            setNavigationIcon(R.drawable.ic_back_arrow)
            setNavigationIconTint(Color.WHITE)
            setNavigationOnClickListener {
                Navigation
                    .findNavController(requireView())
                    .popBackStack()
            }
        }
        binding.profileLeaderboardCv.setOnClickListener {
            Navigation
                .findNavController(it)
                .navigate(ProfileFragmentDirections.profileToLeaderboard())
        }

        binding.profileInviteCv.setOnClickListener {
            shareApp()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun loadUserProfileData(user: User) {
        binding.apply {
            profileNameTv.text = user.username
            profileUserLevelTv.text = "Level ${user.level}"
            profileUserExpTv.text = "Exp ${user.exp}/${user.level.times(1000)}"
            profileCircularProgressBar.apply {
                progressMax = user.level.times(1000).toFloat()
                setProgressWithAnimation(user.exp.toFloat(), 1000)
            }
            profileUserTrophiesTv.text = user.trophies.toString()
            profileUserCorrectsTv.text = user.correct.toString()
            profileUserWrongsTv.text = user.wrong.toString()
            profileUserBadgeTv.text = user.badge
            profileUserBadgeImage.setImageResource(getAppropriateImageBadge(user.badge))

            Glide.with(requireView()).load(user.avatar).placeholder(R.drawable.avatar_placeholder)
                .into(binding.profileUserImage)

        }
    }

    private fun getAppropriateImageBadge(strBadge: String): Int {
        return when (strBadge) {
            "Beginner" -> R.drawable.ic_bronze_medal
            "Intermediate" -> R.drawable.ic_silver_medal
            else -> R.drawable.ic_gold_medal
        }
    }

    private fun shareApp() {
        val intent = Intent(Intent.ACTION_SEND)
        intent.apply {
            type = "text/plain"
            putExtra(
                Intent.EXTRA_TEXT,
                " Install App from:\n https://play.google.com/store/apps/details?id=${requireContext().packageName}"
            )
        }
        startActivity(Intent.createChooser(intent, "Install App from:"))
    }


}