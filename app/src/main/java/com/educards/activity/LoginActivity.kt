package com.educards.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.educards.R
import com.educards.model.User
import com.educards.service.FirebaseConnection
import com.educards.service.SUser
import com.educards.util.Listeners
import com.educards.util.UTextView
import kotlinx.coroutines.*

class LoginActivity : AppCompatActivity() {
    private lateinit var etEmail:EditText
    private lateinit var etPassword: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /*
        Se le cierra la sesion  al usuario que este logueado
         */
        if (SUser.getCurrentUser() != null){
            startActivity(Intent(this,MainActivity::class.java))
        }else{
            setContentView(R.layout.activity_login)
            etEmail = findViewById(R.id.etEmail)
            etPassword = findViewById(R.id.etPassword)
        }

    }

    /*
    1. Metodo conectado a la vista para iniciar sesion
    2. Verificamos que el campo de correo e email no estén vacios con el metodo verifyContentInTextViews de la clase UTextView
    3. Luego creamos una corutina en un hilo diferente del Main(Para evitar que la vista se congele) para iniciar el proceso de
    iniciar sesion
    4. Limpiamos los datos de la clase User y agregamos los nuevos datos
    5. Accedemos al metodo loginToApp de la clase SUser, mandamos un context para los mensajes y el objeto de User y colocamos
    un sleep para esperar que el login se realice y se actualice el current user
    6. Con el método getCurrentUserDetailData accedemos a las propiedades del usuario logueado, y si este en su propiedad
    isEmailVerified es falso le indicamos por mensaje que debe verificarlo
     */
    fun logIn(view:View){
        if (UTextView.verifyContentInTextViews(this,etEmail, "Campo de email nulo o vacío")) {
            if (UTextView.verifyContentInTextViews(this,etPassword, "Campo de contraseña nulo o vacío")) {
                CoroutineScope(Dispatchers.IO).launch {
                    User.clearUserData()
                    User.setEmail(etEmail.text.toString())
                    User.setPassword(etPassword.text.toString())
                    SUser.loginToApp(this@LoginActivity, User)
                    Thread.sleep(3000)

                    if (SUser.getCurrentUserDetailData().getVerified() && SUser.getCurrentUserDetailData().getIdUser() != "") {
                        this@LoginActivity.finish()
                        FirebaseConnection.refreshRefGlobal()
                        startActivity(Intent(applicationContext, MainActivity::class.java))
                    }else if(SUser.getCurrentUserDetailData().getVerified()==false && SUser.getCurrentUserDetailData().getIdUser() != ""){
                        //Log.d("activity/LoginActivity","Debe verificar su correo correo electrónico para iniciar sesión.")
                        Toast.makeText(this@LoginActivity, "Debe verificar su correo correo electrónico para iniciar sesión.", Toast.LENGTH_LONG).show()
                    }else{ }
                }
            }
        }
    }

    fun signIn(view: View){
        this@LoginActivity.finish()
        startActivity(Intent(applicationContext, SignInActivity::class.java))
    }
}