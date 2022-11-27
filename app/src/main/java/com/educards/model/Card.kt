package com.educards.model.entities

import java.util.*

class Card(){
    private lateinit var id: String
    private lateinit var question: String
    private lateinit var answer: String

    constructor(_id:String,_question: String,_answer: String):this(){
        this.id = _id
        this.question = _question
        this.answer = _answer
    }

    fun getId(): String {
        return this.id
    }

    fun setId(_id: String) {
        this.id = _id
    }

    fun getQuestion(): String {
        return this.question
    }

    fun setQuestion(_question: String) {
        this.question = _question
    }

    fun getAnswer(): String {
        return this.answer
    }

    fun setAnswer(_answer: String) {
        this.answer = _answer
    }

    fun clearCardData(){
        id = ""
        question = ""
        answer = ""
    }
}