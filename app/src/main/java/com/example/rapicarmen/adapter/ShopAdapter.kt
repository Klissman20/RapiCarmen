package com.example.rapicarmen.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.rapicarmen.util.GlideApp
import com.example.rapicarmen.R
import com.example.rapicarmen.model.Shop
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage

open class ShopAdapter(mquery: Query?, mListener: OnShopSelectedListener) : FirestoreAdapter<ShopAdapter.ViewHolder>() {

    interface OnShopSelectedListener {
        fun onShopSelected(shop: DocumentSnapshot?)
    }

    private var mListener: OnShopSelectedListener? = null

    init{
        FirestoreAdapter(mquery)
        this.mListener = mListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(
            inflater.inflate(
                R.layout.item_shop,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getSnapshot(position), mListener)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageView: ImageView? = null
        var nameView: TextView? = null
        var telView: TextView? = null
        var catView: TextView? = null

        init {
            imageView = itemView.findViewById(R.id.shop_item_image)
            nameView = itemView.findViewById(R.id.shop_item_name)
            telView = itemView.findViewById(R.id.shop_item_telefono)
            catView = itemView.findViewById(R.id.shop_item_category)
        }

        fun bind(snapshot: DocumentSnapshot?, listener: OnShopSelectedListener?) {

            val shop: Shop? = snapshot?.toObject(
                Shop::class.java)
            //val resources: Resources = itemView.resources

            val storageReference = FirebaseStorage.getInstance()
            val imgReference = storageReference.getReferenceFromUrl("gs://rapicarmen-database.appspot.com/LogosTiendas/${shop?.getNombre()}.png")

            imageView?.let {
                GlideApp.with(itemView.context)
                    .load(imgReference)
                    .into(it)
            }

            //imageView!!.setImageResource(R.drawable.ic_launcher_rapicarmen_foreground)
            nameView!!.setText(shop?.getNombre())
            telView!!.setText(shop?.getTelefono().toString())
            catView!!.setText(shop?.getCategoria())

            // Click listener
            itemView.setOnClickListener {
                listener?.onShopSelected(
                    snapshot
                )
            }
        }
    }
}