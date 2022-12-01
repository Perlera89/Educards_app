package com.educards.activity

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.educards.R
import com.educards.adapter.RecyclerCardAdapter
import com.educards.model.entities.Card
import com.educards.service.FirebaseConnection
import com.educards.util.IndexDeckOrCard
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.google.firestore.admin.v1.Index

@Suppress("DEPRECATION")
class EstudyActivity : AppCompatActivity() {
    private lateinit var recyclerCard: RecyclerView
    private lateinit var tvStudyTitle:TextView
    private lateinit var tvCounter:TextView
    private lateinit var questionCard:CardView
    private lateinit var answerCard: CardView
    private lateinit var tvQuestion:TextView
    private lateinit var tvAnswer: TextView
    private lateinit var btReverse:FloatingActionButton
    private lateinit var btNext: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_estudy)
        tvStudyTitle = findViewById(R.id.tv_study_title)
        tvCounter = findViewById(R.id.tv_counter_study_cards)
        questionCard = findViewById(R.id.cv_question)
        answerCard = findViewById(R.id.cv_answer)
        tvQuestion = findViewById(R.id.et_question)
        tvAnswer = findViewById(R.id.et_answer)
        btReverse = findViewById(R.id.fab_flip)
        btNext = findViewById(R.id.fab_next)

        tvStudyTitle.text = this.intent.extras?.getString("title")

        getCards()
        cardAnimation()
        initStudy()
    }

    var cardsData = ArrayList<Card>()
    var cardsRandom = ArrayList<Card>()

    private fun getCards(){

        FirebaseConnection.refGlobal.child("/cards").child("${IndexDeckOrCard.selectedDeckKey}")
            .get().addOnSuccessListener {
                if (it != null){
                    it.children.forEach{
                        it.getValue<Card>()?.let { it1 -> cardsData.add(it1) }
                    }
                    generateRandomCardsArray()
                }
            }.addOnFailureListener{
                Log.d("EstudyActivity", "Error al mostrar las tarjetas del mazo seleccionado. ${it}")

            }
    }

    private lateinit var frontAnim: AnimatorSet
    private lateinit var backAnim: AnimatorSet
    private var isFront = true

    fun cardAnimation(){
        val scale = this.resources.displayMetrics.density
        questionCard.cameraDistance = 8000 * scale
        answerCard.cameraDistance = 8000 * scale

        frontAnim = AnimatorInflater.loadAnimator(this, R.animator.front_animation) as AnimatorSet
        backAnim = AnimatorInflater.loadAnimator(this, R.animator.back_animation) as AnimatorSet

        btReverse.setOnClickListener{
            if(isFront){
                answerCard.visibility = View.VISIBLE
                frontAnim.setTarget(questionCard)
                backAnim.setTarget(answerCard)
                frontAnim.start()
                backAnim.start()

                isFront = false
            } else{
                answerCard.visibility = View.GONE
                frontAnim.setTarget(answerCard)
                backAnim.setTarget(questionCard)
                backAnim.start()
                frontAnim.start()

                isFront = true
            }
        }
    }

    private fun generateRandomCardsArray(){
        var cardRandom = Card()
        if(cardsData.size > 0){
            for(i in 1..cardsData.size){
                cardRandom = cardsData.random()
                cardsRandom.add(cardRandom)
                cardsData.remove(cardRandom)
            }
            initCard()
            cardsRandom.forEach{
                println(it.getId())
            }
        }
    }

    private fun initCard(){
        if (cardsRandom.size > 0){
            tvQuestion.text = cardsRandom[0].getQuestion()
            tvAnswer.text = cardsRandom[0].getAnswer()
            tvCounter.text = "1/${cardsRandom.size}"
        }
    }

    private fun initStudy(){
        var indexCardShow = 0
        btNext.setOnClickListener{
            if (indexCardShow < cardsRandom.size-1){
                indexCardShow++
                tvQuestion.text = cardsRandom[indexCardShow].getQuestion()
                tvAnswer.text = cardsRandom[indexCardShow].getAnswer()
                tvCounter.text = "${indexCardShow+1}/${cardsRandom.size}"
            }
        }
    }
}