package com.reddevx.quizin.ui.fragments.quiz

import android.animation.Animator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.*
import android.media.MediaPlayer
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.airbnb.lottie.LottieAnimationView
import com.reddevx.quizin.R
import com.reddevx.quizin.data.models.Question
import com.reddevx.quizin.data.models.User
import com.reddevx.quizin.databinding.FragmentQuizGameBinding
import com.reddevx.quizin.listeners.*
import com.reddevx.quizin.utils.LoadingDialog
import java.util.*


class QuizGameFragment : Fragment(), QuizTimeListener, Animator.AnimatorListener,
    View.OnClickListener, QuestionRoundListener, RadioGroup.OnCheckedChangeListener,
    AnswerListener, SuccessListener, UpdateUserListener {


    private lateinit var timer: Timer
    private lateinit var dialog: AlertDialog

    private lateinit var loading: LoadingDialog

    private val viewModel: QuizGameViewModel by activityViewModels()
    private val args: QuizGameFragmentArgs by navArgs()

    // Listeners
    private lateinit var mListener: QuizTimeListener
    private lateinit var roundListener: QuestionRoundListener
    private lateinit var answerListener: AnswerListener

    private val binding by lazy {
        FragmentQuizGameBinding.inflate(layoutInflater)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mListener = this
        roundListener = this
        answerListener = this
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val gamePack = args.gameData
        viewModel.postQuestions(gamePack.questions)
        viewModel.questionsMax = gamePack.questions.size
        viewModel.questionTime = gamePack.quizTime
        loading = LoadingDialog(requireContext())

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment

        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Loading starting quiz animation
        loadStartQuizAnim()
        // Setting the max value of quiz linear progress
        binding.quizGameLinearProgress.max = viewModel.questionsMax

        viewModel.currentQuestion.observe(viewLifecycleOwner) {
            // Load Current Question
            loadQuestion(it)
        }

        viewModel.quizProgress.observe(viewLifecycleOwner) {
            // Changes quiz linear progress
            binding.quizGameLinearProgress.progress = it


        }
        viewModel.gameFinished.observe(viewLifecycleOwner) {
            // Listening on game finished to navigate to the results screen
            if (it) {
                loading.createLoadingDialog()
                val userId = viewModel.getCurrentUser()!!.uid
                viewModel.getUserData(userId, this)
            }

        }


        binding.answerConfirmBtn.setOnClickListener(this)
        binding.answerNextBtn.setOnClickListener(this)
        binding.radioGroup.setOnCheckedChangeListener(this)

        observePlayerScore()


    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.answer_confirm_btn -> {
                val finishedRound = viewModel.getCurrentRound()
                val nextRound = finishedRound.inc()
                roundListener.onRoundFinished(finishedRound, nextRound)
            }
            R.id.answer_next_btn -> {
                resetGameViews()
                // Start round
                roundListener.onRoundStarted(viewModel.getCurrentRound())

            }
        }
    }

    override fun onCheckedChanged(radioGroup: RadioGroup?, checkedId: Int) {
        when (checkedId) {
            R.id.quiz_game_answer1_rb -> {
                viewModel.checkedAnswer = binding.quizGameAnswer1Rb.text.toString()
            }
            R.id.quiz_game_answer2_rb -> {
                viewModel.checkedAnswer = binding.quizGameAnswer2Rb.text.toString()
            }
            R.id.quiz_game_answer3_rb -> {
                viewModel.checkedAnswer = binding.quizGameAnswer3Rb.text.toString()
            }
            R.id.quiz_game_answer4_rb -> {
                viewModel.checkedAnswer = binding.quizGameAnswer4Rb.text.toString()
            }
        }
        binding.answerConfirmBtn.isEnabled = true
    }

    private fun loadQuestion(question: Question) {
        binding.apply {
            quizGameQuestionTv.text = question.question
            quizGameAnswer1Rb.text = question.answer1
            quizGameAnswer2Rb.text = question.answer2
            quizGameAnswer3Rb.text = question.answer3
            quizGameAnswer4Rb.text = question.answer4
        }
    }

    private fun loadStartQuizAnim() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireActivity())
        val rootView = layoutInflater.inflate(R.layout.lottie_dialog, null)

        dialog = builder.setView(rootView)
            .setCancelable(false)
            .create()
        dialog.show()
        // Make a transparent background for the dialog
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val lottieAnim: LottieAnimationView = rootView.findViewById(R.id.starting_quiz_anim)
        lottieAnim.addAnimatorListener(this)


    }

    override fun onAnimationStart(p0: Animator?) {
        // Play starting game sound
        val media = MediaPlayer.create(requireContext(), R.raw.game_start_sound)
        media.start()
    }

    override fun onAnimationEnd(p0: Animator?) {
        // Closes the animation
        dialog.let {
            it.dismiss()
        }
        // Start quiz time counter

        roundListener.onRoundStarted(viewModel.getCurrentRound())
        binding.quizGameLayout.visibility = View.VISIBLE

    }

    override fun onAnimationCancel(p0: Animator?) {
    }

    override fun onAnimationRepeat(p0: Animator?) {
    }


    private fun setCircularProgressTime(progressMax: Int) {
        binding.quizGameTimeCircluarProgress.max = progressMax

        var progress = 0;

        timer = Timer()
        val task = object : TimerTask() {
            override fun run() {
                progress += 1
                requireActivity().runOnUiThread {
                    mListener.onProgressChanged(progress, progressMax)
                    if (progress >= progressMax) {
                        val finishedRound = viewModel.getCurrentRound()
                        val nextRound = finishedRound.inc()
                        roundListener.onRoundFinished(finishedRound, nextRound)
                    }
                }

            }

        }
        timer.schedule(task, 1000, 1000)
    }


    override fun onStop() {
        super.onStop()
        // Canceling quiz counter
        timer.cancel()
    }

    override fun onProgressChanged(progress: Int, progressMax: Int) {
        binding.quizGameTimeCircluarProgress.setProgressCompat(progress, true)
        binding.quizProgressTv.text = (progressMax - progress).toString()
    }

    override fun onProgressFinished() {
        timer.cancel()

    }

    @SuppressLint("SetTextI18n")
    override fun onRoundStarted(round: Int) {

        // Set the next question
        val gameFinished = viewModel.postNextQuestion(round)
        if (gameFinished) return

        val questionNumber = round.inc()
        // Start Progress Time
        setCircularProgressTime(viewModel.questionTime)
        binding.apply {
            answerConfirmBtn.apply {
                visibility = View.VISIBLE
                isEnabled = false
            }
            answerNextBtn.visibility = View.INVISIBLE

            quizGameRemainingQuestionsTv.text =
                "Question $questionNumber of ${viewModel.questionsMax}"

        }
        viewModel.checkedAnswer = ""

        // Enabling radio buttons
        enableRadioButtons(true)
    }

    override fun onRoundFinished(finishedRound: Int, nextRound: Int) {
        mListener.onProgressFinished()
        viewModel.postQuizProgress(nextRound)
        viewModel.setNextRound(nextRound)
        binding.apply {
            answerConfirmBtn.visibility = View.INVISIBLE
            answerNextBtn.visibility = View.VISIBLE
        }
        checkAnswer()
        // Disabling radio buttons
        enableRadioButtons(false)


    }

    private fun checkAnswer() {
        if (viewModel.checkedAnswer.isEmpty()) {
            // Player did not select any answer
            answerListener.onNoAnswerSelected()

            return
        }
        if (viewModel.checkAnswer()) {
            // The selected answer is correct
            answerListener.onCorrectAnswerSelected()
            return
        }
        // The selected answer is wrong
        answerListener.onWrongAnswerSelected()


    }

    private fun getRadioButtonByValue(value: String): RadioButton? {
        return when (value) {
            binding.quizGameAnswer1Rb.text.toString() -> binding.quizGameAnswer1Rb
            binding.quizGameAnswer2Rb.text.toString() -> binding.quizGameAnswer2Rb
            binding.quizGameAnswer3Rb.text.toString() -> binding.quizGameAnswer3Rb
            binding.quizGameAnswer4Rb.text.toString() -> binding.quizGameAnswer4Rb
            else -> null
        }
    }

    private fun getCheckedRadioButton(): RadioButton? {
        return requireView().findViewById(binding.radioGroup.checkedRadioButtonId)
    }


    private fun getSelectedDrawable(
        radioButton: RadioButton,
        childPosition: Int
    ): GradientDrawable {
        val drawable = radioButton.background.mutate() as StateListDrawable
        val containerState = drawable.constantState as DrawableContainer.DrawableContainerState
        val items = containerState.children
        return items[childPosition] as GradientDrawable
    }

    private fun setCorrectAnswerDrawables(radioButton: RadioButton, childPosition: Int = 0) {
        getSelectedDrawable(radioButton, childPosition)
            .setStroke(6, Color.GREEN)
    }

    private fun setCorrectAnswerTextColor(radioButton: RadioButton) {
        radioButton.setTextColor(Color.GREEN)
    }

    private fun showTheCorrectAnswer() {
        val correctAnswer =
            viewModel.getQuestionCorrectAnswer(viewModel.currentQuestion.value ?: Question())
        val correctAnswerBtn = getRadioButtonByValue(correctAnswer) ?: return
        setCorrectAnswerDrawables(correctAnswerBtn, 1)
        setCorrectAnswerTextColor(correctAnswerBtn)
    }

    private fun setCorrectAnswerViews() {
        val checkedButton = getCheckedRadioButton()!!
        setCorrectAnswerTextColor(checkedButton)
        setCorrectAnswerDrawables(checkedButton)
    }

    private fun setWrongAnswerDrawables(drawables: Array<GradientDrawable>) {
        drawables[0].setStroke(6, Color.RED)
        drawables[1].setStroke(6, Color.GREEN)
    }


    private fun setWrongAnswerTextColor(radioButtons: Array<RadioButton>) {
        radioButtons[0].setTextColor(Color.RED)
        radioButtons[1].setTextColor(Color.GREEN)
    }

    private fun setWrongAnswerViews() {
        val checkedButton = getCheckedRadioButton() ?: return
        val correctAnswerRadioBtn = getRadioButtonByValue(
            viewModel
                .getQuestionCorrectAnswer(
                    viewModel
                        .currentQuestion.value ?: Question()
                )
        ) ?: return
        setWrongAnswerTextColor(arrayOf(checkedButton, correctAnswerRadioBtn))
        val checkedDrawable = getSelectedDrawable(checkedButton, 0)
        val correctDrawable = getSelectedDrawable(correctAnswerRadioBtn, 1)
        setWrongAnswerDrawables(arrayOf(checkedDrawable, correctDrawable))
    }


    private fun resetDrawables(drawables: RadioButton) {
        drawables.background =
            ContextCompat.getDrawable(requireContext(), R.drawable.quiz_answer_selector)

    }

    private fun resetRadiosText(radioButtons: RadioButton) {
        radioButtons.setTextColor(Color.WHITE)
    }

    private fun resetCorrectAnswerGameViews() {
        val checkedButton = getCheckedRadioButton() ?: return
        resetRadiosText(checkedButton)
        resetDrawables(checkedButton)
        binding.radioGroup.clearCheck()
    }

    private fun resetWrongAnswerGameViews() {
        val checkedButton = getCheckedRadioButton() ?: return
        checkedButton.apply {
            resetRadiosText(this)
            resetDrawables(this)
        }
        val correctAnswerRadioBtn = getRadioButtonByValue(
            viewModel
                .getQuestionCorrectAnswer(
                    viewModel
                        .currentQuestion.value ?: Question()
                )
        ) ?: return
        correctAnswerRadioBtn.apply {
            resetRadiosText(this)
            resetDrawables(this)
        }

        binding.radioGroup.clearCheck()

    }

    private fun resetGameViews() {
        if (viewModel.checkedAnswer.isEmpty()) {
            // No answer was selected in the previous round
            resetUnselectedAnswerGameViews()

            return
        } else if (viewModel.checkAnswer()) {
            // Reset views of correct answer
            resetCorrectAnswerGameViews()
            return
        }
        // Reset views of wrong answer
        resetWrongAnswerGameViews()
    }


    private fun resetUnselectedAnswerGameViews() {
        val correctAnswerRadioBtn = getRadioButtonByValue(
            viewModel
                .getQuestionCorrectAnswer(
                    viewModel
                        .currentQuestion.value ?: Question()
                )
        ) ?: return
        correctAnswerRadioBtn.apply {
            // Reset text color
            resetRadiosText(this)
            // Reset drawable
            resetDrawables(this)
        }

        binding.radioGroup.clearCheck()

    }

    private fun observePlayerScore() {
        viewModel.apply {
            correctAnswers.observe(viewLifecycleOwner) {
                binding.quizGameCorrectAnswersTv.text = it.toString()
            }
            wrongAnswers.observe(viewLifecycleOwner) {
                binding.quizGameWrongAnswersTv.text = it.toString()
            }
            collectedTrophies.observe(viewLifecycleOwner) {
                binding.quizGameTrophiesTv.text = it.toString()
            }
        }
    }

    private fun enableRadioButtons(enabled: Boolean) {
        binding.apply {
            quizGameAnswer1Rb.isEnabled = enabled
            quizGameAnswer2Rb.isEnabled = enabled
            quizGameAnswer3Rb.isEnabled = enabled
            quizGameAnswer4Rb.isEnabled = enabled
        }
    }

    override fun onCorrectAnswerSelected() {
        setCorrectAnswerViews()
        // Increase correct answers
        viewModel.increaseCorrectAnswers()
        viewModel.increaseCollectedTrophies(binding.quizProgressTv.text.toString().toInt())
    }

    override fun onWrongAnswerSelected() {
        setWrongAnswerViews()
        // Increase Wrong answers
        viewModel.increaseWrongAnswers()
    }

    override fun onNoAnswerSelected() {
        showTheCorrectAnswer()
        viewModel.increaseWrongAnswers()
    }

    override fun onSuccess(any: Any?) {
        val user = any as User
        val updatedUser = viewModel.getUpdatedUserData(user)
        viewModel.updateUserData(updatedUser, this)


    }

    override fun onFailure(e: Exception) {
        Toast.makeText(requireContext(), "Error : ${e.message}", Toast.LENGTH_SHORT).show()
        loading.close()

    }

    override fun onUserUpdatedSuccessfully(user: User) {
        loading.close()
        Navigation
            .findNavController(requireView())
            .navigate(
                QuizGameFragmentDirections.quizGameToResult(
                    user,
                    viewModel.getUserQuizInfo()
                )
            )
    }

    override fun onUpdatingUserFailed(e: Exception) {
        Toast.makeText(requireContext(), "Error : ${e.message}", Toast.LENGTH_SHORT).show()
        loading.close()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.clearGame()
    }


}