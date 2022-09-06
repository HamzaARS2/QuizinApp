package com.reddevx.quizin.ui.fragments.quiz_settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.reddevx.quizin.R
import com.reddevx.quizin.data.models.GamePack
import com.reddevx.quizin.data.models.Question
import com.reddevx.quizin.databinding.FragmentSettingsQuizBinding
import com.reddevx.quizin.listeners.QuestionsListener
import com.reddevx.quizin.utils.LoadingDialog

class QuizSettingsFragment : Fragment(), QuestionsListener, View.OnClickListener {
    private val viewModel: QuizSettingsViewModel by activityViewModels()
    private val args: QuizSettingsFragmentArgs by navArgs()
    private lateinit var quizId: String
    private lateinit var categoryId: String

    private lateinit var loading: LoadingDialog

    private val binding by lazy {
        FragmentSettingsQuizBinding.inflate(layoutInflater)
    }

    private object Constants {
        // Difficulties
        const val EASY_DIFFICULTY = "easy"
        const val NORMAL_DIFFICULTY = "normal"
        const val HARD_DIFFICULTY = "hard"

        // Times
        const val THE_QUICKER_TIME = 7
        const val THE_NORMAL_TIME = 15
        const val THE_LONGER_TIME = 20


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        categoryId = args.categoryId
        quizId = args.quizId
        loading = LoadingDialog(requireContext())


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.apply {
            difficultyEasyCv.setOnClickListener(this@QuizSettingsFragment)
            difficultyNormalCv.setOnClickListener(this@QuizSettingsFragment)
            difficultyHardCv.setOnClickListener(this@QuizSettingsFragment)
            card7sec.setOnClickListener(this@QuizSettingsFragment)
            card15sec.setOnClickListener(this@QuizSettingsFragment)
            card20sec.setOnClickListener(this@QuizSettingsFragment)
            extendedFabStartQuiz.setOnClickListener(this@QuizSettingsFragment)

            quizSettingsToolbar.setNavigationOnClickListener {
                Navigation
                    .findNavController(requireView())
                    .popBackStack()
            }
        }


    }

    override fun onQuestionsRetrievedSuccessfully(questions: ArrayList<Question>) {
        loading.close()
        if (questions.size == 0) {
            Toast.makeText(
                requireContext(),
                "This quiz does not have enough questions. Please change difficulty or try again later",
                Toast.LENGTH_LONG
            ).show()
            return
        }
        val gamePack = GamePack(questions, viewModel.selectedQuizTime)
        Navigation
            .findNavController(requireView())
            .navigate(QuizSettingsFragmentDirections.quizSettingsToQuizGame(gamePack))

    }

    override fun onRetrievingQuestionsFailed(e: Exception) {
        Toast.makeText(requireContext(), "Error : ${e.message}", Toast.LENGTH_SHORT).show()
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.difficulty_easy_cv -> {
                checkEasyDifficulty()
            }
            R.id.difficulty_normal_cv -> {
                checkNormalDifficulty()
            }
            R.id.difficulty_hard_cv -> {
                checkHardDifficulty()
            }
            R.id.card_7sec -> {
                selectTheQuickerTime()
            }
            R.id.card_15sec -> {
                selectTheNormalTime()
            }
            R.id.card_20sec -> {
                selectTheLongerTime()
            }
            R.id.extended_fab_start_quiz -> {
                val difficulty = viewModel.selectedDifficulty
                viewModel.getQuestionsByDifficulty(categoryId, quizId, difficulty, this)
                loading.startLoading()
            }
        }
    }

    private fun checkEasyDifficulty() {
        binding.apply {
            difficultyEasyCheck.visibility = View.VISIBLE
            difficultyNormalCheck.visibility = View.INVISIBLE
            difficultyHardCheck.visibility = View.INVISIBLE
        }
        viewModel.selectedDifficulty = Constants.EASY_DIFFICULTY
    }

    private fun checkNormalDifficulty() {
        binding.apply {
            difficultyEasyCheck.visibility = View.INVISIBLE
            difficultyNormalCheck.visibility = View.VISIBLE
            difficultyHardCheck.visibility = View.INVISIBLE
        }
        viewModel.selectedDifficulty = Constants.NORMAL_DIFFICULTY
    }

    private fun checkHardDifficulty() {
        binding.apply {
            difficultyEasyCheck.visibility = View.INVISIBLE
            difficultyNormalCheck.visibility = View.INVISIBLE
            difficultyHardCheck.visibility = View.VISIBLE
        }
        viewModel.selectedDifficulty = Constants.HARD_DIFFICULTY
    }

    private fun selectTheQuickerTime() {
        binding.apply {
            time7sIsSelectedTv.visibility = View.VISIBLE
            time15sIsSelectedTv.visibility = View.INVISIBLE
            time20sIsSelectedTv.visibility = View.INVISIBLE
        }
        viewModel.selectedQuizTime = Constants.THE_QUICKER_TIME
    }

    private fun selectTheNormalTime() {
        binding.apply {
            time7sIsSelectedTv.visibility = View.INVISIBLE
            time15sIsSelectedTv.visibility = View.VISIBLE
            time20sIsSelectedTv.visibility = View.INVISIBLE
        }
        viewModel.selectedQuizTime = Constants.THE_NORMAL_TIME
    }

    private fun selectTheLongerTime() {
        binding.apply {
            time7sIsSelectedTv.visibility = View.INVISIBLE
            time15sIsSelectedTv.visibility = View.INVISIBLE
            time20sIsSelectedTv.visibility = View.VISIBLE
        }
        viewModel.selectedQuizTime = Constants.THE_LONGER_TIME
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.resetQuizSettings()
    }


}