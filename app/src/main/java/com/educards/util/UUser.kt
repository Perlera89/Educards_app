package com.educards.util

import android.util.Log
import android.widget.EditText
import android.widget.Toast

object UUser {
    /*
    1 Permite verificar si la contraseña cumple con el formato de longitud minima de 8 caracteres y que deben contener almenos
    una letra mayuscula, una minuscula y un numero
    - Regex("[a-z]").containsMatchIn(pass): verdadero si la contraseña tiene un caracter entre a y z
    - Regex("[A-Z]").containsMatchIn(pass): verdadero si la contraseña tiene un caracter entre A y Z
    - Regex("[0-9]").containsMatchIn(pass):  verdadero si la contraseña tiene un numero entre 0 y 9
    Sino cumple mandamos la alerte
    2. Luego verificamos que el largo de la contraseña sea mayor a 7 caracteres, y si no cumple mandamos la alerte
    Si todo esta bien retornamos un estado de verdadero*/
    fun verifyPassword(_pass: EditText):Boolean{
        var statePassword = false
        var pass = _pass.text.toString()
        if (Regex("[a-z]").containsMatchIn(pass) && Regex("[A-Z]").containsMatchIn(pass)
            && Regex("[0-9]").containsMatchIn(pass)){
            if (pass.length >=8) {
                statePassword = true
                Log.d("util/UUser", "La contraseña contiene caracteres en los rangos de: a-z, A-Z , 0-9, y es" +
                        "de longitud mayor a 7 .Estado de aprobación es: ${statePassword}")
            }
        }else{
            Toast.makeText(_pass.context, "Formato de contraseña inválido. Use mayúsculas, minúsculas, numeros. Debe tener más de 7 caracteres", Toast.LENGTH_LONG).show()
        }
        return statePassword
    }
}