package com.educards.adapter

import android.app.Activity
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.ListAdapter
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.educards.R
import com.educards.activity.MainActivity
import com.educards.model.Deck
import com.educards.util.OnCardSelectListener
import org.w3c.dom.Text
import java.util.*
import kotlin.collections.ArrayList

class RecyclerViewAdapter(var decks: MutableList<Deck>) :
    RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, item: Int): ViewHolder {
        val holder = LayoutInflater.from(viewGroup.context).inflate(R.layout.deck_item, viewGroup, false)
        return ViewHolder(holder)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val deck = decks.get(position)
        holder.bind(deck)
    }

    override fun getItemCount(): Int {
        return decks.size
    }
        inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
            var title: TextView
            var countCards: TextView
            var favorite: ImageView
            var delete: ImageView

            init {
                title = itemView.findViewById(R.id.tv_title_deck)
                countCards = itemView.findViewById(R.id.tv_count_cards)
                favorite = itemView.findViewById(R.id.iv_favorite)
                delete = itemView.findViewById(R.id.iv_delete)
            }

            fun bind(deck: Deck){
                title.text = deck.title
                countCards.text = deck.count.toString()
                itemView.setOnClickListener(View.OnClickListener {
                    Toast.makeText(MainActivity(), "Carta", Toast.LENGTH_SHORT).show()
                })
            }
        }
}