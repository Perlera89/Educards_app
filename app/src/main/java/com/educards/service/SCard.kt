package com.educards.service

import android.util.Log
import com.educards.model.Deck
import com.educards.model.entities.Card
import com.educards.util.IndexDeckOrCard

object SCard {
    var refGlobal = FirebaseConnection.firebaseRealTimeDB.getReference("${SUser.getCurrentUserDetailData().getIdUser()}_data")

    fun saveCard(_card: Card) {
        _card.id = "${IndexDeckOrCard.lastKeyCardDeckSelected?.toInt()?.plus(1)}"
        refGlobal.child("/cards/${IndexDeckOrCard.selectedDeckKey}/${_card.id}").setValue(_card)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Log.d("service/SCard", "Card agregado exitosamente al mazo de id <${IndexDeckOrCard.selectedDeckKey}>")
                    //Actualizar el numero de cards en el dec
                    var deck = Deck()
                    deck.setId(IndexDeckOrCard.selectedDeckKey)
                    SDeck.updateCountInDeck(deck,1)
                }
            }.addOnFailureListener {
                Log.d("service/SCard", "Error al agregar card al mazo de id <${IndexDeckOrCard.selectedDeckKey}, Detalles: \n $it")
            }
    }
    fun updateCard(_card: Card) {
        //agregar el key en el id del card
        val card = mapOf(
            "id" to _card.id,
            "question" to _card.question,
            "answer" to _card.answer
        )
        refGlobal.child("/cards/${IndexDeckOrCard.selectedDeckKey}/${_card.id}").updateChildren(card)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Log.d("service/SCard", "Card de id<${_card.id}> actualizado exitosamente en el mazo de id <${IndexDeckOrCard.selectedDeckKey}>")
                }
            }.addOnFailureListener {
                Log.d("service/SCard", "Error al actualizar card de id<${_card.id}> en el mazo de id <${IndexDeckOrCard.selectedDeckKey}, Detalles: \n $it")
            }
    }
    fun deleteCard(_cardId: String) {
        //agregar el key en el id del card
        refGlobal.child("/cards/${IndexDeckOrCard.selectedDeckKey}/${_cardId}").removeValue()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Log.d("service/SCard", "Card de id<${_cardId}> eliminado exitosamente en el mazo de id <${IndexDeckOrCard.selectedDeckKey}>")
                    //se disminuye el contador de cards del mazo
                    var deck = Deck()
                    deck.setId(IndexDeckOrCard.selectedDeckKey)
                    SDeck.updateCountInDeck(deck,-1)
                }
            }.addOnFailureListener {
                Log.d("service/SCard", "Error al eliminar card de id<${_cardId}> en el mazo de id <${IndexDeckOrCard.selectedDeckKey}, Detalles: \n $it")
            }
    }
    fun deleteAllCards() {
        //agregar el key en el id del card
        refGlobal.child("/cards/${IndexDeckOrCard.selectedDeckKey}").removeValue()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Log.d("service/SCard", "Todos los cards se han eliminado exitosamente en el mazo de id <${IndexDeckOrCard.selectedDeckKey}>")
                }
            }.addOnFailureListener {
                Log.d("service/SCard", "Error al eliminar todos los cards en el mazo de id <${IndexDeckOrCard.selectedDeckKey}, Detalles: \n $it")
            }
    }
}