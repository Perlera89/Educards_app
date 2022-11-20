package com.educards.service

import android.util.Log
import com.educards.model.MDeck
import com.google.firebase.firestore.FieldValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

object SDeck {

    fun saveDeck(_deck: MDeck) {
        val newDeck = hashMapOf(
            "title" to _deck.getTitle(),
            "description" to _deck.getDescription(),
            "isFavorite" to _deck.isFavorite(),
            "count" to _deck.getCount(),
            "creationDate" to FieldValue.serverTimestamp(),
            "lastUpdateDate" to FieldValue.serverTimestamp()
        )
        FirebaseConnection.refCollectionGlobal.add(newDeck)
            .addOnCompleteListener { task->
                if (task.isSuccessful){
                    Log.d("TAG","Nuevo deck(mazo) creado existosamente")
                }
            }.addOnFailureListener{
                Log.d("data/dao/DDecks","Error al crear nuevo deck. Detalles: "+it.toString())
            }
    }

    fun showDecks():ArrayList<Array<String>> {
        var decks = ArrayList<Array<String>>()
        FirebaseConnection.refCollectionGlobal.orderBy("creationDate").get().addOnCompleteListener {
            if (it.isSuccessful) {
                Log.d("service/SDeck", "Lista de decks del usuario generada con exito")
                for (i in it.result.documents) {
                    /*Deckss.setId(i.id)
                    Deckss.setTitle(i.data?.get("title").toString())
                    Deckss.setDescription(i.data?.get("description").toString())
                    Deckss.setFavorite(i.data?.get("isFavorite").toString().toBoolean())*/
                    decks.add(
                        arrayOf(
                            i.id,
                            i.data?.get("title").toString(),
                            i.data?.get("description").toString(),
                            i.data?.get("isFavorite").toString(),
                            i.data?.get("count").toString()
                        )
                    )
                }
            }
        }
        return decks
    }

    fun updateDecks(_deck: MDeck) {
        val updateDeck = hashMapOf(
            "title" to _deck.getTitle(),
            "description" to _deck.getDescription(),
            "isFavorite" to _deck.isFavorite(),
            "count" to _deck.getCount(),
            "lastUpdateDate" to FieldValue.serverTimestamp()
        )
        FirebaseConnection.refCollectionGlobal.document(_deck.getId()).update(updateDeck)
            .addOnCompleteListener { task->
                if (task.isSuccessful){
                    Log.d("service/SDeck","Deck actualizados existosamente")
                }
            }.addOnFailureListener{
                Log.d("service/SDeck","Error al actualizar decks. Detalles: "+it.toString())
            }
    }
    fun deleteDeck(_documentDeckIdPath:String){
        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.IO) {
                SCard.deleteAllCards(_documentDeckIdPath)
            }
            withContext(Dispatchers.IO) {
                FirebaseConnection.refCollectionGlobal.document(_documentDeckIdPath).delete()
                    .addOnCompleteListener {
                        Log.d("service/SDeck", "El deck y sus documentos se han borrado con exito"
                        )
                    }
                    .addOnFailureListener {
                        Log.d("service/SDeck", "Error al eliminar el deck y sus documentos. Detalles: $it"
                        )
                    }
            }
        }
    }

    fun updateCountInDeck(_documentDeckIdPath:String, _value:Double){
        FirebaseConnection.refCollectionGlobal.document(_documentDeckIdPath)
            .update("count",FieldValue.increment(_value))
            .addOnCompleteListener {
                if (it.isSuccessful){
                    Log.d("service/SDeck", "Conteo de tarjetas en el mazo aumentado/disminuido con existo")
                }
            }.addOnFailureListener{e->
                Log.d("service/SDeck", "Error al contar las tarjetas en el mazo aumentado/disminuido. Detalles: ${e.toString()}")
            }
    }
}