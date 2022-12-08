package com.example.miscobros.models

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
class Cobros(
    var uId: String,
    var concepto: String,
    var saldoPagar: Double,
    var fechaCobro: String,
    var cobrador: String,
    var cliente: String) {

    constructor() : this("", "", 0.0, "", "", "")
}