package com.educards.adapter

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.educards.R
import com.educards.model.Deck
import com.educards.model.entities.Card
import com.google.android.material.snackbar.Snackbar

class RecyclerCardAdapter(private var cards: MutableList<Card>) :
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

    fun showAlertDialog(){}
        inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
            var id: Int = 0
            var question: TextView
            var answer: TextView

            init {
                question = view.findViewById(R.id.et_question)
                answer = view.findViewById(R.id.et_answer)
            }

            fun bind(card: Card){
//                itemView.id = card.id
                question.text = card.question
                answer.text = card.answer
            }

        }
}