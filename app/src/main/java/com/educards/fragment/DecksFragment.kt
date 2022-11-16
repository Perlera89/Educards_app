package com.educards.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.educards.R
import com.educards.activity.MainActivity
import com.educards.adapter.RecyclerViewAdapter
import com.educards.util.OnCardSelectListener
import java.util.zip.Inflater

class DecksFragment : Fragment(), OnCardSelectListener {
    private lateinit var recyclerDeck: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_decks, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerDeck = view.findViewById(R.id.recycler_decks)
        val adapter = RecyclerViewAdapter(this)

        recyclerDeck.layoutManager = LinearLayoutManager(MainActivity())
        recyclerDeck.adapter = adapter
    }

    override fun onCardClick(position: Int) {
        when(position){

        }
    }
}