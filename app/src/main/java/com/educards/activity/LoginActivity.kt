package com.educards.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
<<<<<<< Updated upstream
import android.util.Log
=======
import android.os.Handler
import android.os.Message
>>>>>>> Stashed changes
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.EditText
<<<<<<< Updated upstream
import android.widget.Toast
=======
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.bumptech.glide.Glide
>>>>>>> Stashed changes
import com.educards.R
import com.educards.model.User
import com.educards.service.FirebaseConnection
import com.educards.service.SUser
import com.educards.util.UTextView
import kotlinx.coroutines.*

class LoginActivity : AppCompatActivity() {
    private lateinit var etEmail:EditText
    private lateinit var etPassword: EditText

    private lateinit var relative_main: RelativeLayout
    private lateinit var page_start: ImageView
    private lateinit var ly_main:LinearLayout
    //private lateinit var drawer: DrawerLayout

    private var isShowPageStart = true
    private val MESSAGE_SHOW_DRAWER_LAYOUT = 0x001
    private val MESSAGE_SHOW_START_PAGE = 0x002


    var mHandler: Handler = @SuppressLint("HandlerLeak")
    object : Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                MESSAGE_SHOW_DRAWER_LAYOUT -> {
                    //drawer.openDrawer(androidx.core.view.GravityCompat.START)
                    val sharedPreferences = getSharedPreferences("app", MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    editor.putBoolean("isFirst", false)
                    editor.apply()
                }
                MESSAGE_SHOW_START_PAGE -> {
                    val alphaAnimation = AlphaAnimation(1.0f, 0.0f)
                    alphaAnimation.duration = 300
                    alphaAnimation.setAnimationListener(object : Animation.AnimationListener {
                        override fun onAnimationStart(animation: Animation) {}
                        override fun onAnimationEnd(animation: Animation) {
                            relative_main.visibility = View.GONE
                            ly_main.visibility = View.VISIBLE
                        }

                        override fun onAnimationRepeat(animation: Animation) {}
                    })
                    relative_main.startAnimation(alphaAnimation)
                }
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        etEmail =findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)

        /*
        Se le cierra la sesion  al usuario que este logueado
         */
<<<<<<< Updated upstream
        if (SUser.getCurrentUser() != null){
            FirebaseConnection.firebaseAuth.signOut()
=======

        //validacion de usuario
        if (SUser.getCurrentUser() != null && SUser.getCurrentUserDetailData().getVerified()){
            startActivity(Intent(this,MainActivity::class.java))
        }else{
            setContentView(R.layout.activity_login)
            //animacion
            relative_main = findViewById(R.id.relative_main)
            page_start = findViewById(R.id.img_page_start)
            ly_main = findViewById(R.id.ly_main)

            etEmail = findViewById(R.id.etEmail)
            etPassword = findViewById(R.id.etPassword)
            pageStart()
>>>>>>> Stashed changes
        }
    }

    private fun pageStart(){
        val sharedPreferences = getSharedPreferences("app", MODE_PRIVATE)
        if (isShowPageStart) {
            //ly_main.visibility = View.GONE
            relative_main.visibility = View.VISIBLE
            Glide.with(this).load(R.mipmap.ic_launcher_round).into(page_start)
            if (sharedPreferences.getBoolean("isFirst", true)) {
            } else {
                mHandler.sendEmptyMessageDelayed(MESSAGE_SHOW_START_PAGE, 3000)
            }
            isShowPageStart = false

        }
        if (sharedPreferences.getBoolean("isFirst", true)) {
            mHandler.sendEmptyMessageDelayed(MESSAGE_SHOW_DRAWER_LAYOUT, 2500)
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
                        startActivity(Intent(applicationContext, MainActivity::class.java))
                    }else if(SUser.getCurrentUserDetailData().getVerified()==false && SUser.getCurrentUserDetailData().getIdUser() != ""){
                        Log.d("activity/LoginActivity","Debe verificar su correo correo electrónico para iniciar sesión.")
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
    override fun onDestroy() {
        mHandler.removeCallbacksAndMessages(null)
        super.onDestroy()
    }
}