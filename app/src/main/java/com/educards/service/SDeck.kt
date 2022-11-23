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
        _deck.id = IndexDeckOrCard.lastKeyDeck?.toInt()?.plus(1).toString()
        refGlobal.child("/decks/${_deck.id}").setValue(_deck)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Log.d("service/RSDecks", "Mazo agregado exitosamente")
                    IndexDeckOrCard.selectedDeckKey = _deck.id!!
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
            "id" to _deck.id,
            "title" to _deck.title,
            "description" to _deck.description,
            "isFavorite" to _deck.isFavorite,
            "count" to _deck.count
        )
        refGlobal.child("/decks/${_deck.id}").updateChildren(deck)
            .addOnSuccessListener {
                Log.d("RSDeck","El deck de id <${_deck.id}> se ha actualizado con éxito")
            }.addOnFailureListener{
                Log.d("RSDeck","Error al actualizar el deck de id <${_deck.id}> . Detalles: \n ${it}")
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

    fun updateCountInDeck(_deckId: String,_incrementValue:Long){
        //agregar el id del deck en la clase
        //var selectedDeck = IndexDeckOrCard.selectedDeckKey

        refGlobal.child("/decks/${_deckId}").child("count").setValue(ServerValue.increment(_incrementValue))
            .addOnSuccessListener {
                Log.d("RSDeck","El contador del deck de id <${_deckId}> se ha actualizado con éxito")
            }.addOnFailureListener{
                Log.d("RSDeck","Error al actualizar el contador del deck de id <${_deckId}> . Detalles: \n ${it}")
            }
    }
}