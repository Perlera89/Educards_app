package com.educards.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import com.educards.R
import com.educards.model.User
import com.educards.service.FirebaseConnection
import com.educards.service.SUser
import com.educards.util.UAlertGenericDialog
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
        if (SUser.getCurrentUser() != null && SUser.getCurrentUserDetailData().getVerified()){
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
        if (UTextView.verifyContentInTextViews(this,etEmail, "Null or empty email field")) {
            if (UTextView.verifyContentInTextViews(this,etPassword, "Null or empty password field")) {
                    User.clearUserData()
                    User.setEmail(etEmail.text.toString())
                    User.setPassword(etPassword.text.toString())
                    SUser.loginToApp(this, User)
                    CoroutineScope(Dispatchers.Main).launch {
                        Thread.sleep(1500)
                        if (SUser.getCurrentUserDetailData().getVerified() && SUser.getCurrentUserDetailData().getIdUser() != "") {
                            FirebaseConnection.refreshRefGlobal()
                            startActivity(Intent(applicationContext, MainActivity::class.java))
                            this@LoginActivity.finish()
                        }else if(SUser.getCurrentUserDetailData().getVerified()==false && SUser.getCurrentUserDetailData().getIdUser() != ""){
                            UAlertGenericDialog.createDialogAlert(this@LoginActivity,"Login","You must verify your email to login.")
                        }else{ }
                    }

            }
        }
    }

    fun signIn(view: View){
        startActivity(Intent(applicationContext, SignInActivity::class.java))
        this@LoginActivity.finish()
    }
}