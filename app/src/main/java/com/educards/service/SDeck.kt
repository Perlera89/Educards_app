package com.educards.service

import android.util.Log
import com.educards.model.Deck
import com.educards.model.entities.Card
import com.educards.util.IndexDeckOrCard
import com.google.firebase.database.ServerValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object SDeck {
    var refGlobal = FirebaseConnection.firebaseRealTimeDB.getReference("${SUser.getCurrentUserDetailData().getIdUser()}_data")
    fun saveDeck(_deck: Deck) {
        //agregamos un indice para ordenar los datos
        _deck.setId(IndexDeckOrCard.lastKeyDeck?.toInt()?.plus(1).toString())
        refGlobal.child("/decks/${_deck.getId()}").setValue(_deck)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Log.d("service/RSDecks", "Mazo agregado exitosamente")
                    IndexDeckOrCard.selectedDeckKey = _deck.getId()
                    IndexDeckOrCard.realTimeIndexCardInSelectedDeck()
                    CoroutineScope(Dispatchers.IO).launch{
                        Thread.sleep(1000)
                        SCard.saveCard(Card("","Click to edit this question","Click to edit this answer"))
                    }
                }
            }.addOnFailureListener {
                Log.d("service/RSDecks", "Error al agregar mazo, Detalles: \n $it")
            }
    }

    fun updateDeck(_deck: Deck){
        //agregar el id del deck en la clase
        //var selectedDeck = IndexDeckOrCard.selectedDeckKey
        val deck = mapOf(
            "id" to _deck.getId(),
            "title" to _deck.getTitle(),
            "description" to _deck.getDescription(),
            "isFavorite" to _deck.getIsFavorite(),
            "count" to _deck.getCount()
        )
        refGlobal.child("/decks/${_deck.getId()}").updateChildren(deck)
            .addOnSuccessListener {
                Log.d("RSDeck","El deck de id <${_deck.getId()}> se ha actualizado con éxito")
            }.addOnFailureListener{
                Log.d("RSDeck","Error al actualizar el deck de id <${_deck.getId()}> . Detalles: \n ${it}")
            }
    }

    fun deleteDeck(_deckId: String){
        //agregar el id del deck en la clase
        //var selectedDeck = IndexDeckOrCard.selectedDeckKey
        refGlobal.child("/decks/${_deckId}").removeValue()
            .addOnSuccessListener {
                Log.d("RSDeck","El deck de id <${_deckId}> se ha eliminado con éxito")
                //se borran las cards que tengan el mismo id del deck
                SCard.deleteAllCards()
            }.addOnFailureListener{
                Log.d("RSDeck","Error al eliminar el deck de id <${_deckId}> . Detalles: \n ${it}")
            }
    }

    fun updateCountInDeck(_deck: Deck){
        //agregar el id del deck en la clase
        refGlobal.child("/decks/${_deck.getId()}").child("count").setValue(IndexDeckOrCard.itemsCardInDeckSelected)
            .addOnSuccessListener {
                Log.d("RSDeck","El contador del deck de id <${_deck.getId()}> se ha actualizado con éxito")
            }.addOnFailureListener{
                Log.d("RSDeck","Error al actualizar el contador del deck de id <${_deck.getId()}> . Detalles: \n ${it}")
            }
    }
}