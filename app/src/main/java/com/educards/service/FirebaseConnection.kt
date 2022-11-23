package com.educards.service

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

object FirebaseConnection {
    /*
    Solo cuando estas variables se llamen se van a inicializar
    firebaseFirestoreDB ----  obtenemos una instancia de la base de datos en tiempo real
    firebaseAuth ---- instancia de FirebaseAuth que nos permite usar los servicios de autentificacion de Firebase
     */
    val firebaseRealTimeDB: FirebaseDatabase by lazy{FirebaseDatabase.getInstance()}
    val firebaseAuth: FirebaseAuth by lazy { Firebase.auth }
}