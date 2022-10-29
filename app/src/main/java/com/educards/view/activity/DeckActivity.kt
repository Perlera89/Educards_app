package com.educards.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.educards.R

class DeckActivity : AppCompatActivity() {
    private lateinit var activity: DeckActivity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_deck)

        activity = this
    }

    fun onClick(view: View) {
        when (view.id) {
            R.id.bt_back -> {
                activity.finish()
            }
        }
    }

    fun back(view: View){
        this.finish()
    }
}