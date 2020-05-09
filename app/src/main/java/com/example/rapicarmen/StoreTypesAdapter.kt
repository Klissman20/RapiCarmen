package com.example.rapicarmen

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DatabaseReference
import com.google.firebase.firestore.*


class StoreTypesAdapter  (context: Context): BaseAdapter() {
    private var context: Context = context
    var stores: Array<StoreTypes> = StoreTypes("",0).getArray()

    /*
    val mFirestore = FirebaseFirestore.getInstance()

    val refNegocios = mFirestore.collection("Negocios")

    val res = refNegocios.get().addOnSuccessListener { result ->
        for (document in result){
            stores[] = StoreTypes("{$document.id}")
        }
    }.addOnFailureListener { exception ->
        Toast.makeText(context, "Error getting documents.", Toast.LENGTH_SHORT).show()
    }

     */


    override fun getCount(): Int {
        return stores.size
    }

    override fun getItem(position: Int): StoreTypes {
        return stores[position]
    }

    override fun getItemId(position: Int): Long {
        return getItem(position).getId().toLong()
    }

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