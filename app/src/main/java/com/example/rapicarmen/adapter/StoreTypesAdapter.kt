package com.example.rapicarmen.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.rapicarmen.R
import com.example.rapicarmen.model.StoreTypes

@Suppress("NAME_SHADOWING")
class StoreTypesAdapter  (context: Context, stores: Array<StoreTypes>): BaseAdapter() {

    private var context = context
    private var stores = stores

    override fun getCount(): Int { return stores.size }

    override fun getItem(position: Int): StoreTypes { return stores[position] }

    override fun getItemId(position: Int): Long { return getItem(position).getId().toLong() }

    override fun getView(position: Int, view: View?, viewGroup: ViewGroup?): View {
        var view = view
        if (view == null) {
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = inflater.inflate(R.layout.grid_view, viewGroup, false)
        }
        val imagenTipo = view!!.findViewById(R.id.imagen_tipo) as ImageView
        val nombreTipo = view.findViewById(R.id.nombre_tipo) as TextView
        val item: StoreTypes = getItem(position)
        imagenTipo.setImageResource(item.getIdDrawable())
        nombreTipo.setText(item.getNombre())
        return view
    }

}