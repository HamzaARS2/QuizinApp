package com.reddevx.quizin.ui.fragments.quiz_result

import android.animation.Animator
import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.airbnb.lottie.LottieAnimationView
import com.reddevx.quizin.R
import com.reddevx.quizin.data.models.User
import com.reddevx.quizin.data.models.UserQuizInfo
import com.reddevx.quizin.databinding.QuizResultFragmentBinding
import com.reddevx.quizin.utils.BAD_RESULT
import com.reddevx.quizin.utils.QuizOverAll

class QuizResultFragment : Fragment(), Animator.AnimatorListener, View.OnClickListener {


    private val binding by lazy {
        QuizResultFragmentBinding.inflate(layoutInflater)
    }

    private val viewModel: QuizResultViewModel by activityViewModels()
    private val args: QuizResultFragmentArgs by navArgs()

    private lateinit var user: User
    private lateinit var userQuizInfo: UserQuizInfo
    private lateinit var overAll: QuizOverAll.OverAllResultInfo
    private lateinit var result: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        user = args.userData
        userQuizInfo = args.userQuizData
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        overAll = viewModel.getQuizOverAll(userQuizInfo)
        result = viewModel.getOverAllResult(overAll.value.toFloat())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadUserQuizInfo()
        setTitle(result)
        setAnimation(binding.quizResultAnimation, result)
        binding.quizResultAnimation.addAnimatorListener(this)
        binding.quizResultDoneBtn.setOnClickListener(this)

        checkUserLevelUp(userQuizInfo.levelUp)

    }

    @SuppressLint("SetTextI18n")
    private fun loadUserQuizInfo() {

        binding.apply {
            quizResultTrophiesTv.text = userQuizInfo.trophies.toString()
            quizResultCorrectAnswersTv.text = userQuizInfo.corrects.toString()
            quizResultWrongAnswersTv.text = userQuizInfo.wrongs.toString()
            quizResultUserCollectedExpTv.text = "+${userQuizInfo.exp}"
            quizResultGoldTv.text = userQuizInfo.collectedGold.toString()
            quizResultUserLevelTv.text = "Level ${user.level}"
            quizResultUserExpTv.text = "Exp ${user.exp}/${user.level.times(1000)}"

            profileCircularProgressBar.apply {
                progressMax = user.level.times(1000).toFloat()
                setProgressWithAnimation(user.exp.toFloat(), 1000)
            }

            quizResultOverallTv.apply {
                text = "${overAll.value}% OverAll"
                setTextColor(ResourcesCompat.getColor(resources, overAll.color, null))
            }

            quizResultMessageTv.text =
                "You have completed this quiz with ${overAll.message} result"

        }

    }

    private fun checkUserLevelUp(levelUp: Boolean) {
        if (levelUp)
            binding.quizResultUserLevelUpTv.visibility = View.VISIBLE
    }


    private fun setAnimation(lottieAnimationView: LottieAnimationView, result: String) {
        if (result == BAD_RESULT) {
            lottieAnimationView.setAnimation(R.raw.quiz_failed_anim)
        } else {
            lottieAnimationView.setAnimation(R.raw.victory_anim)
        }
    }

    private fun setResultSound(result: String) {
        val media: MediaPlayer? = if (result == BAD_RESULT)
            MediaPlayer.create(requireContext(), R.raw.quiz_failed_sound)
        else
            MediaPlayer.create(requireContext(), R.raw.good_result_sound)

        media?.start()
    }

    private fun setTitle(result: String) {
        binding.quizResultTitleTv.text = if (result == BAD_RESULT)
            "Try Again!"
        else
            "Congratulations!"
    }


    override fun onAnimationStart(p0: Animator?) {
        setResultSound(viewModel.getOverAllResult(overAll.value.toFloat()))
    }

    override fun onAnimationEnd(p0: Animator?) {
    }

    override fun onAnimationCancel(p0: Animator?) {
    }

    override fun onAnimationRepeat(p0: Animator?) {
    }

    override fun onClick(view: View?) {
        Navigation
            .findNavController(requireView())
            .navigate(QuizResultFragmentDirections.quizResultToExplore(user))
    }


}