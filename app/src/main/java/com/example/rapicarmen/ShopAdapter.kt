package com.example.rapicarmen

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query

open class ShopAdapter(mquery: Query?, mListener: OnRestaurantSelectedListener) : FirestoreAdapter<ShopAdapter.ViewHolder>() {

    interface OnRestaurantSelectedListener {
        fun onRestaurantSelected(restaurant: DocumentSnapshot?)
    }

    private var mListener: OnRestaurantSelectedListener? = null

    init{
        super.setQuery(mquery)
        this.mListener = mListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(inflater.inflate(R.layout.item_shop, parent, false))
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

        fun bind(snapshot: DocumentSnapshot?, listener: OnRestaurantSelectedListener?) {

            val shop: Shop? = snapshot?.toObject(Shop::class.java)
            //val resources: Resources = itemView.resources

            // Load image
            //Glide.with(imageView.context)
            //    .load(restaurant.getPhoto())
            //    .into(imageView)
            nameView!!.setText(shop?.getNombre())
            telView!!.setText(shop?.getTelefono())
            catView!!.setText(shop?.getCategoria())


            // Click listener
            itemView.setOnClickListener(View.OnClickListener {
                listener?.onRestaurantSelected(
                    snapshot
                )
            })
        }

    }



}