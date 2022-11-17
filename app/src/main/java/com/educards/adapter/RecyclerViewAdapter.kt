package com.educards.adapter

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.educards.R
import com.educards.model.Deck
import com.google.android.material.snackbar.Snackbar

class RecyclerViewAdapter(var decks: MutableList<Deck>, val context: Context?, val activity: Activity?) :
    RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, item: Int): ViewHolder {
        val holder = LayoutInflater.from(viewGroup.context).inflate(R.layout.deck_item, viewGroup, false)
        return ViewHolder(holder, context, activity)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val deck = decks.get(position)
        holder.bind(deck)
    }

    override fun getItemCount(): Int {
        return decks.size
    }

    fun showAlertDialog(){

    }
        inner class ViewHolder(view: View, context: Context?, activity: Activity?): RecyclerView.ViewHolder(view){
            var title: TextView
            var countCards: TextView
            var favorite: ImageView
            var delete: ImageView
            var builder: AlertDialog.Builder

            init {
                title = view.findViewById(R.id.tv_title_deck)
                countCards = view.findViewById(R.id.tv_count_cards)
                favorite = view.findViewById(R.id.iv_favorite)
                delete = view.findViewById(R.id.iv_delete)
                builder = AlertDialog.Builder(context)
            }

            fun bind(deck: Deck){
                title.text = deck.title
                countCards.text = deck.count.toString()

                delete.setOnClickListener{
                    builder.setTitle("Confirm to delete")
                        .setMessage("\nDo you want to remove ${deck.title} deck?")
                        .setCancelable(true)
                        .setPositiveButton("Yes"){dialogInterface, it ->
//                          TODO: Delete deck
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
//                    TODO: Move to favorites
                    val snackBar = Snackbar.make(activity!!.findViewById(R.id.cl_main), "${deck.title} deck move to favorites", Snackbar.LENGTH_LONG)
                    snackBar.show()
                }
            }
        }
}