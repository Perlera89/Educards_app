package com.educards.adapter

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.educards.R
import com.educards.model.entities.Card
import com.educards.service.SCard
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout

class RecyclerCardAdapter(private var cards: MutableList<Card>, private var _btReverse:ImageButton, private var _context:Context, private val activity: Activity?) :
    RecyclerView.Adapter<RecyclerCardAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, item: Int): ViewHolder {
        val holder = LayoutInflater.from(viewGroup.context).inflate(R.layout.card_item, viewGroup, false)
        return ViewHolder(holder)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val card = cards[position]
        holder.bind(card)
        //holder.itemView.setOnClickListener { holder.cardAnimation()}
        holder.itemView.setOnTouchListener { view, motion ->
            holder.cardAnimation()
            true
        }
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
            private var deleteCard: ImageView
            var builder: android.app.AlertDialog.Builder

            private lateinit var tvEditCard: TextInputLayout
            private lateinit var cardHeader: TextView
            private lateinit var addCard: MaterialButton

            private lateinit var frontAnim: AnimatorSet
            private lateinit var backAnim: AnimatorSet
            private var isFront = true

            init {
                cardQuestion = view.findViewById(R.id.cv_question)
                cardAnswer = view.findViewById(R.id.cv_answer)

                deleteCard = view.findViewById(R.id.bt_delete_card)
                builder = android.app.AlertDialog.Builder(_context)

                question = view.findViewById(R.id.et_question)
                answer = view.findViewById(R.id.et_answer)
            }

            fun bind(card: Card){
                question.text = card.getQuestion()
                answer.text = card.getAnswer()

                val viewCard = activity?.layoutInflater?.inflate(R.layout.dialog_input_card, null)
                tvEditCard = viewCard!!.findViewById(R.id.titl_card)
                cardHeader = viewCard!!.findViewById(R.id.tv_header_card)
                addCard = viewCard!!.findViewById(R.id.bt_add_card)

                question.setOnClickListener {
                    cardQuestion?.isEnabled
                    cardAnswer?.isEnabled = false
                    question.selectionStart
                    val builder = AlertDialog.Builder(_context)
                    builder.setView(viewCard)

                    dialog = builder.create()
                    dialog.show()

                    addCard.setOnClickListener {
//                        TODO: Agregar pregunta
                        Toast.makeText(_context, "Se ha agregado la pregunta", Toast.LENGTH_SHORT).show()
                        dialog.hide()
                    }
                }

                answer.setOnClickListener {
                    cardAnswer?.isEnabled
                    cardQuestion?.isEnabled = false
                    answer.selectionStart
                    val builder = AlertDialog.Builder(_context)
                    tvEditCard.hint = _context.getString(R.string.dialog_buttom_answer)
                    cardHeader.text = "Add answer"
                    builder.setView(viewCard)

                    dialog = builder.create()
                    dialog.show()

                    addCard.setOnClickListener {
//                        TODO: Agregar respuesta
                        Toast.makeText(_context, "Se ha agregado la respuesta", Toast.LENGTH_SHORT).show()
                        dialog.hide()
                    }
                }

                deleteCard.setOnClickListener{
                    builder.setTitle("Confirm delete")
                        .setMessage("\nDo you want to remove card?")
                        .setCancelable(true)
                        .setPositiveButton("Yes"){dialogInterface, it ->
                           SCard.deleteCard(card.getId())
                        }
                        .setNegativeButton("No"){dialogInterface, it ->
                            dialogInterface.cancel()
                        }
                        .show()
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
                        cardAnswer?.visibility = View.VISIBLE
                        frontAnim.setTarget(cardQuestion)
                        backAnim.setTarget(cardAnswer)
                        frontAnim.start()
                        backAnim.start()

                        isFront = false
                    } else{
                        cardAnswer?.visibility = View.GONE
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