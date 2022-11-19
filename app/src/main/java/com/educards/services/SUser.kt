package com.educards.services

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.educards.model.User
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.userProfileChangeRequest

object SUser {
    /*
    1 Metodo que nos retornaná el usuario actual
    2 Accedemos a la instacia unica de nueatra clase FirebaseConnection, accedemos a la variable firebaseAuth para acceder
    los servicios de autentficacion de firebase y luego accedemos a su propiedad currentUser que nos devuelve el usuario que ha
    iniciado sesion
    3. Mensaje de seguimiento
     */
    fun getCurrentUser(): FirebaseUser?{
        return FirebaseConnection.firebaseAuth.currentUser
        Log.d("service/SUser", "Informacion de usuario enviada.")
    }

    /*
    1 Metodo que nos retornaná los detalles del usuario que ha iniciado sesion
    2 Usamos el metodo getCurrentUser para acceder al usuario logueado, y con let indicamcos que trabajaremos con los
    datos que retorne
    3. Con el metodo clearUserData limpiamos los datos de la clase unica de User
    4... luego procedemos agregar los datos los datos a la clase y la retornamos
     */
    fun getCurrentUserDetailData():User{
            getCurrentUser()?.let {
            User.clearUserData()
            User.setTokenUser(it.getIdToken(true))
            User.setIdUser(it.uid)
            User.setDisplayName(it.displayName)
            User.setEmail(it.email.toString())
            User.setVerified(it.isEmailVerified)
            User.setFotoUri(it.photoUrl)
        }
        Log.d("service/SUser", "Informacion detallada de usuario enviada.")
        return User
    }

    /*
    1. metodo para guardar un nuevo usuario; recibe un context para los mensajes flotantes, y un objeto user
    2. guardamos el Name de usuario (No funciona si no es asi)
    3. Accedemos a la clase FirebaseConnection y a la variable firebaseAuth, esta propiedad contiene el método
    createUserWithEmailAndPassword que permite crear un usuario solo con correo y contraseña
    4. Agregramos el evento addOnCompleteListener oara detectar si todo se realizo de manera adecuada
    5. Si todo sale bien, se envía automáticamente un correo de verificacion
    6. Se actualica el display name( el nombre de usuario), pues por defecto solo guarda correo y contraseña
    7. accedemos al metodo getCurrentUser que nos retorna el valor de la propiedad currentUser y esta posee el metodo
    reload que nos refrescar los datos user actual pues hemos actualizado su displaymane
    8. Por ultimo tambien se agrega el método  addOnFailureListener para detectar errores y dar una alerta
     */
    fun saveUser(_context: Context, _user:User){
        val name = User.getDisplayName()
        FirebaseConnection.firebaseAuth.createUserWithEmailAndPassword(_user.getEmail(),_user.getPassword())
            .addOnCompleteListener {
                if (it.isSuccessful){
                    Log.d("service/SUser", "Usuario creado con exito.")
                    sendEmailVerificationToUser()
                    updateUserDisplayName(name)
                    getCurrentUser()?.reload()
                    Toast.makeText(_context,"Usuario creado correctamente",Toast.LENGTH_LONG).show()
                }
            }.addOnFailureListener{
                Toast.makeText(_context,"Error al crear el usuario."+it.toString(),Toast.LENGTH_LONG).show()
            }
    }

    /*
    1. El metodo loginToApp se usa oara iniciar sesion, recibe un contexto para mandar mensajes y un objeto de User
    2. Accedemos a la clase FirebaseConnection, luego a la variable firebaseAuth que contine el metodo
    signInWithEmailAndPassword que permite iniciar sesion con correo y contraseña
    3. Agreamaos el metodo addOnCompleteListener para detectar si todo se realizó bien. A este puento la propiedad currentUser
    ya tiene los datos del usuario loguedo
    4. Con el método getCurrentUserDetailData accedemos a las propiedades del usuario logueado, y si este en su propiedad
    isEmailVerified es falso le indicamos por mensaje que debe verificarlo
    5. Agregamos el método addOnFailureListener para escuchar errores y mandar mensajes de alerte
     */
    fun loginToApp(_context: Context,_user:User){
        FirebaseConnection.firebaseAuth.signInWithEmailAndPassword(_user.getEmail(),_user.getPassword())
            .addOnCompleteListener { task ->
                if (task.isSuccessful){
                    Log.d("service/SUser","El usuario ${getCurrentUserDetailData().getEmail()} ha iniciado sesión exitosamente")
                    if (getCurrentUserDetailData().getVerified()==false){
                        Toast.makeText(_context, "Debe verificar su correo correo electrónico para iniciar sesión.", Toast.LENGTH_LONG).show()
                    }
                }
                /*getCurrentUserDetailData().let {
                    println("nombre: "+it.getDisplayName())
                    println("email: "+it.getEmail())
                    println("verificación: "+it.getVerified())
                }*/
            }.addOnFailureListener{ e ->
                Toast.makeText(_context,"Error al iniciar sesión. Ingrese datos válidos o regístrese. \nDetalles: "+e.toString(), Toast.LENGTH_LONG).show()
                Log.d("service/SUser","Error al iniciar sesión, detalles: "+e.toString())
            }
    }

    /*
    1. Metodo para actualizar la propiedad displayName del currentUser; este recibe como parametro el nombre del usuario
    2. A traves de método getCurrentUser accedemos la propiedad currentUser que contiene el método updateProfile
    que permite actualizar propiedades del currentUser y recibe como parametro un objeto userProfileChangeRequest en el
    cual iremos llamando a las propiedades y les daremos su nuevo valor
    3. Agregamos el método addOnCompleteListener para detectar si todo se ha actualizado correctamente
    4. Llamamos al getCurrentUser para acceder a la propiedad currentUser y llamar al método reload para refrescar las
    propiedades del currentUser
    5. Agregamos el método addOnFailureListener para detectar errores y mandar alertas
     */
    private fun updateUserDisplayName(_displayName:String?){
        getCurrentUser()?.updateProfile(userProfileChangeRequest {
            displayName = _displayName
        })?.addOnCompleteListener {
            if (it.isSuccessful){
                Log.d("service/SUser","Se ha actualizado el displayname exitosamente")
                getCurrentUser()?.reload()
            }
        }?.addOnFailureListener{
            Log.d("service/SUser","Ha ocuarrido un error al actualizar el display name")
        }
    }

    fun deleteUser(_currentUser:FirebaseUser){
        _currentUser.delete()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("service/SUser", "User account deleted.")
                    getCurrentUser()?.reload()
                }
            }.addOnFailureListener{ e ->
                Log.d("service/SUser","Error al borrar el usuario")
            }
    }

/*
1. Este metodo permite enviar un correo de verificacion al usuario
2. Se usa el metodo getCurrentUser para verificar que el currentUser actual no sea nulo; hay alguien logueado
3. Se usa el metodo getCurrentUserDetailData para acceder a la propiedad isEmailVerified para comprobar que no esta veridicado
4. Accedemos al current user actual con el metodo getCurrentUser y accedemos al metodo sendEmailVerification para enviar el correo de verififcacion
5. Se agrega el método addOnCompleteListener para detectar si todo sucedio sin problemas y el metodo addOnFailureListener para
detectar errores y dar alerta

     */
    private fun sendEmailVerificationToUser(){
        if (getCurrentUser() != null) {
            if (getCurrentUserDetailData().getVerified() == false) {
                getCurrentUser()?.sendEmailVerification()
                    ?.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d("service/SUser", "Correo de verificación enviado")
                        }
                    }?.addOnFailureListener {
                        Log.d("service/SUser", "Error al enviar el correo de verificación" + it.toString()
                        )

                    }
            }
        }
    }

    fun sendEmailToResetPasswordToUser(_email:String){
        FirebaseConnection.firebaseAuth.sendPasswordResetEmail(_email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("service/SUser", "Email de cambio de contraseña enviado.")
                }
            }
    }

}