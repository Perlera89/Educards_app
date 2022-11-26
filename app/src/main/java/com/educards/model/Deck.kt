package com.educards.model

class Deck() {
    private lateinit var id: String
    private lateinit var title: String
    private lateinit var description: String
    private var isFavorite:Boolean = false
    private var count:Int = 0

    constructor(_id: String,_title: String,_description: String,_isFavorite:Boolean,_count:Int = 0):this(){
        this.id = _id
        this.title = _title
        this.description = _description
        this.isFavorite = _isFavorite
        this.count = _count
    }

    fun getId(): String {
        return this.id
    }

    fun setId(_id: String) {
        this.id = _id
    }

    fun getTitle(): String {
        return this.title
    }

    fun setTitle(_title: String) {
        this.title = _title
    }
    fun isFavorite(): Boolean {
        return this.isFavorite
    }

    fun setFavorite(_isFavorite: Boolean) {
        this.isFavorite = _isFavorite
    }

    fun getDescription(): String {
        return this.description
    }

    fun setDescription(_description: String) {
        this.description = _description
    }

    fun getCount():Int{
        return this.count
    }

    fun setCount(_count:Int){
        this.count = _count
    }

    fun clearDeckData(){
        id = ""
        title = ""
        description = ""
        isFavorite = false
        count = 0
    }
}
