package com.example.rapicarmen

class Shop(name: String, telefono:String, categoria: String) {

    private var nombre = name
    private var tel = telefono
    private var cat = categoria

    fun getNombre(): String{return this.nombre}
    fun setNombre(name: String){this.nombre = name}

    fun getTelefono(): String {return this.tel}
    fun setTelefono(tel: String){this.tel = tel}

    fun getCategoria(): String {return this.cat}
    fun setCategoria(cat: String){this.cat = cat}
}