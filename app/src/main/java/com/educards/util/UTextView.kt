package com.educards.util

import android.content.Context
import android.widget.TextView
import android.widget.Toast

object UTextView {

    /*
    1. Permite verificar si los TextViews está vacios, recibe el contexto para dar los mensajes de alerta,
    la variable del textview a evaluar y un mesaje de error si está vacio, retorna un estado verdader si tiene datos y falso si está vacio
     */
    fun verifyContentInTextViews(_context:Context, _textView: TextView, _errorMessaje:String):Boolean{
        var state = false
        if(_textView.text.trim().isNotBlank() && _textView.text.trim().isNotEmpty()){
            state = true
        }else{
            _textView.setText("")
            Toast.makeText(_context,_errorMessaje,Toast.LENGTH_LONG).show()
            state = false
        }
        return state
    }

    /*
    1. Permite limpiar el contenido de todos los textviews que reciba atraves de vararg y luego los itera asignando una cadena vacia
    al textview
     */
    fun clearContentInTextViews(vararg _textViews: android.widget.TextView){
        _textViews.forEach {
            it.setText("")
        }
    }
}