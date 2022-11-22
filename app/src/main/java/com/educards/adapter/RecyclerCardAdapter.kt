package com.educards.adapter

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.educards.R
import com.educards.model.Deck
import com.educards.model.entities.Card
import com.google.android.material.snackbar.Snackbar

class RecyclerCardAdapter(private var cards: MutableList<Card>, private var _context:Context,private var _btReverse:ImageButton) :
    RecyclerView.Adapter<RecyclerCardAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, item: Int): ViewHolder {
        val holder = LayoutInflater.from(viewGroup.context).inflate(R.layout.card_item, viewGroup, false)
        return ViewHolder(holder)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val card = cards[position]
        holder.bind(card)
    }

    override fun getItemCount(): Int {
        return cards.size
    }

    override fun onViewAttachedToWindow(holder: ViewHolder) {
        super.onViewAttachedToWindow(holder)
        holder.cardAnimation()
    }

        inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
            private var question: TextView
            private var answer: TextView
            private var cardQuestion: CardView
            private var cardAnswer: CardView

            private lateinit var frontAnim: AnimatorSet
            private lateinit var backAnim: AnimatorSet
            private var isFront = true

            init {
                question = view.findViewById(R.id.et_question)
                answer = view.findViewById(R.id.et_answer)
                cardQuestion = view.findViewById(R.id.cv_question)
                cardAnswer = view.findViewById(R.id.cv_answer)
            }

            fun bind(card: Card){
                question.text = card.question
                answer.text = card.answer
            }
            fun cardAnimation(){
                val scale = _context.resources.displayMetrics.density
                cardQuestion.cameraDistance = 8000 * scale
                cardAnswer.cameraDistance = 8000 * scale

                frontAnim = AnimatorInflater.loadAnimator(_context, R.animator.front_animation) as AnimatorSet
                backAnim = AnimatorInflater.loadAnimator(_context, R.animator.back_animation) as AnimatorSet

                _btReverse.setOnClickListener{
                    if(isFront){
                        frontAnim.setTarget(cardQuestion)
                        backAnim.setTarget(cardAnswer)
                        frontAnim.start()
                        backAnim.start()

                        isFront = false
                    } else{
                        frontAnim.setTarget(cardAnswer)
                        backAnim.setTarget(cardQuestion)
                        backAnim.start()
                        frontAnim.start()

                        isFront = true
                    }
                }
            }

        }
}