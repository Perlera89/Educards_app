package com.educards.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.educards.R
import com.educards.adapter.RecyclerCardAdapter
import com.educards.model.entities.Card

class EstudyActivity : AppCompatActivity() {
    private lateinit var recyclerCard: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_estudy)

        var horizontalLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        recyclerCard = findViewById(R.id.recycler_cards_study)

        val adapter = RecyclerCardAdapter(getCards())
        recyclerCard.setHasFixedSize(true)
        recyclerCard.layoutManager = horizontalLayoutManager
        recyclerCard.adapter = adapter
    }

    private fun getCards(): MutableList<Card>{
        val cards: MutableList<Card> = ArrayList()
        cards.add(Card("1", "Pregunta 1", "Respuesta 1"))
        cards.add(Card("2", "Pregunta 2", "Respuesta 2"))
        cards.add(Card("3", "Pregunta 3", "Respuesta 3"))

        return cards
    }
}