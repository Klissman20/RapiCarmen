package com.example.rapicarmen

import androidx.annotation.Keep
import com.google.firebase.firestore.IgnoreExtraProperties

@Keep
@IgnoreExtraProperties
data class Shop(
    private var nombre: String? = null,
    private var telefono: Long? = null,
    private var categoria: String? = null) {

    fun getNombre(): String{return this.nombre!!}
    fun setNombre(name: String){this.nombre = name}

    fun getTelefono(): Long {return this.telefono!!}
    fun setTelefono(tel: Long){this.telefono = tel}

    fun getCategoria(): String {return this.categoria!!}
    fun setCategoria(cat: String){this.categoria = cat}
}