package com.educards.model.entities

import java.util.*

interface Card {
    val title: String
    val description: String
    val icon: String
    val creationDate: Date
    val lastUpdateDate: Date
}