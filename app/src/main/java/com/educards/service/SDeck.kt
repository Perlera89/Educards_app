package com.educards.service

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.educards.model.Deck
import com.educards.model.entities.Card
import com.educards.service.FirebaseConnection.refGlobal
import com.educards.util.UAlertGenericDialog.createDialogAlert
import com.educards.util.UIndexDeckOrCard.getItemsCardInDeckSelected
import com.educards.util.UIndexDeckOrCard.getLastKeyCardDeckSelected
import com.educards.util.UIndexDeckOrCard.getLastKeyDeck
import com.educards.util.UIndexDeckOrCard.realTimeIndexCardInSelectedDeck
import com.educards.util.UIndexDeckOrCard.setSelectedDeckKey
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object SDeck {

    fun saveDeck(_deck: Deck,_context:Context) {
        val newDeckKey = getLastKeyDeck()?.toInt()?.plus(1)
        refGlobal.child("/decks/$newDeckKey")
            .setValue(_deck)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    //declaramos como seleccionado el mazo recien creado
                    setSelectedDeckKey(_deck.getId())
                    //se actualiza el contador de tarjetas y el indice de la ultima tarjeta del nuevo mazo seleccionado
                    realTimeIndexCardInSelectedDeck()
                    Toast.makeText(_context,"Deck added successfully",Toast.LENGTH_SHORT).show()
                    CoroutineScope(Dispatchers.Main).launch{
                        Thread.sleep(2000)
                        //indice y llave de la tarjeta creada por defecto
                        val cardIndex = getItemsCardInDeckSelected().plus(1)
                        val newCardKey = getLastKeyCardDeckSelected()?.toInt()?.plus(1)
                        SCard.saveCard(_context,Card("$newCardKey","Click to edit question $cardIndex","Click to edit answer $cardIndex"))
                    }
                }
            }.addOnFailureListener {
                createDialogAlert(_context,"Deck","Failed to add deck.\nDetails: $it")
            }
    }

    fun updateDeck(_context: Context,_deck: Deck,_type:String){
        //la llave del deck seleccionado viene desde el bundle ya en el objeto deck
        val deck = mapOf(
            "id" to _deck.getId(),
            "title" to _deck.getTitle(),
            "description" to _deck.getDescription(),
            "isFavorite" to _deck.getIsFavorite(),
            "count" to _deck.getCount()
        )
        refGlobal.child("/decks/${_deck.getId()}")
            .updateChildren(deck)
            .addOnCompleteListener {
                if (it.isSuccessful){
                    Toast.makeText(_context, "Deck $_type updated successfully", Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener{
                createDialogAlert(_context,"Deck","Failed to update deck $_type.\n Details: ${it}")
            }
    }

    fun deleteDeck(_context: Context,_deckId: String){
        refGlobal.child("/decks/${_deckId}")
            .removeValue()
            .addOnCompleteListener {
                if (it.isSuccessful){
                    Toast.makeText(_context, "Deck successfully removed", Toast.LENGTH_SHORT).show()
                    //se borran las tarjetas que esten en una rama que tenga la misma llavel del mazo eliminado
                    SCard.deleteAllCards(_context)
                }
            }.addOnFailureListener{
                createDialogAlert(_context,"Deck","Failed to delete deck.\nDetails: $it")
            }
    }

    fun updateCountInDeck(_context: Context,_deck: Deck){
        val currentCountCard = getItemsCardInDeckSelected()
        refGlobal.child("/decks/${_deck.getId()}").child("count")
            .setValue(currentCountCard)
            .addOnCompleteListener {
                if (it.isSuccessful){
                    Log.d("RSDeck","Id ${_deck.getId()} deck counter has been successfully updated")
                }
            }.addOnFailureListener{
                createDialogAlert(_context,"Deck","Failed to update deck counter.\nDetails: $it")
            }
    }
}