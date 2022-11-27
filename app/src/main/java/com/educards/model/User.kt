package com.educards.model

import android.net.Uri
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.GetTokenResult

object User {
    /*
    Esta clase la estructura Singleton por lo que solo puede ser instanciada una ver, por ello tiene el metodo
    clearUserData para poder limpier los datos que posea antes de usarle de nuevo
     */
    private var tokenUser:Task<GetTokenResult>? = null
    private var idUser:String = ""
    private var displayName:String? = null
    private var email: String = ""
    private var password:String = ""
    private var verified:Boolean = false
    private var fotoUrl: Uri? = null

    fun getTokenUser(): Task<GetTokenResult>?{
        return this.tokenUser
    }
    fun setTokenUser(_tokenUser: Task<GetTokenResult>){
        this.tokenUser = _tokenUser
    }
    fun getIdUser():String{
        return this.idUser
    }
    fun setIdUser(_idUser: String){
        this.idUser = _idUser
    }
    fun getDisplayName():String?{
        return this.displayName
    }
    fun setDisplayName(_displayName: String?){
        this.displayName = _displayName
    }
    fun getEmail():String{
        return this.email
    }
    fun setEmail(_email: String){
        this.email = _email
    }
    fun getPassword():String{
        return this.password
    }
    fun setPassword(_password: String){
        this.password = _password
    }
    fun getVerified():Boolean{
        return this.verified
    }
    fun setVerified(_verified: Boolean){
        this.verified = _verified
    }
    fun getFotoUri():Uri?{
        return this.fotoUrl
    }
    fun setFotoUri(_fotoUrl: Uri?){
        this.fotoUrl = _fotoUrl
    }
    fun clearUserData(){
        tokenUser = null
        idUser= ""
        displayName = null
        email=""
        password= ""
        verified = false
        fotoUrl = null
    }
}