package com.educards.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.educards.R
import com.educards.adapter.RecyclerDeckAdapter
import com.educards.model.Deck

class DecksFragment(var _decks: ArrayList<Deck?>) : Fragment() {
    private lateinit var recyclerDeck: RecyclerView
    private lateinit var emptyDecks: CardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_decks, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerDeck = view.findViewById(R.id.recycler_decks)
        emptyDecks = view.findViewById(R.id.empty_deck)
        val adapter = RecyclerDeckAdapter(getDecks().sortedWith(compareBy { it?.getTitle() }) as MutableList<Deck?>, context, activity)

        if(_decks.size > 0){
            recyclerDeck.setHasFixedSize(true)
            recyclerDeck.layoutManager = LinearLayoutManager(context)
            recyclerDeck.adapter = adapter
        } else {
            recyclerDeck.alpha = 0f
            emptyDecks.alpha = 1f
        }
    }

    fun getDecks(): MutableList<Deck?>{
        val decks = mutableListOf<Deck?>()
        decks.add(Deck("1","Programacion","Una descripcion",false,6))
//        decks.add(Deck("2","Mercadeo","Una descripcion",false,6))
//        decks.add(Deck("3","Fisica","Una descripcion",false,12))
//        decks.add(Deck("3","Filosofia con mucho texto","Una descripcion",false,12))
//        decks.add(Deck("4","Matematicas","Una descripcion",false,5))
//        decks.add(Deck("5","Metodologia","Una descripcion",false,5))

        return decks
    }
}