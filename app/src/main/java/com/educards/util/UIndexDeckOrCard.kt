package com.educards.util

import android.util.Log
import com.educards.service.FirebaseConnection.refGlobal
import com.educards.service.SUser
import com.educards.util.UListeners.setCardListener
import com.educards.util.UListeners.setRealTimeIndexCardInSelectedDeck
import com.educards.util.UListeners.setRealTimeIndexDeck
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

object UIndexDeckOrCard {

    //ultimo key de los deck del usuario
    private var lastKeyDeck:String?="0"
    //ultimo key del card de un deck seleccionado
    private var lastKeyCardDeckSelected:String?="0"
    //deck seleccionado en un momento determinado
    private var selectedDeckKey:String = "0"
    //contiene la cantidad de cards en el deck seleccionado
    private var itemsCardInDeckSelected = 0

    fun getLastKeyDeck():String?{
        return this.lastKeyDeck
    }
    fun getLastKeyCardDeckSelected():String?{
        return this.lastKeyCardDeckSelected
    }
    fun getSelectedDeckKey():String{
        return this.selectedDeckKey
    }
    fun setSelectedDeckKey(_deckSelectedKey:String){
        this.selectedDeckKey = _deckSelectedKey
        realTimeIndexCardInSelectedDeck()
    }
    fun getItemsCardInDeckSelected():Int{
        return this.itemsCardInDeckSelected
    }

    fun realTimeIndexDeck(){
        if (!SUser.getCurrentUserDetailData().getIdUser().isEmpty()) {
            refGlobal.child("decks").orderByKey()
                .addValueEventListener(listenerDeck)
            setRealTimeIndexDeck(listenerDeck)
        }
    }
    fun realTimeIndexCardInSelectedDeck(){
        if (!SUser.getCurrentUserDetailData().getIdUser().isEmpty()) {
            refGlobal.child("cards").child(selectedDeckKey).orderByKey()
                .addValueEventListener(listenerCard)
            setRealTimeIndexCardInSelectedDeck(listenerCard)
        }
    }
    val listenerDeck = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            if (dataSnapshot.hasChildren()) {
                lastKeyDeck = dataSnapshot.children.last().key
                //println("LLAVE DEL ULTIMO MAZO: $lastKeyDeck")
            }else{
                lastKeyDeck = "0"
                //println("LLAVE DEL ULTIMO MAZO: $lastKeyDeck")
            }
        }

        override fun onCancelled(databaseError: DatabaseError) {
            // Getting Post failed, log a message
            Log.w("RSDeck", "loadPost:onCancelled", databaseError.toException())
        }
    }
    val listenerCard = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            // Get Post object and use the values to update the UI
            //val post = dataSnapshot.value
            if (!selectedDeckKey.isNullOrEmpty()){
                if(dataSnapshot.hasChildren() && dataSnapshot.hasChildren()){
                    lastKeyCardDeckSelected = dataSnapshot.children.last().key
                    itemsCardInDeckSelected = dataSnapshot.children.count()
                    //println("LLAVE DE LA ULTIMA TARJETA DEL DECK <$selectedDeckKey>: $lastKeyCardDeckSelected")
                }else{
                    lastKeyCardDeckSelected = "0"
                    itemsCardInDeckSelected = dataSnapshot.children.count()
                    //println("LLAVE DE LA ULTIMA TARJETA DEL DECK <$selectedDeckKey>: $lastKeyCardDeckSelected")
                }
                println("Last card key: $lastKeyCardDeckSelected y numero de cartas en el mazo: $itemsCardInDeckSelected,")
            }
        }

        override fun onCancelled(databaseError: DatabaseError) {
            // Getting Post failed, log a message
            Log.w("RSDeck", "loadPost:onCancelled", databaseError.toException())
        }
    }

}