package com.educards.service

import android.content.Context
import android.widget.Toast
import com.educards.model.Deck
import com.educards.model.entities.Card
import com.educards.service.FirebaseConnection.refGlobal
import com.educards.util.UAlertGenericDialog.createDialogAlert
import com.educards.util.UIndexDeckOrCard.getLastKeyCardDeckSelected
import com.educards.util.UIndexDeckOrCard.getSelectedDeckKey

object SCard {

    fun saveCard(_context: Context, _card: Card) {
        //la llave de la rama padre de las tarjetas y el mazo es la misma
        val indexCardKey = getSelectedDeckKey()
        refGlobal.child("/cards/${indexCardKey}/${_card.getId()}")
            .setValue(_card)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(_context,"Card added to deck successfully",Toast.LENGTH_SHORT).show()
                    //Actualizar el numero de cards en el mazo
                    if (getLastKeyCardDeckSelected() != null){
                        SDeck.updateCountInDeck(_context,Deck(getSelectedDeckKey()))
                    }
                }
            }.addOnFailureListener {
                createDialogAlert(_context,"Card","Failed to add card to deck.\nDetails: $it")
            }
    }
    fun updateCard(_context: Context,_card: Card,_type:String) {
        val card = mapOf(
            "id" to _card.getId(),
            "question" to _card.getQuestion(),
            "answer" to _card.getAnswer()
        )
        //la llave de la rama padre de las tarjetas
        val fatherIndexCardKey = getSelectedDeckKey() //mismo llave del mazo
        refGlobal.child("/cards/${fatherIndexCardKey}/${_card.getId()}")
            .updateChildren(card)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(_context, "The $_type has been updated successfully",Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener {
                createDialogAlert(_context,"Card","Failed to update card in deck.\nDetails: $it")
            }
    }
    fun deleteCard(_context: Context,_cardId: String) {
        //la llave de la rama padre de las tarjetas
        val fatherIndexCardKey = getSelectedDeckKey() //misma key del mazo
        refGlobal.child("/cards/${fatherIndexCardKey}/${_cardId}")
            .removeValue()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(_context, "Card successfully removed",Toast.LENGTH_SHORT).show()
                    //se disminuye el contador de tarjetas del mazo
                    SDeck.updateCountInDeck(_context,Deck(getSelectedDeckKey()))
                }
            }.addOnFailureListener {
                createDialogAlert(_context,"Card","Failed to remove card from deck.\nDetails: $it")
            }
    }
    fun deleteAllCards(_context: Context) {
        //la llave de la rama padre de las tarjetas
        val fatherIndexCardKey = getSelectedDeckKey() //mismo index del mazo
        refGlobal.child("/cards/${fatherIndexCardKey}")
            .removeValue()
            .addOnFailureListener {
                createDialogAlert(_context,"Card","Failed to remove all cards from the deck.\nDetails: $it")
            }
    }
}