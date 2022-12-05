package com.educards.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.educards.R
import com.educards.model.User
import com.educards.service.SUser
import com.educards.util.UTextView
import com.educards.util.UUser

class SignInActivity : AppCompatActivity() {
    private lateinit var etName: EditText
    private lateinit var etEmail:EditText
    private lateinit var etPass:EditText
    private lateinit var etConfirmPass:EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        etName = findViewById(R.id.etName)
        etEmail = findViewById(R.id.etEmail)
        etPass = findViewById(R.id.etPassword)
        etConfirmPass = findViewById(R.id.etConfirmPassword)

        if (SUser.getCurrentUser() != null){
            SUser.userLogout(this)
        }
    }

    /*
    1. Metodo asociado al boton de registro que permite registrar el usuario
    2. Se utiliza el metodo verifyContentInTextViews de la clase UTextView para verificar que todos los campos necesarios no
    esten vacios
    3. Si todo tiene datos usamos el metodo verifyPassword de la clase UUser para verificar el largo de la contrsena y el formato
    4. Si el formato es correcto comparamos la contraseña con la ingresada en el campo de confirmar contraseña
    5. Si las validaciones están bien accedemos al metodo clearUserData de la clase unica User para limpiar otros valores
    6. Procedemos a agregar los nuevos datos y accedemos al metodo saveUser de la clase SUser para guardar el usuario; recibe un
    context para mandar mensajes y el objeto de user
    7 Luego usamos el metodo clearContentInTextViews de la clase UTextView para limpiar todo el contenido de los textviews
     */
    fun registerUser(view:View){
        if(UTextView.verifyContentInTextViews(this,etName,"Null or empty name field")) {
            if (UTextView.verifyContentInTextViews(this,etEmail,"Null or empty email field")) {
                if (UTextView.verifyContentInTextViews(this,etPass,"Null or empty password field")) {
                    if (UTextView.verifyContentInTextViews(this,etConfirmPass,"Confirm password field null or empty")) {
                        if (UUser.verifyPassword(etPass)) {
                            if (etPass.text.toString() == etConfirmPass.text.toString()) {
                                User.clearUserData()
                                User.setEmail(etEmail.text.toString())
                                User.setPassword(etPass.text.toString())
                                User.setDisplayName(etName.text.toString())
                                SUser.saveUser(this, User)
                                UTextView.clearContentInTextViews(etName,etEmail,etPass,etConfirmPass)
                            } else {
                                Toast.makeText(this, "Passwords are different.", Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                }
            }
        }
    }

    fun logIn(view: View){
        startActivity(Intent(this, LoginActivity::class.java))
        this.finish()
    }
}