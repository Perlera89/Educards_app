package com.educards.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.educards.R

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    fun signIn(view: View){
        startActivity(Intent(this, SignInActivity::class.java))
    }

    fun addCard(view: View){
        startActivity(Intent(this, AddCardActivity::class.java))
    }
}