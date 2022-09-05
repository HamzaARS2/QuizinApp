package com.reddevx.quizin.data.repositories

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.reddevx.quizin.data.models.*
import com.reddevx.quizin.listeners.*
import com.reddevx.quizin.utils.QUESTIONS_PER_QUIZ

class Repository(
    private val mFirestore: FirebaseFirestore = Firebase.firestore,
    private val mStorage: FirebaseStorage = Firebase.storage
) {

    // Categories Operations
    fun getCategories(mListener: CategoriesListener) {
        val categoryList = ArrayList<Category>()
        mFirestore.collection("categories")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { docs ->
                for (doc in docs) {
                    val category = doc.toObject(Category::class.java)
                    categoryList.add(category)
                }
                mListener.onRetrievingCategoriesCompleted(categoryList)
            }
            .addOnFailureListener {
                mListener.onRetrievingCategoriesFailed(it)
            }
    }

    fun getCategoryQuizzesById(id: String, mListener: QuizzesListener) {
        val quizList = ArrayList<Quiz>()
        mFirestore.collection("categories")
            .document(id)
            .collection("quizzes")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { docs ->
                for (doc in docs) {
                    val quiz = doc.toObject(Quiz::class.java)
                    quizList.add(quiz)
                }
                mListener.onRetrievingQuizzesCompleted(quizList)
            }
    }

    // Quizzes Operations
    fun getLatestQuizzes(mListener: LatestQuizzesListener) {
        val latestQuizzes: ArrayList<LatestQuiz> = arrayListOf()
        mFirestore
            .collection("latestQuizzes")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { docs ->
                for (doc in docs) {
                    val quiz = doc.toObject(LatestQuiz::class.java)
                    latestQuizzes.add(quiz)
                }
                mListener.onLatestQuizzesRetrievedSuccessfully(latestQuizzes)
            }
            .addOnFailureListener {
                mListener.onRetrievingLatestQuizzesFailed(it)
            }
    }

    fun getQuiz(categoryId: String, quizId: String, mListener: SuccessListener) {
        mFirestore
            .collection("categories")
            .document(categoryId)
            .collection("quizzes")
            .document(quizId)
            .get()
            .addOnSuccessListener { doc ->
                val quiz = doc.toObject(Quiz::class.java)
                mListener.onSuccess(quiz)
            }
            .addOnFailureListener {
                mListener.onFailure(it)
            }
    }

    // Questions Operations

    fun getQuestionsByDifficulty(
        categoryId: String,
        quizId: String,
        difficulty: String,
        mListener: QuestionsListener
    ) {
        val questions = ArrayList<Question>()
        mFirestore
            .collection("categories")
            .document(categoryId)
            .collection("quizzes")
            .document(quizId)
            .collection("questions")
            .whereEqualTo("difficulty", difficulty)
            .get()
            .addOnSuccessListener { docs ->
                val documents = docs.documents
                val limit =
                    if (documents.size > QUESTIONS_PER_QUIZ) QUESTIONS_PER_QUIZ else documents.size
                documents.shuffle()
                for (i in 0 until limit) {
                    val question = documents[i].toObject(Question::class.java)!!
                    questions.add(question)
                }
                mListener.onQuestionsRetrievedSuccessfully(questions)
            }
            .addOnFailureListener {
                mListener.onRetrievingQuestionsFailed(it)
            }
    }

    // User Operations
    fun saveUserData(user: User, mListener: UpdateUserListener) {
        mFirestore
            .collection("users")
            .document(user.id)
            .set(user)
            .addOnSuccessListener {
                mListener.onUserUpdatedSuccessfully(user)
            }
            .addOnFailureListener {
                mListener.onUpdatingUserFailed(it)
            }
    }

    fun getLeaderboardUsers(mListener: RankedUsersListener) {
        val rankedUsers = ArrayList<User>()
        mFirestore
            .collection("users")
            .limit(50)
            .orderBy("trophies", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { docs ->
                for (doc in docs) {
                    val user = doc.toObject(User::class.java)
                    rankedUsers.add(user)
                }
                mListener.onRankedUsersReceivedSuccessfully(rankedUsers)

            }
            .addOnFailureListener {
                mListener.onReceivingRankedUsersFailed(it)
            }

    }

    fun addUserAvatar(userId: String, avatar: String, mListener: SuccessListener) {
        mFirestore
            .collection("users")
            .document(userId)
            .update("avatar", avatar)
            .addOnSuccessListener {
                mListener.onSuccess(null)
            }
            .addOnFailureListener {
                mListener.onFailure(it)
            }
    }

 



    // Storage

    fun getAvailableAvatars(mListener: AvatarsListener) {
        val listRef = mStorage.reference.child("avatars")
        listRef.listAll()
            .addOnSuccessListener { result ->
                for (item in result.items) {
                    item.downloadUrl.addOnSuccessListener {
                        mListener.onAvatarsReceivedSuccessfully(Avatar(it.toString()))
                    }
                }
            }
            .addOnFailureListener {
                mListener.onReceivingAvatarsFailed(it)
            }
    }


}