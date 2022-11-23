package com.educards.model

data class Deck(
    var id: String?,
    val title: String,
    val description: String,
    val isFavorite:Boolean = false,
    val count:Int = 0
) {}
