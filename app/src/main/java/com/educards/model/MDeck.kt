package com.educards.model

object MDeck {
    /*
    * Esta clase tiene una estructura singleton asi que solo se puede instaciar una vez, por lo que tiene un metodo para
    * limpiar los datos antes de usarla nuevamente*/
    private lateinit var id: String
    private lateinit var title: String
    private lateinit var description: String
    private var isFavorite:Boolean = false
    private var count:Int = 0

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
    }
}