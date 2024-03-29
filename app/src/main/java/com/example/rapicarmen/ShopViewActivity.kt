package com.example.rapicarmen

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rapicarmen.adapter.ShopAdapter
import com.google.firebase.firestore.*
import java.util.*


class ShopViewActivity : AppCompatActivity(),View.OnClickListener, ShopAdapter.OnShopSelectedListener {

    private var mFirestore: FirebaseFirestore? = null
    private var mQuery: Query? = null
    private var mShopsRecycler: RecyclerView? = null
    private val mEmptyView: ViewGroup? = null
    private var mAdapter: ShopAdapter? = null
    private var mSearchView: SearchView? = null

    private var bundle: Bundle? = null
    private var query: String? = null
    private var searchTerm: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop)
        val toolbar = findViewById<Toolbar>(R.id.toolbare)
        setSupportActionBar(toolbar)

        bundle = this.intent.extras
        query = bundle!!.getString("query")

        mShopsRecycler = findViewById(R.id.recycler_shops)

        mSearchView = findViewById<SearchView>(R.id.searchViewShop)
        mSearchView!!.isActivated

        initFirestore()
        getFirestoreQuery()
        initRecyclerView()

    }

    private fun filter(){
        mSearchView!!.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String): Boolean {
                searchTerm = newText
                mAdapter!!.stopListening()
                if (searchTerm != "") {
                    Search(searchTerm)
                    mAdapter!!.setQuery(mQuery)
                }else{
                    getFirestoreQuery()
                    mAdapter!!.setQuery(mQuery)
                }

                //mAdapter!!.startListening()
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                searchTerm = query
                mAdapter!!.stopListening()
                if (searchTerm != "") {
                    Search(searchTerm)
                    mAdapter!!.setQuery(mQuery)
                }else{
                    getFirestoreQuery()
                    mAdapter!!.setQuery(mQuery)
                }
                //mAdapter!!.startListening()
                return false
            }
        })


    }

    private fun Search(search: String?): Query? {

        mQuery = mFirestore!!.collection("/Negocios").document("$query")
            .collection("Tiendas").orderBy("nombre")
            .whereGreaterThanOrEqualTo("nombre", search!!.toUpperCase(Locale.ROOT))
            //.startAt(search!!.toUpperCase()).limit(3)
            //.whereGreaterThanOrEqualTo("nombre","${search!!.toUpperCase()}")
        //.startAt(search!!.toUpperCase())//.endAt(search + "\uf8ff")
        return mQuery
    }

    private fun initFirestore(){
        mFirestore = FirebaseFirestore.getInstance()

        val settings = FirebaseFirestoreSettings.Builder()
            .setPersistenceEnabled(true)
            .setCacheSizeBytes(FirebaseFirestoreSettings.CACHE_SIZE_UNLIMITED)
            .build()
        mFirestore!!.firestoreSettings = settings
    }

    private fun getFirestoreQuery() {
        // To read data from Firetore Database
        mQuery = mFirestore!!.collection("/Negocios").document("$query")
            .collection("Tiendas").orderBy("nombre")

    }

    override fun onStart() {
        super.onStart()

        filter()

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
        //when (v.id) {
            //R.id.filter_bar -> onFilterClicked()
            //R.id.button_clear_filter -> onClearFilterClicked()
        //}
    }

    override fun onShopSelected(shop: DocumentSnapshot?) {
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
