package com.educards.services

import android.util.Log
import com.educards.model.entities.Card
import com.google.firebase.firestore.FieldValue

object SCard {
    fun saveCard(_documentPathToCard: String,_card:Card) {
        val newCard = hashMapOf(
            "question" to _card.getQuestion(),
            "answer" to _card.getAnswer(),
            "creationDate" to FieldValue.serverTimestamp(),
            "lastUpdateDate" to FieldValue.serverTimestamp()
        )
        FirebaseConnection.refCollectionGlobal.document(_documentPathToCard).collection("cards").add(newCard)
            .addOnCompleteListener { task->
                if (task.isSuccessful){
                    Log.d("dao/DCards","Nuevo card creado existosamente")
                    SDeck.updateCountInDeck(_documentPathToCard,1.0)
                }
            }.addOnFailureListener{
                Log.d("data/dao/DCards","Error al crear nuevo card. Detalles: "+it.toString())
            }
    }

    fun showCards(_documentPathToCard: String):ArrayList<Array<String>> {
        var cards = ArrayList<Array<String>>()
        FirebaseConnection.refCollectionGlobal.document(_documentPathToCard).collection("cards")
            .orderBy("creationDate").get().addOnCompleteListener {
                if (it.isSuccessful) {
                    Log.d("dao/DSets", "Lista de cards del usuario generada con exito")
                    for (i in it.result.documents) {
                        cards.add(arrayOf(
                            i.id,
                            i.data?.get("question").toString(),
                            i.data?.get("answer").toString()
                        ))
                    }
                }

            }
        return cards
    }

    fun updateCards(_documentPathToCard: String,_card:Card) {
        val updateCard = hashMapOf(
            "question" to _card.getQuestion(),
            "answer" to _card.getAnswer(),
            "lastUpdateDate" to FieldValue.serverTimestamp()
        )
        FirebaseConnection.refCollectionGlobal.document(_documentPathToCard).collection("cards")
            .document(Card.getId()).update(updateCard)
            .addOnCompleteListener { task->
                if (task.isSuccessful){
                    Log.d("dao/DCards","Card actualizado existosamente")
                }
            }.addOnFailureListener{
                Log.d("data/dao/DCards","Error al actualizar card. Detalles: "+it.toString())
            }


    }

    fun deleteCard(_documentPathToCard: String,_idCard:String){
        FirebaseConnection.refCollectionGlobal.document(_documentPathToCard).collection("cards")
            .document(_idCard).delete()
            .addOnSuccessListener {
                Log.d("dao/DCards","El card se ha eliminado exitosamente")
                SDeck.updateCountInDeck(_documentPathToCard,-1.0)
            }.addOnFailureListener{
                Log.d("dao/DCards", "Error al eliminar el card. Detalles: $it")

            }
    }

    fun deleteAllCards(_documentCardIdPath:String){
        FirebaseConnection.refCollectionGlobal.document(_documentCardIdPath).collection("cards").get()
            .addOnSuccessListener { querySnapshop->
                if (!querySnapshop.isEmpty){
                    querySnapshop.documents.forEach{document->
                        deleteCard(_documentCardIdPath,document.id)
                    }
                }
            }.addOnFailureListener{
                Log.d("dao/DCards", "Error al eliminar los cards. Detalles: $it")
            }
    }
}