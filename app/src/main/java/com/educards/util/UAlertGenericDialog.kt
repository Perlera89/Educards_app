package com.educards.util

import android.app.AlertDialog
import android.content.Context

object UAlertGenericDialog {
    fun createDialogAlert(_context:Context,_title:String,_message:String){
        val builder = AlertDialog.Builder(_context)
        builder.setTitle(_title)
            .setMessage("\n$_message")
            .setCancelable(true)
            .setPositiveButton("Aceptar"){dialogInterface, it ->
                dialogInterface.cancel()
            }
            .show()
    }
}