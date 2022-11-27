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

class FavoritesFragment(var _favoriteDecks:ArrayList<Deck?>) : Fragment() {
    private lateinit var recyclerFavoriteDeck: RecyclerView
    private lateinit var emptyFavoriteDecks: CardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_favorites, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerFavoriteDeck = view.findViewById(R.id.recycler_favorite_decks)
        emptyFavoriteDecks = view.findViewById(R.id.empty_favorite_deck)
        val adapter = RecyclerDeckAdapter(_favoriteDecks, context, activity)

        if(_favoriteDecks.size > 0){
            recyclerFavoriteDeck.setHasFixedSize(true)
            recyclerFavoriteDeck.layoutManager = LinearLayoutManager(context)
            recyclerFavoriteDeck.adapter = adapter
        } else {
            recyclerFavoriteDeck.alpha = 0f
            emptyFavoriteDecks.alpha = 1f
        }
    }

}