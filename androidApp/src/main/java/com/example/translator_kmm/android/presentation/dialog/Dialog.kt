package com.example.translator_kmm.android.presentation.dialog

import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.example.translator_kmm.MR
import com.example.translator_kmm.android.R
import dev.icerock.moko.resources.StringResource
import dev.icerock.moko.resources.desc.desc


class Dialog {

    fun showErrorDialog(activity: Activity?) {
        val activity = activity ?: return
        prepareDialog(
            activity,
            titleRes = MR.strings.error_title,
            bodyRes = MR.strings.error_default,
            positiveButtonRes = MR.strings.ok,
        ).show()
    }

    fun showClearHistoryDialog(activity: Activity?,
                               clearHistory: Boolean,
                               onButtonClick: () -> Unit) {
        val activity = activity ?: return
        val title = if (clearHistory)
            MR.strings.view_pager_history
        else
            MR.strings.view_pager_favorites
        val message = if (clearHistory)
            MR.strings.delete_history_question
        else
            MR.strings.delete_favorites_question
        prepareDialog(
            activity,
            titleRes = title,
            bodyRes = message,
            positiveButtonRes = MR.strings.yes,
            onPositiveButtonClick = {
                onButtonClick()
            },
            negativeButtonRes = MR.strings.cancel,
        ).show()
    }

    fun showInitDialog(activity: Activity?,
                       onButtonClick: () -> Unit) {
        val activity = activity ?: return
        prepareDialog(
            activity,
            titleRes = MR.strings.init_langs_title,
            bodyRes = MR.strings.init_langs_description,
            positiveButtonRes = MR.strings.ok,
            onPositiveButtonClick = {
                onButtonClick()
            },
        ).show()
    }

    private fun prepareDialog(
        activity: Activity,
        titleRes: StringResource?,
        bodyRes: StringResource?,
        positiveButtonRes: StringResource? = null,
        onPositiveButtonClick: (() -> Unit)? = null,
        negativeButtonRes: StringResource? = null,
        onNegativeButtonClick: (() -> Unit)? = null,
    ): AlertDialog {
        val builder = AlertDialog.Builder(activity)
        val dialogLayout = activity.layoutInflater.inflate(R.layout.layout_dialog, null)
        builder.setView(dialogLayout)
        val alertDialog = builder.create()

        titleRes?.let {
            val titleTextView = dialogLayout.findViewById<TextView>(R.id.title)
            titleTextView.text = it.desc().toString(activity)
            titleTextView.visibility = View.VISIBLE
        }

        bodyRes?.let {
            val bodyTextView = dialogLayout.findViewById<TextView>(R.id.body)
            bodyTextView.text = it.desc().toString(activity)
            bodyTextView.visibility = View.VISIBLE
        }

        positiveButtonRes?.let {
            val positiveButton = dialogLayout.findViewById<Button>(R.id.positiveButton)
            positiveButton.visibility = View.VISIBLE
            positiveButton.text = it.desc().toString(activity)
            positiveButton.setOnClickListener {
                onPositiveButtonClick?.invoke()
                alertDialog.dismiss()
            }
        }

        negativeButtonRes?.let {
            val negativeButton = dialogLayout.findViewById<Button>(R.id.negativeButton)
            negativeButton.visibility = View.VISIBLE
            negativeButton.text = it.desc().toString(activity)
            negativeButton.setOnClickListener {
                onNegativeButtonClick?.invoke()
                alertDialog.dismiss()
            }
        }

        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog.setCancelable(false)
        return alertDialog
    }

}