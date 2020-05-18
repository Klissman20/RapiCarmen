package com.example.rapicarmen

open class StoreTypes (name: String, id: Int) {

    private var nombreCategoria: String = name
    private var idDrawable: Int = id

    fun getNombre(): String{ return this.nombreCategoria }

    fun setNombre(name: String){ this.nombreCategoria = name }

    fun getIdDrawable(): Int{ return this.idDrawable }

    fun setidDrawable(id: Int){ this.idDrawable = id }

    fun getId(): Int{ return this.nombreCategoria.hashCode() }

}