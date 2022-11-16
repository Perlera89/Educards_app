package com.educards.model

import com.educards.model.entities.Card
import java.util.*

data class Deck(
    val title: String,
    val description: String,
    val icon: String,
    val creationDate: Date,
    val lastUpdateDate: Date,
    val cards: List<Card>
) {

}