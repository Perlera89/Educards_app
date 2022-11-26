package com.educards.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.educards.R
import com.educards.model.Deck
import com.educards.service.FirebaseConnection
import com.google.android.material.snackbar.Snackbar
/*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue

 */

class RecyclerDeckAdapter(private var decks: ArrayList<Deck?>, val context: Context?, val activity: Activity?) :
    RecyclerView.Adapter<RecyclerDeckAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, item: Int): ViewHolder {
        val holder = LayoutInflater.from(viewGroup.context).inflate(R.layout.deck_item, viewGroup, false)
        return ViewHolder(holder, context, activity)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val deck = decks[position]
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

            fun bind(deck: Deck?){
                title.text = deck?.getTitle()
                countCards.text = deck?.getCount().toString()

                delete.setOnClickListener{
                    builder.setTitle("Confirm to delete")
                        .setMessage("\nDo you want to remove ${deck?.getTitle()} deck?")
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
                    val snackBar = Snackbar.make(activity!!.findViewById(R.id.cl_main), "${deck?.getTitle()} deck move to favorites", Snackbar.LENGTH_LONG)
                    snackBar.show()
                }
            }
        }
}