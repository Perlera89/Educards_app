package com.educards.util

import com.educards.service.FirebaseConnection
import com.google.firebase.database.ValueEventListener

object Listeners {
    var deckListener: ValueEventListener? = null
    var cardListener:ValueEventListener? = null
    var realTimeIndexDeck:ValueEventListener? = null
    var realTimeIndexCardInSelectedDeck:ValueEventListener? = null

    fun removeAllListeners(){
        val selectedDeckKey = IndexDeckOrCard.selectedDeckKey
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