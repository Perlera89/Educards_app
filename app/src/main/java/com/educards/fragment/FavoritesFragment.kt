package com.educards.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.educards.R
import com.educards.adapter.RecyclerDeckAdapter
import com.educards.model.Deck

class FavoritesFragment(var _favoriteDecks:ArrayList<Deck?>) : Fragment() {
    private lateinit var recyclerDeck: RecyclerView
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
        val adapter = RecyclerDeckAdapter(_favoriteDecks, context, activity)

        recyclerDeck.setHasFixedSize(true)
        recyclerDeck.layoutManager = LinearLayoutManager(context)
        recyclerDeck.adapter = adapter
    }

}