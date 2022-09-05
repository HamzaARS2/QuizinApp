package com.reddevx.quizin.data.repositories

import android.util.Log
import com.reddevx.quizin.data.models.User
import com.reddevx.quizin.listeners.UserListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.reddevx.quizin.listeners.SignInListener
import com.reddevx.quizin.listeners.SuccessListener
import com.reddevx.quizin.listeners.UpdateUserListener

class AuthRepository(
    private val mAuth: FirebaseAuth = Firebase.auth,
    private val mFirestore: FirebaseFirestore = Firebase.firestore
) {


    // Current User
    fun getSignedInUser(): FirebaseUser? {
        return mAuth.currentUser
    }

    fun signOut() {
        if (getSignedInUser() != null)
            mAuth.signOut()
    }

    // Register new user
    fun createNewUser(username: String, email: String, password: String, mListener: UserListener) {
        // Creating new user
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val newUser = mAuth.currentUser
                    val uid = newUser?.uid
                    mListener.onUserCreatedSuccessfully(User(uid!!, username, email))
                    // Adding new user to users collection
                    mFirestore
                        .collection("users")
                        .document(uid)
                        .set(User(uid, username, email))
                        .addOnSuccessListener {
                            Log.d("AuthRepo", "User added to collection successfully")
                        }
                        .addOnFailureListener {
                            Log.w("AuthRepo", "User failed to be added to the collection")
                        }
                } else {
                    mListener.onUserCreationFailed(task.exception)
                }

            }

    }


    fun loginUser(email: String, password: String, mListener: SignInListener) {
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val uid = mAuth.currentUser?.uid
                    mFirestore
                        .collection("users")
                        .document(uid!!)
                        .get()
                        .addOnSuccessListener { doc ->
                            val user = doc.toObject(User::class.java)
                            mListener.onUserSignedInSuccessfully(user!!)
                        }
                        .addOnFailureListener {
                            mListener.onUserSigningFailed(it)
                        }
                } else {
                    mListener.onUserSigningFailed(task.exception)
                }
            }
    }

    fun getCurrentUserData(uid: String, mListener: SuccessListener) {
        mFirestore
            .collection("users")
            .document(uid)
            .get()
            .addOnSuccessListener {
                val user = it.toObject(User::class.java)
                mListener.onSuccess(user)
            }
            .addOnFailureListener {
                mListener.onFailure(it)
            }

    }

    fun updateUserInfo(updatedUser: User, mListener: UpdateUserListener) {
        val user = mAuth.currentUser
        user!!.updateEmail(updatedUser.email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    mFirestore
                        .collection("users")
                        .document(updatedUser.id)
                        .update(
                            "username", updatedUser.username,
                            "email", updatedUser.email
                        )
                        .addOnSuccessListener {
                            mListener.onUserUpdatedSuccessfully(updatedUser)
                        }
                        .addOnFailureListener {
                            mListener.onUpdatingUserFailed(it)
                        }
                } else {
                    mListener.onUpdatingUserFailed(task.exception!!)
                }
            }
    }

    fun deleteUser(uid: String, mListener: SuccessListener) {
        val user = mAuth.currentUser!!
        user
            .delete()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    mFirestore
                        .collection("users")
                        .document(uid)
                        .delete()
                        .addOnSuccessListener {
                            mListener.onSuccess(null)
                        }
                        .addOnFailureListener {
                            mListener.onFailure(it)
                        }
                } else {
                    mListener.onFailure(task.exception!!)
                }
            }
    }


}