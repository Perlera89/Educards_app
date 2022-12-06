package com.educards.adapter

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.educards.R
import com.educards.activity.DeckActivity
import com.educards.model.Deck
import com.educards.service.SDeck
import com.educards.util.UIndexDeckOrCard.realTimeIndexCardInSelectedDeck
import com.educards.util.UIndexDeckOrCard.setSelectedDeckKey
import com.google.android.material.snackbar.Snackbar
/*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue

 */

class RecyclerDeckAdapter(private var decks: MutableList<Deck?>, val context: Context?, val activity: Activity?) :
    RecyclerView.Adapter<RecyclerDeckAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, item: Int): ViewHolder {
        val holder = LayoutInflater.from(viewGroup.context).inflate(R.layout.deck_item, viewGroup, false)
        return ViewHolder(holder, context, activity)
    }

    var positionItem = 0
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (decks.size > 0){
            val deck = decks[position]
            holder.bind(deck)
        }
    }

    override fun getItemCount(): Int {
        return decks.size
    }

    fun showAlertDialog(){
    }
        inner class ViewHolder(view: View, context: Context?, activity: Activity?): RecyclerView.ViewHolder(view){
            var title: TextView
            var index: TextView
            var countCards: TextView
            var favorite: ImageView
            var delete: ImageView
            var builder: AlertDialog.Builder
            var deckItem: CardView

            init {
                title = view.findViewById(R.id.tv_title_deck)
                index = view.findViewById(R.id.tv_card_index)
                countCards = view.findViewById(R.id.tv_count_cards)
                favorite = view.findViewById(R.id.iv_favorite)
                delete = view.findViewById(R.id.iv_delete)
                deckItem = view.findViewById(R.id.cardDeck)
                builder = AlertDialog.Builder(context)
            }

            fun bind(deck: Deck?){
                title.text = deck?.getTitle()
                if(title.text.length > 20){
                    title.text = title.text.substring(0, 18) + "..."
                }
                index.text = deck!!.getTitle().first().uppercaseChar().toString()
                if (adapterPosition>0) {
                    if (index.text == decks[adapterPosition - 1]!!.getTitle().first().uppercaseChar().toString()) {
                        index.alpha = 0f
                    }
                }
                countCards.text = deck.getCount().toString()

                if (deck.getIsFavorite()){
                    favorite.setImageResource(R.drawable.ic_favorite_select)
                }else{
                    favorite.setImageResource(R.drawable.ic_favorite)
                }

                delete.setOnClickListener{
                    builder.setTitle("Confirm delete")
                        .setMessage("\nDo you want to remove ${deck.getTitle()} deck?" +
                                "\nThis action is not reversible; if you confirm the action, " +
                                "the deck will be permanently deleted")
                        .setCancelable(true)
                        .setPositiveButton("Yes"){dialogInterface, it ->
                            if(deck != null){
                                setSelectedDeckKey(deck.getId())
                                positionItem = layoutPosition
                                SDeck.deleteDeck(context!!,deck.getId())
                                adapterPosition.plus(-1)
                            }
                        }
                        .setNegativeButton("No"){dialogInterface, it ->
                            dialogInterface.cancel()
                        }
                        .setNeutralButton("Cancel"){dialogInterface, it ->
                            dialogInterface.cancel()
                        }
                        .show()
                }

                favorite.setOnClickListener{
                    var message = ""
                    if(deck!=null){
                        if (deck.getIsFavorite()){
                            deck.setIsFavorite(false)
                            SDeck.updateDeck(context!!,deck,"favorite status to false")
                            message ="${deck.getTitle()} deck remove to favorites"
                        }else{
                            deck.setIsFavorite(true)
                            SDeck.updateDeck(context!!,deck,"favorite status to true")
                            message = "${deck.getTitle()} deck move to favorites"
                        }
                        val snackBar = Snackbar.make(activity!!.findViewById(R.id.cl_main),message, Snackbar.LENGTH_LONG)
                        snackBar.show()
                    }
                }
                deckItem.setOnClickListener{
                    if(deck != null && context != null){
                        //se guarda el nuevo mazo seleccionado
                        setSelectedDeckKey(deck.getId())
                        //se ejecuta el metodo para obtener el numero de cards en el mazo y la llave(index) del
                        //la ultima tarjeta agregada al mazo
                        realTimeIndexCardInSelectedDeck()
                        val intentCard = Intent(context, DeckActivity::class.java)
                        intentCard.putExtra("idDeck",deck.getId())
                        intentCard.putExtra("title",deck.getTitle())
                        intentCard.putExtra("description",deck.getDescription())
                        intentCard.putExtra("isFavorite",deck.getIsFavorite())
                        intentCard.putExtra("count",deck.getCount())
                        startActivity(context,intentCard, Bundle())
                    }
                }
            }
        }
}