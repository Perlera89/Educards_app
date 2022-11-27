package com.educards.adapter

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.educards.R
import com.educards.model.entities.Card

class RecyclerCardAdapter(private var cards: MutableList<Card>, private var _btReverse:ImageButton, private var _context:Context, private val activity: Activity?) :
    RecyclerView.Adapter<RecyclerCardAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, item: Int): ViewHolder {
        val holder = LayoutInflater.from(viewGroup.context).inflate(R.layout.card_item, viewGroup, false)
        return ViewHolder(holder)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val card = cards[position]
        holder.bind(card)
        //holder.itemView.setOnClickListener { holder.cardAnimation()}
        holder.itemView.setOnTouchListener(object : View.OnTouchListener{
            override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
                holder.cardAnimation()
                return true
            }
        })
    }

    override fun getItemCount(): Int {
        return cards.size
    }

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
            private var question: TextView
            private var answer: TextView
            private var cardQuestion: CardView?
            private var cardAnswer: CardView?
            private lateinit var dialog: AlertDialog

            private var editQuestion: EditText
            private var editAnswer: EditText

            private lateinit var frontAnim: AnimatorSet
            private lateinit var backAnim: AnimatorSet
            private var isFront = true

            init {
                question = view.findViewById(R.id.et_question)
                answer = view.findViewById(R.id.et_answer)
                cardQuestion = view.findViewById(R.id.cv_question)
                cardAnswer = view.findViewById(R.id.cv_answer)
                editQuestion = view.findViewById(R.id.et_card_question)
                editAnswer = view.findViewById(R.id.et_card_answer)

            }

            fun bind(card: Card){
                question.text = card.getQuestion()
                answer.text = card.getAnswer()

                question.setOnClickListener {
                    val builder = AlertDialog.Builder(_context)
                    val viewDialog = activity?.layoutInflater?.inflate(R.layout.dialog_input_question, null)
                    builder.setView(viewDialog)

                    dialog = builder.create()
                    dialog.show()
                }
            }

            fun cardAnimation(){
                val scale = _context.resources.displayMetrics.density
                cardQuestion?.cameraDistance = 8000 * scale
                cardAnswer?.cameraDistance = 8000 * scale

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