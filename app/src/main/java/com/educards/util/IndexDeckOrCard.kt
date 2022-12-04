package com.educards.util

import android.util.Log
import com.educards.service.SUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firestore.v1.StructuredQuery.Order

object IndexDeckOrCard {
    private val firebaseDB = FirebaseDatabase.getInstance()
    var refGlobal = firebaseDB.getReference("${SUser.getCurrentUserDetailData().getIdUser()}_data")

    //ultimo key de los deck del usuario
    var lastKeyDeck:String?="0"
    //ultimo key del card de un deck seleccionado
    var lastKeyCardDeckSelected:String?=""
    //deck seleccionado en un momento determinado
    var selectedDeckKey:String = "0"
    //contiene la cantidad de cards en el deck seleccionado
    var itemsCardInDeckSelected = 0

    fun realTimeIndexDeck(){
        if (!SUser.getCurrentUserDetailData().getIdUser().isEmpty()) {
            refGlobal.child("decks").orderByKey().addValueEventListener(postListenerDeck)
        }
    }
    fun realTimeIndexCardInSelectedDeck(){
        if (!SUser.getCurrentUserDetailData().getIdUser().isEmpty()) {
            refGlobal.child("cards").child(selectedDeckKey).orderByKey().addValueEventListener(postListenerCard)
        }
    }
    val postListenerDeck = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            // Get Post object and use the values to update the UI
            //val post = dataSnapshot.value
            /*dataSnapshot.children.forEach {
                println()
            }*/
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
    val postListenerCard = object : ValueEventListener {
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
            }
        }

        override fun onCancelled(databaseError: DatabaseError) {
            // Getting Post failed, log a message
            Log.w("RSDeck", "loadPost:onCancelled", databaseError.toException())
        }
    }

}