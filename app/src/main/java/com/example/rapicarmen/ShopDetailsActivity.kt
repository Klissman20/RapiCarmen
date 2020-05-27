package com.example.rapicarmen

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.storage.FirebaseStorage

class ShopDetailsActivity : AppCompatActivity() {

    val storageReference = FirebaseStorage.getInstance()
    //val imgTiendasRef = storageReference.child("ImagenesTiendas/")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_detail)

        getData()
    }

    private fun getData() {

        var bundle: Bundle? = this.intent.extras
        var nombre = bundle!!.getString("nombre")
        var telefono = bundle.getString("tel")
        var categoria = bundle.getString("categoria")

        val imgReference = storageReference.getReferenceFromUrl("gs://rapicarmen-database.appspot.com/ImagenesTiendas/$nombre.png")
        //val imgReference = storageReference.reference.child("ImagenesTiendas/$nombre.png")

        val imageView = findViewById<ImageView>(R.id.shop_item_imageImg)

        GlideApp.with(this)
            .load(imgReference)
            .into(imageView)

        val nameView = findViewById<TextView>(R.id.nombredetail)
        val telView = findViewById<TextView>(R.id.teldetail)
        val catView = findViewById<TextView>(R.id.catdetail)

        nameView.setText(nombre)
        telView.setText(telefono)
        catView.setText(categoria)
    }

    private fun showAlert() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Se ha producido un error cargando los datos")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }




}

