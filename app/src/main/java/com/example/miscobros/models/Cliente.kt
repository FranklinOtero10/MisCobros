package com.example.miscobros.models

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
class Cliente(
    var uId: String,
    var nombres: String,
    var email: String,
    var direccion: String,
    var telefono: String){

    constructor() : this("","", "", "", "")
}
    /*fun getNames(): String = this.nombres
    fun getCorreo(): String = this.email
    fun getDir(): String = this.direccion
    fun getTel(): String = this.telefono*/

