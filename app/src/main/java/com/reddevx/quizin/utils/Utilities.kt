package com.reddevx.quizin.utils

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.core.view.*
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.QueryProductDetailsParams
import com.reddevx.quizin.R


// Note : This will return 7 questions for every quiz.
// So if you want to increase/decrease questions for each quiz just change the value of this QUESTIONS_PER_QUIZ constant
const val QUESTIONS_PER_QUIZ = 7

const val SHORT_QUESTION_TIME = 7
const val NORMAL_QUESTION_TIME = 15
const val LONG_QUESTION_TIME = 20

const val GREAT_RESULT = "Great"
const val GOOD_RESULT = "Good"
const val BAD_RESULT = "Bad"

const val PROFESSIONAL_BADGE = "Professional"
const val INTERMEDIATE_BADGE = "Intermediate"
const val BEGINNER_BADGE = "Beginner"

fun setFullScreen(window: Window) {
    WindowCompat.setDecorFitsSystemWindows(window, false)
}

fun lightStatusBar(window: Window, isLight: Boolean = true) {
    WindowInsetsControllerCompat(window, window.decorView)
        .isAppearanceLightStatusBars = isLight
}

fun setViewInsets(itemView: View) {
    ViewCompat.setOnApplyWindowInsetsListener(itemView) { view, windowInsets ->
        val insets = windowInsets.getInsets(WindowInsetsCompat.Type.statusBars())
        // Apply the insets as a margin to the view. Here the system is setting
        // only the bottom, left, and right dimensions, but apply whichever insets are
        // appropriate to your layout. You can also update the view padding
        // if that's more appropriate.
        view.updateLayoutParams<ViewGroup.MarginLayoutParams> {
            topMargin = insets.top
        }
        // Return CONSUMED if you don't want want the window insets to keep being
        // passed down to descendant views.
        WindowInsetsCompat.CONSUMED
    }
}

fun hideNavigationBar(window: Window) {
    WindowInsetsControllerCompat(
        window,
        window.decorView
    ).hide(WindowInsetsCompat.Type.navigationBars())
}

fun showNavigationBar(window: Window) {
    WindowInsetsControllerCompat(
        window,
        window.decorView
    ).show(WindowInsetsCompat.Type.navigationBars())
}

fun getProducts() : MutableList<QueryProductDetailsParams.Product> {
    return mutableListOf(
        QueryProductDetailsParams.Product.newBuilder()
            .setProductId("gems_pack_lvl1")
            .setProductType(BillingClient.ProductType.INAPP)
            .build(),
        QueryProductDetailsParams.Product.newBuilder()
            .setProductId("gems_pack_lvl2")
            .setProductType(BillingClient.ProductType.INAPP)
            .build(),
        QueryProductDetailsParams.Product.newBuilder()
            .setProductId("gems_pack_lvl3")
            .setProductType(BillingClient.ProductType.INAPP)
            .build()
    )
}


fun showDialog(cancelable: Boolean, message: String ,context:Context,setPositiveBtn: (dialog: DialogInterface) -> Unit) {
    val dialog = AlertDialog.Builder(context)
        .setCancelable(cancelable)
        .setMessage(message)
        .setPositiveButton(R.string.yes) { dialog, _ ->
            setPositiveBtn(dialog)
        }
        .setNegativeButton(R.string.no) { dialog, _ ->
            dialog.dismiss()
        }
        .create()
    dialog.show()
}












