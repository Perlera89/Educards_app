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
<<<<<<< Updated upstream
=======
        tvStudyTitle = findViewById(R.id.tv_study_title)
        tvCounter = findViewById(R.id.tv_counter_study_cards)
        questionCard = findViewById(R.id.cv_question)
        answerCard = findViewById(R.id.cv_answer)
        tvQuestion = findViewById(R.id.et_question)
        tvAnswer = findViewById(R.id.et_answer)
        btReverse = findViewById(R.id.fab_flip)
        btNext = findViewById(R.id.fab_next)
        btDone = findViewById(R.id.fab_done)

        btnDeleteCard = findViewById(R.id.bt_delete_card)
        btnMore = findViewById(R.id.bt_more)
        //ocultado los elementos no requeridos
        btnDeleteCard.alpha = 0F
        btnMore.alpha = 0F

        tvStudyTitle.text = this.intent.extras?.getString("title")

        getCards()
        initStudy()
    }

    var cardsData = ArrayList<Card>()
    var cardsRandom = ArrayList<Card>()

    private fun getCards(){
        val selectDeckKey = getSelectedDeckKey()

        FirebaseConnection.refGlobal.child("/cards").child("$selectDeckKey")
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
    private fun cardAnimation(){
        val scale = this.resources.displayMetrics.density
        questionCard.cameraDistance = 8000 * scale
        answerCard.cameraDistance = 8000 * scale

        frontAnim = AnimatorInflater.loadAnimator(this, R.animator.front_animation) as AnimatorSet
        backAnim = AnimatorInflater.loadAnimator(this, R.animator.back_animation) as AnimatorSet

        if(isFront) {
            answerCard.visibility = View.VISIBLE
            frontAnim.setTarget(questionCard)
            backAnim.setTarget(answerCard)
            frontAnim.start()
            backAnim.start()

            isFront = false
        } else {
            answerCard.visibility = View.GONE
            frontAnim.setTarget(answerCard)
            backAnim.setTarget(questionCard)
            backAnim.start()
            frontAnim.start()

            isFront = true
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
        }
    }

    private fun initCard(){
        if (cardsRandom.size > 0){
            tvQuestion.text = cardsRandom[0].getQuestion()
            UTextView.adjustTextInTextView(tvQuestion)
            tvAnswer.text = cardsRandom[0].getAnswer()
            UTextView.adjustTextInTextView(tvAnswer)
            tvCounter.text = "1/${cardsRandom.size}"
        }
    }

    private fun initStudy(){
        var indexCardShow = 0
        btNext.setOnClickListener{
            if (indexCardShow < cardsRandom.size-1){
                indexCardShow++
                //si isFront = false --> la tarjeta esta mostrado una respuesta
                if (!isFront){
                    //si se esta mostrando una respuesta y da click en siguiente, se gira la tarjeta y se muestra la pregunta
                    cardAnimation()
                }
                tvQuestion.text = cardsRandom[indexCardShow].getQuestion()
                UTextView.adjustTextInTextView(tvQuestion)
                tvAnswer.text = cardsRandom[indexCardShow].getAnswer()
                UTextView.adjustTextInTextView(tvAnswer)
                tvCounter.text = "${indexCardShow+1}/${cardsRandom.size}"
            }

            if(indexCardShow+1 == cardsRandom.size){
                btNext.visibility = View.GONE
                btDone.visibility = View.VISIBLE
            }
        }
        btReverse.setOnClickListener{
            cardAnimation()
        }
        btDone.setOnClickListener{
            this.finish()
        }
>>>>>>> Stashed changes
    }

}