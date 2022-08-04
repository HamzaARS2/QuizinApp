package com.example.myquizapp.ui.fragments.leaderboard

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.myquizapp.R
import com.example.myquizapp.data.models.User
import com.example.myquizapp.databinding.LeaderboardFragmentBinding
import com.example.myquizapp.listeners.RankedUsersListener

class LeaderboardFragment : Fragment(), RankedUsersListener, View.OnClickListener,
    LeaderboardAdapter.LeaderboardUserClickListener {

    private val viewModel: LeaderboardViewModel by activityViewModels()

    private lateinit var leaderboardAdapter: LeaderboardAdapter
    private lateinit var topThreeUsers: Array<User>
    private val binding by lazy {
        LeaderboardFragmentBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        leaderboardAdapter = LeaderboardAdapter(mListener = this)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getRankedUsers(this)
        binding.leaderboardToolbar.setNavigationOnClickListener {
            Navigation
                .findNavController(requireView())
                .popBackStack()
        }
        binding.leaderboardRv.apply {
            adapter = leaderboardAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            hasFixedSize()
        }

        binding.apply {
            leaderboardTop1Cv.setOnClickListener(this@LeaderboardFragment)
            leaderboardTop2Cv.setOnClickListener(this@LeaderboardFragment)
            leaderboardTop3Cv.setOnClickListener(this@LeaderboardFragment)

        }
    }

    override fun onRankedUsersReceivedSuccessfully(users: ArrayList<User>) {
        when {
            users.size  == 1 -> {
                loadTopThreePlayersInfo(arrayOf(users[0], User(),User()))
                changeVisibility()
            }
            users.size < 3 -> {
                loadTopThreePlayersInfo(arrayOf(users[0], users[1], User()))
                changeVisibility()
            }
            else -> {
                loadTopThreePlayersInfo(arrayOf(users[0], users[1], users[2]))
                leaderboardAdapter.setLeaderboardUsers(users.subList(3, users.size))
                changeVisibility()
            }
        }

    }

    override fun onReceivingRankedUsersFailed(e: Exception) {
        Toast.makeText(requireContext(), "Error : ${e.message}", Toast.LENGTH_SHORT).show()
    }

    private fun loadTopThreePlayersInfo(users: Array<User>) {
        topThreeUsers = users

        setTopOneInfo(users[0])
        setTopTwoInfo(users[1])
        setTopThreeInfo(users[2])
    }

    @SuppressLint("SetTextI18n")
    private fun setTopOneInfo(user: User) {
        binding.apply {
            leaderboardTop1UsernameTv.text = user.username
            leaderboardTop1UserTrophiesTv.text = user.trophies.toString()
            leaderboardTop1UserLevelTv.text = "Level ${user.level}"
            Glide.with(requireView()).load(user.avatar).placeholder(R.drawable.avatar_placeholder)
                .into(binding.leaderboardTop1UserAvatarImg)
            leaderboardTop1BadgeImg.setImageResource(getUserBadge(user.badge))
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setTopTwoInfo(user: User) {
        binding.apply {
            leaderboardTop2UsernameTv.text = user.username
            leaderboardTop2UserTrophiesTv.text = user.trophies.toString()
            leaderboardTop2UserLevelTv.text = "Level ${user.level}"
            Glide.with(requireView()).load(user.avatar).placeholder(R.drawable.avatar_placeholder)
                .into(binding.leaderboardTop2UserAvatarImg)
            leaderboardTop2BadgeImg.setImageResource(getUserBadge(user.badge))

        }
    }

    @SuppressLint("SetTextI18n")
    private fun setTopThreeInfo(user: User) {
        binding.apply {
            leaderboardTop3UsernameTv.text = user.username
            leaderboardTop3UserTrophiesTv.text = user.trophies.toString()
            leaderboardTop3UserLevelTv.text = "Level ${user.level}"
            Glide.with(requireView()).load(user.avatar).placeholder(R.drawable.avatar_placeholder)
                .into(binding.leaderboardTop3UserAvatarImg)
            leaderboardTop3BadgeImg.setImageResource(getUserBadge(user.badge))


        }
    }

    private fun changeVisibility() {
        binding.apply {
            leaderboardTop1Cv.visibility = View.VISIBLE
            leaderboardTop2Cv.visibility = View.VISIBLE
            leaderboardTop3Cv.visibility = View.VISIBLE
            leaderboardRv.visibility = View.VISIBLE

            leaderboardRvProgress.visibility = View.GONE
            leaderboardTopProgress.visibility = View.GONE

        }
    }

    private fun getUserBadge(badge: String): Int {
        return when (badge) {
            "Beginner" -> R.drawable.ic_bronze_medal
            "Intermediate" -> R.drawable.ic_silver_medal
            else -> R.drawable.ic_gold_medal
        }
    }


    override fun onClick(view: View?) {
        var user = User()
        when (view?.id) {
            R.id.leaderboard_top1_cv -> {
                user = topThreeUsers[0]
            }
            R.id.leaderboard_top2_cv -> {
                user = topThreeUsers[1]
            }
            R.id.leaderboard_top3_cv -> {
                user = topThreeUsers[2]
            }
        }
        Navigation
            .findNavController(requireView())
            .navigate(LeaderboardFragmentDirections.leaderboardToProfile(user, false))
    }

    override fun onUserClicked(user: User) {
        Navigation
            .findNavController(requireView())
            .navigate(LeaderboardFragmentDirections.leaderboardToProfile(user, false))
    }


}