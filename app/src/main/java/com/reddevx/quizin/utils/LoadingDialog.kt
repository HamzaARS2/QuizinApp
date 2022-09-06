package com.reddevx.quizin.utils


import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import com.reddevx.quizin.R

class LoadingDialog(
    private val context: Context
) {

    private lateinit var dialog: AlertDialog


    fun startLoading() {
        dialog = AlertDialog.Builder(context)
            .setCancelable(false)
            .setView(LayoutInflater.from(context).inflate(R.layout.loading_dialog_layout, null))
            .create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }

    fun close() {
        dialog.dismiss()
    }

}