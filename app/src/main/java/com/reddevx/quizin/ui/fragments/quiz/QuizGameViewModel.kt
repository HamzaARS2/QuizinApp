package com.reddevx.quizin.ui.fragments.quiz

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import com.reddevx.quizin.data.models.Question
import com.reddevx.quizin.data.models.User
import com.reddevx.quizin.data.models.UserQuizInfo
import com.reddevx.quizin.data.repositories.AuthRepository
import com.reddevx.quizin.data.repositories.Repository
import com.reddevx.quizin.listeners.SuccessListener
import com.reddevx.quizin.listeners.UpdateUserListener
import com.reddevx.quizin.utils.UserExperienceSystem
import com.reddevx.quizin.utils.UserGoldCollector
import com.reddevx.quizin.utils.UserTrophiesSystem

class QuizGameViewModel(
    private val repository: Repository = Repository(),
    private val authRepo: AuthRepository = AuthRepository()
) : ViewModel() {


    private val questions: ArrayList<Question> = arrayListOf()
    var questionTime: Int = 15
    var questionsMax = 0
    private var nextRound = 0
    var checkedAnswer = ""

    var collectedExp = 0
    var levelUp = false
    var collectedGold = 0


    // Current Question
    private val _currentQuestion = MutableLiveData(Question())
    val currentQuestion: LiveData<Question> get() = _currentQuestion

    // Quiz Progress
    private val _quizProgress = MutableLiveData(0)
    val quizProgress: LiveData<Int> get() = _quizProgress

    // Game Finished
    private val _gameFinished = MutableLiveData(false)
    val gameFinished: LiveData<Boolean> get() = _gameFinished

    // Correct answers
    private val _correctAnswers = MutableLiveData(0)
    val correctAnswers: LiveData<Int> get() = _correctAnswers

    // Wrong answers
    private val _wrongAnswers = MutableLiveData(0)
    val wrongAnswers: LiveData<Int> get() = _wrongAnswers

    // Collected trophies
    private val _collectedTrophies = MutableLiveData(0)
    val collectedTrophies: LiveData<Int> get() = _collectedTrophies


    fun clearGame() {
        nextRound = 0
        questionsMax = 0
        collectedExp = 0
        collectedGold = 0
        levelUp = false
        checkedAnswer = ""
        _correctAnswers.postValue(0)
        _wrongAnswers.postValue(0)
        _collectedTrophies.postValue(0)
        _gameFinished.postValue(false)
        _quizProgress.postValue(0)
        _currentQuestion.postValue(Question())
        finishGame(false)
        questions.clear()
    }

    fun increaseCorrectAnswers() {
        val corrects = correctAnswers.value?.inc()
        _correctAnswers.postValue(corrects ?: 0)
    }

    fun increaseWrongAnswers() {
        val wrongs = wrongAnswers.value?.inc()
        _wrongAnswers.postValue(wrongs ?: 0)
    }

    fun increaseCollectedTrophies(elapsedTime: Int) {
        val earnedTrophies = UserTrophiesSystem(
            questionMaxTime = questionTime,
            elapsedTime = elapsedTime,
            quizDifficulty = questions[0].difficulty
        )
            .getCollectedTrophies()
        val trophies = collectedTrophies.value!! + earnedTrophies
        _collectedTrophies.postValue(trophies)
    }

    fun postQuizProgress(progress: Int) {
        _quizProgress.postValue(progress)
    }

    fun postNextQuestion(progress: Int): Boolean {
        if (progress >= questionsMax) {
            finishGame(true)
            return true
        }
        _currentQuestion.postValue(questions[progress])
        return false
    }

    private fun finishGame(isFinished: Boolean) {
        _gameFinished.postValue(isFinished)
    }

    fun postQuestions(questionList: ArrayList<Question>) {
        questions.addAll(questionList)
    }

    fun setNextRound(round: Int) {
        this.nextRound = round
    }

    fun getCurrentRound(): Int = this.nextRound

    private fun correctAnswer(question: Question, answer: String): Boolean {
        val questionCorrectAnswer = getQuestionCorrectAnswer(question)
        return questionCorrectAnswer == answer
    }

    fun checkAnswer(): Boolean {
        return correctAnswer(currentQuestion.value!!, checkedAnswer)
    }

    fun getQuestionCorrectAnswer(question: Question): String {
        return when (question.correctAnswer) {
            "answer1" -> {
                question.answer1
            }
            "answer2" -> {
                question.answer2
            }
            "answer3" -> {
                question.answer3
            }
            else -> {
                question.answer4
            }

        }

    }

    fun getUserData(uid: String, mListener: SuccessListener) {
        authRepo.getCurrentUserData(uid, mListener)

    }

    fun getCurrentUser(): FirebaseUser? {
        return authRepo.getSignedInUser()
    }

    fun updateUserData(user: User, mListener: UpdateUserListener) {
        repository.saveUserData(user, mListener)
    }

    fun getUpdatedUserData(user: User): User {
        val correctAnswers = correctAnswers.value!!
        val wrongAnswers = wrongAnswers.value!!
        val collectedTrophies = collectedTrophies.value!!
        val userExpSystem = UserExperienceSystem(user)
        val pair = userExpSystem.getUpdatedUserExperience(collectedTrophies)
        val updatedUser = pair.first
        this.collectedExp = pair.second
        this.collectedGold = collectUserGold(
            user,
            questions[0].difficulty,
            collectedTrophies,
            correctAnswers,
            wrongAnswers,
            questionTime
        )
        levelUp = userExpSystem.levelUp
        // Saving collected experience
        updatedUser.apply {
            correct = user.correct + correctAnswers
            wrong = user.wrong + wrongAnswers
            trophies = user.trophies + collectedTrophies
            gold += collectedGold
        }
        return updatedUser
    }


    fun getUserQuizInfo(): UserQuizInfo = UserQuizInfo(
        questions[0].difficulty,
        collectedTrophies.value!!,
        correctAnswers.value!!,
        wrongAnswers.value!!,
        collectedExp,
        collectedGold,
        levelUp,
        questionTime,
        questionsMax
    )

    private fun collectUserGold(
        user: User,
        difficulty: String,
        trophies: Int,
        corrects: Int,
        wrongs: Int,
        questionMaxTime: Int
    ): Int {
        val userGoldCollector =
            UserGoldCollector(user, difficulty, trophies, corrects, wrongs, questionMaxTime)
        return userGoldCollector.getCollectedGold()
    }


}