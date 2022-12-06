package com.educards.util

import android.content.Context
import android.widget.EditText
import android.widget.TextView

object UTextView {

    /*
    1. Permite verificar si los TextViews está vacios, recibe el contexto para dar los mensajes de alerta,
    la variable del textview a evaluar y un mesaje de error si está vacio, retorna un estado verdader si tiene datos y falso si está vacio
     */
    fun verifyContentInEditText(_context:Context, _textView: EditText, _errorMessaje:String):Boolean{
        var state = false
        if(_textView.text.trim().isNotBlank() && _textView.text.trim().isNotEmpty()){
            state = true
            _textView.error = null
        }else{
            _textView.setText("")
            _textView.error = _errorMessaje
            state = false
        }
        return state
    }

    /*
    1. Permite limpiar el contenido de todos los textviews que reciba atraves de vararg y luego los itera asignando una cadena vacia
    al textview
     */
    fun clearContentInTextViews(vararg _textViews: TextView){
        _textViews.forEach {
            it.setText("")
        }
    }
    fun adjustTextInTextView(_textView:TextView){
        val textLength = _textView.text.length
        if (textLength in 1..100){
            _textView.setTextSize(21F)
        }else if (textLength in 101..140){
            _textView.setTextSize(20F)
        }else if (textLength in 141..180){
            _textView.setTextSize(18F)
        }else if (textLength in 181..250){
            _textView.setTextSize(16F)
        }else if (textLength in 251..350){
            _textView.setTextSize(14F)
        }else if (textLength in 351..450){
            _textView.setTextSize(13F)
        }else{
            _textView.setTextSize(10F)
        }
    }
}