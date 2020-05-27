package com.example.rapicarmen

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query

class ShopViewActivity : AppCompatActivity(),View.OnClickListener, ShopAdapter.OnShopSelectedListener {

    private var mFirestore: FirebaseFirestore? = null
    private var mQuery: Query? = null
    private var mShopsRecycler: RecyclerView? = null
    private val mEmptyView: ViewGroup? = null
    private var mAdapter: ShopAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop)
        val toolbar = findViewById<Toolbar>(R.id.toolbare)
        setSupportActionBar(toolbar)

        mShopsRecycler = findViewById(R.id.recycler_shops)

        getFirestoreQuery()
        initRecyclerView()
    }

    private fun getFirestoreQuery() {
        mFirestore = FirebaseFirestore.getInstance()

        var bundle: Bundle? = this.intent.extras
        var query = bundle!!.getString("query")

        // To read data from Firetore Database
        mQuery = mFirestore!!.collection("/Negocios").document("$query")
            .collection("Tiendas")
    }

    override fun onStart() {
        super.onStart()

        if (mAdapter != null) {
            mAdapter!!.startListening()
        }
    }

    override fun onStop() {
        super.onStop()
        if (mAdapter != null) {
            mAdapter!!.stopListening()
        }
    }

    /*
    override fun onPause() {
        super.onPause()
        if (mAdapter != null) {
            mAdapter!!.stopListening()
        }
    }

    override fun onResume() {
        super.onResume()
        if (mAdapter != null) {
            mAdapter!!.startListening()
        }
    }

     */

    override fun onDestroy() {
        super.onDestroy()
        finish()
    }


    override fun onClick(v: View) {
        when (v.id) {
            //R.id.filter_bar -> onFilterClicked()
            //R.id.button_clear_filter -> onClearFilterClicked()
        }
    }

    override fun onShopSelected(shop: DocumentSnapshot?) {
        // Go to the details page for the selected restaurant
        //val intent = Intent(this, RestaurantDetailActivity::class.java)
        //intent.putExtra(RestaurantDetailActivity.KEY_RESTAURANT_ID, restaurant!!.id)
        //startActivity(intent)
        //Toast.makeText(this,"Selecciono: "+"${shop!!["nombre"]}",Toast.LENGTH_SHORT).show()
        val intent = Intent(this, ShopDetailsActivity::class.java).apply {
            putExtra("nombre", shop!!["nombre"].toString())
            putExtra("tel", shop["telefono"].toString())
            putExtra("categoria", shop["categoria"].toString())
        }
        startActivity(intent)
    }

    //initRecyclerView  obtiene la vista de las tiendas según su categoria
    private fun initRecyclerView() {
        if (mQuery == null) {
            Log.w("MAIN", "No query, not initializing RecyclerView")
        }
        mAdapter = object : ShopAdapter(mQuery, this) {
            override fun onDataChanged() {
                // Show/hide content if the query returns empty.
                if (getItemCount() == 0) {
                    mShopsRecycler!!.setVisibility(View.GONE)
                    mEmptyView!!.setVisibility(View.VISIBLE)
                } else {
                    mShopsRecycler!!.setVisibility(View.VISIBLE)
                    mEmptyView!!.setVisibility(View.GONE)
                }
            }

            override fun onError(e: FirebaseFirestoreException?) {
                Toast.makeText(this@ShopViewActivity, "Error obteniendo los datos", Toast.LENGTH_SHORT).show()

            }
        }
        mShopsRecycler!!.setLayoutManager(LinearLayoutManager(this))
        mShopsRecycler!!.adapter = mAdapter
    }
}
