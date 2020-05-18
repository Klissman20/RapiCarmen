package com.example.rapicarmen

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.GridView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.*

class MainActivity : AppCompatActivity(), View.OnClickListener, ShopAdapter.OnRestaurantSelectedListener {

    private var mFirestore: FirebaseFirestore? = null
    private var mQuery: Query? = null

    private val mCurrentSearchView: TextView? = null
    private val mCurrentSortByView: TextView? = null
    private val mRestaurantsRecycler: RecyclerView? = null
    private val mEmptyView: ViewGroup? = null
    private var mAdapter: ShopAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<Toolbar>(R.id.toolbare)
        setSupportActionBar(toolbar)

        val gridView: GridView = findViewById(R.id.grid)

        initFirestore()

        getCategories(object : FirestoreCallBack {
            override fun onCallBack(arrayCategories: Array<StoreTypes>) {
                val storeAdapter = StoreTypesAdapter(baseContext, arrayCategories);
                gridView.adapter = storeAdapter;
            }
        },mFirestore!!)

        gridListener(gridView)//Funcion de escucha para clicks en gridView

        addRealtimeUpdate(mFirestore!!)
    }

    private fun initFirestore() {
        mFirestore = FirebaseFirestore.getInstance()


        // To read data from Firetore Database
        mQuery = mFirestore!!.collection("/Negocios").document("Carnicerias")
                                .collection("Tiendas")
            .orderBy("avgRating", Query.Direction.DESCENDING)
            .limit(Long.MAX_VALUE)///////////////////////////////////////////////(LIMIT)


    }

    private fun initRecyclerView() {
        if (mQuery == null) {
            Log.w("MAIN", "No query, not initializing RecyclerView")
        }
        mAdapter = object : ShopAdapter(mQuery, this) {
            override fun onDataChanged() {
                // Show/hide content if the query returns empty.
                if (getItemCount() == 0) {
                    mRestaurantsRecycler!!.setVisibility(View.GONE)
                    mEmptyView!!.setVisibility(View.VISIBLE)
                } else {
                    mRestaurantsRecycler!!.setVisibility(View.VISIBLE)
                    mEmptyView!!.setVisibility(View.GONE)
                }
            }

            override fun onError(e: FirebaseFirestoreException?) {
                // Show a snackbar on errors

            }
        }
        mRestaurantsRecycler!!.setLayoutManager(LinearLayoutManager(this))
        mRestaurantsRecycler.setAdapter(mAdapter)
    }

    //Escribir en la base de datos
    private fun onAddItemsClicked() {
        val storeCarnicerias = mFirestore!!.collection("Negocios")
            .document("Carnicerias")
            .collection("Tienda2")
        /*
        val tienda = StoreTypes("holaaaa",5433456)
        val store = hashMapOf(
            "nombre" to "${tienda.getNombre()}",
            "telefono" to "${tienda.getIdDrawable()}"
        )

         */
        val tienda = Tienda("pruebaa data class", 43523423)
        storeCarnicerias.document("dataclass").set(tienda)
    }

    data class Tienda(
        val nombre: String? = null,
        val telefono: Int? = null
    )

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.pedidos -> {
                Toast.makeText(baseContext,"Para revisar tus pedidos", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.domicilios -> {
                Toast.makeText(baseContext,"Para realizar tus domicilios", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.agregar -> {
                onAddItemsClicked()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun gridListener(gridView: GridView){
        gridView.onItemClickListener = object : AdapterView.OnItemClickListener{
            override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                //val selectedItem = parent?.getItemAtPosition(position)
                when (position) {
                    0 -> {
                        Toast.makeText(baseContext,"Eligio: Carnicerias",Toast.LENGTH_SHORT).show()
                    }
                    1 -> {
                        Toast.makeText(baseContext,"Eligio: Farmacias",Toast.LENGTH_SHORT).show()
                    }
                    2 -> {
                        Toast.makeText(baseContext,"Eligio: Fruterias",Toast.LENGTH_SHORT).show()
                    }
                    3 -> {
                        Toast.makeText(baseContext,"Eligio: Licoreras",Toast.LENGTH_SHORT).show()
                    }
                    4 -> {
                        Toast.makeText(baseContext,"Eligio: Restaurantes",Toast.LENGTH_SHORT).show()
                    }
                    5 -> {
                        Toast.makeText(baseContext,"Eligio: Supermercados",Toast.LENGTH_SHORT).show()
                    }
                    6 -> {
                        Toast.makeText(baseContext,"Eligio: FERRETERIAS",Toast.LENGTH_SHORT).show()
                    }
                    7 -> {
                        Toast.makeText(baseContext,"Eligio: PANADERIAS",Toast.LENGTH_SHORT).show()
                    }
                    8 -> {
                        Toast.makeText(baseContext,"Eligio: FARMACIAS",Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun addRealtimeUpdate(db: FirebaseFirestore) {
        val negociosListener: DocumentReference = db.collection("Negocios")
                .document("Carnicerias").collection("Tienda1")
                .document("Datos")
        negociosListener.addSnapshotListener(object : EventListener<DocumentSnapshot?> {
            override fun onEvent(documentSnapshot: DocumentSnapshot?, e: FirebaseFirestoreException?) {
                if (e != null) {
                    Toast.makeText(this@MainActivity,"Error",Toast.LENGTH_SHORT).show()
                    return
                }
                if (documentSnapshot != null && documentSnapshot.exists()) {
                    Toast.makeText(this@MainActivity, "Current data:"
                                                + documentSnapshot.data, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    //Funcion para obtener las categorias de la vista principal
    private fun getCategories(CallBack: FirestoreCallBack,database: FirebaseFirestore){
        var category: Array<StoreTypes>
        var index: Int = 0
        val db = database
        val negociosRef = db.collection("/Negocios")

        negociosRef.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                category = Array<StoreTypes>(size = task.result!!.size()){StoreTypes("",0)}
                for (document in task.result!!) {
                    category[index] = StoreTypes(document.id,document["drawable"].toString().toInt())
                    index++
                }
                CallBack.onCallBack(category)
            }
        }
        /*
        negociosRef.get().addOnSuccessListener { result ->

            types = Array<StoreTypes>(size = result.size()){StoreTypes("",0)}
            for (document in result) {
                types[i] = StoreTypes(document.id,document["drawable"].toString().toInt())
                i++
            }
        }*/
    }

    //Interfaz de devolucion de llamada de los datos en FireStore
    interface FirestoreCallBack{
        fun onCallBack(arrayCategories: Array<StoreTypes>)
    }


    override fun onClick(v: View) {
        when (v.id) {
            //R.id.filter_bar -> onFilterClicked()
            //R.id.button_clear_filter -> onClearFilterClicked()
        }
    }

    override fun onRestaurantSelected(restaurant: DocumentSnapshot?) {
        // Go to the details page for the selected restaurant
        //val intent = Intent(this, RestaurantDetailActivity::class.java)
        //intent.putExtra(RestaurantDetailActivity.KEY_RESTAURANT_ID, restaurant!!.id)

        //startActivity(intent)
    }

}