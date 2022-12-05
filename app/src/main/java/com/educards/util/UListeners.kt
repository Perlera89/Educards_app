package com.educards.util

import com.educards.service.FirebaseConnection
import com.educards.util.UIndexDeckOrCard.getSelectedDeckKey
import com.google.firebase.database.ValueEventListener

object UListeners {
    private var deckListener: ValueEventListener? = null
    private var cardListener:ValueEventListener? = null
    private var realTimeIndexDeck:ValueEventListener? = null
    private var realTimeIndexCardInSelectedDeck:ValueEventListener? = null

    fun setDeckListener(_deckListener:ValueEventListener){
        this.deckListener = _deckListener
    }
    fun setCardListener(_cardListener:ValueEventListener){
        this.cardListener = _cardListener
    }
    fun setRealTimeIndexDeck(_realTimeIndexDeck:ValueEventListener){
        this.realTimeIndexDeck = _realTimeIndexDeck
    }
    fun setRealTimeIndexCardInSelectedDeck(_realTimeIndexCardInSelectedDeck:ValueEventListener){
        this.realTimeIndexCardInSelectedDeck = _realTimeIndexCardInSelectedDeck
    }

    fun removeAllListeners(){
        val selectedDeckKey = getSelectedDeckKey()
        if (deckListener!=null) {
            FirebaseConnection.refGlobal.child("/decks").removeEventListener(deckListener!!)
        }
        if (cardListener!=null) {
            FirebaseConnection.refGlobal.child("/cards").child(selectedDeckKey).removeEventListener(cardListener!!)
        }
        if (realTimeIndexDeck != null) {
            FirebaseConnection.refGlobal.child("decks").removeEventListener(realTimeIndexDeck!!)
        }
        if (realTimeIndexCardInSelectedDeck != null) {
            FirebaseConnection.refGlobal.child("cards").child(selectedDeckKey).removeEventListener(realTimeIndexCardInSelectedDeck!!)
        }
    }

}