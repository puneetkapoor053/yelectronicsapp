package com.ycompany.yelectronics.utils

import android.app.Activity
import android.app.AlertDialog
import android.widget.TextView
import com.ycompany.yelectronics.ui.R

class ProgressLoadingDialog(private val activity: Activity) {

    private var dialog: AlertDialog? = null

    fun startLoadingDialog(message: String) {
        val builder = AlertDialog.Builder(activity)
        val inflater = activity.layoutInflater
        val view = inflater.inflate(R.layout.custom_progress_dialog, null)
        builder.setView(view)
        val textView = view.findViewById<TextView>(R.id.dialogMessage)
        textView.text = message
        builder.setCancelable(false)
        dialog = builder.create()
        dialog?.show()
    }

    fun dismissDialog() {
        dialog?.dismiss()
    }
}