package com.example.rapicarmen

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.GridView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.facebook.login.LoginManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*

enum class ProviderType{
    BASIC,
    GOOGLE,
    FACEBOOK
}

class MainActivity : AppCompatActivity() {

    private var mFirestore: FirebaseFirestore? = null
    private var provider: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<Toolbar>(R.id.toolbare)
        setSupportActionBar(toolbar)

        val bundle= intent.extras
        val email= bundle?.getString("email")
        provider = bundle?.getString("provider")
        //setup(email?:"",provider?:"")

        /*
        val a = R.drawable.ic_belleza_store
        val b = R.drawable.ic_carnicerias_store
        val c = R.drawable.ic_farmacias_store
        val d = R.drawable.ic_fruterias_store
        val e = R.drawable.ic_licores_store
        val f = R.drawable.ic_mascotas_store
        val g = R.drawable.ic_moda_stores
        val h = R.drawable.ic_restaurantes_store
        val i = R.drawable.ic_tiendas_store
        val j = R.drawable.ic_tecnologia_store
         */

        //Guardado de datos
        val prefs=getSharedPreferences(getString(R.string.prefs_file),Context.MODE_PRIVATE).edit()
        prefs.putString("email",email)
        prefs.putString("provider",provider)
        prefs.apply()

        val gridView: GridView = findViewById(R.id.grid)

        initFirestore()

        //Interfaz de captura los datos de la consulta hecha a firestore para inflar la vista de las categorias
        getCategories(object : FirestoreCallBack {
            override fun onCallBack(arrayCategories: Array<StoreTypes>) {
                val storeAdapter = StoreTypesAdapter(baseContext, arrayCategories);
                gridView.adapter = storeAdapter;
            }
        },mFirestore!!)

        gridListener(gridView)//Funcion de escucha para clicks en gridView(Vista de categorias)
    }

    private fun signOut(provider: String?){
        val prefs=getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
        prefs.clear()
        prefs.apply()

        if(provider == ProviderType.FACEBOOK.name){
            LoginManager.getInstance().logOut()
        }

        FirebaseAuth.getInstance().signOut()
        val intent = Intent(this,LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun initFirestore() {
        mFirestore = FirebaseFirestore.getInstance()
    }

    //Escribir tiendas en la base de datos
    private fun onAddItemsClicked() {
        val storeCarnicerias = mFirestore!!.collection("Negocios")
            .document("Carnicerias")
            .collection("Tiendas")
        /*
        val tienda = StoreTypes("holaaaa",5433456)
        val store = hashMapOf(
            "nombre" to "${tienda.getNombre()}",
            "telefono" to "${tienda.getIdDrawable()}"
        )*/

        val tienda = Shop()
        storeCarnicerias.document().set(tienda)
    }

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
                //onAddItemsClicked()
                true
            }
            R.id.signOut -> {
                signOut(provider)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun gridListener(gridView: GridView){
        gridView.onItemClickListener = object : AdapterView.OnItemClickListener{
            override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                //var selectedItem = parent?.getItemAtPosition(position)
                when (position) {
                    0 -> {
                        val query: String = "Belleza"
                        val intent = Intent(this@MainActivity, ShopViewActivity::class.java).apply {
                            putExtra("query", query)
                        }
                        startActivity(intent)
                    }
                    1 -> {
                        val query: String = "Carnicerias"
                        val intent = Intent(this@MainActivity, ShopViewActivity::class.java).apply {
                            putExtra("query", query)
                        }
                        startActivity(intent)
                    }
                    2 -> {
                        val query: String = "Farmacias"
                        val intent = Intent(this@MainActivity, ShopViewActivity::class.java).apply {
                            putExtra("query", query)
                        }
                        startActivity(intent)
                    }
                    3 -> {
                        val query: String = "Fruterias"
                        val intent = Intent(this@MainActivity, ShopViewActivity::class.java).apply {
                            putExtra("query", query)
                        }
                        startActivity(intent)
                    }
                    4 -> {
                        val query: String = "Licoreras"
                        val intent = Intent(this@MainActivity, ShopViewActivity::class.java).apply {
                            putExtra("query", query)
                        }
                        startActivity(intent)
                    }
                    5 -> {
                        val query: String = "Mascotas"
                        val intent = Intent(this@MainActivity, ShopViewActivity::class.java).apply {
                            putExtra("query", query)
                        }
                        startActivity(intent)
                    }
                    6 -> {
                        val query: String = "Moda"
                        val intent = Intent(this@MainActivity, ShopViewActivity::class.java).apply {
                            putExtra("query", query)
                        }
                        startActivity(intent)
                    }
                    7 -> {
                        val query: String = "Restaurantes"
                        val intent = Intent(this@MainActivity, ShopViewActivity::class.java).apply {
                            putExtra("query", query)
                        }
                        startActivity(intent)
                    }
                    8 -> {
                        val query: String = "Supermercados"
                        val intent = Intent(this@MainActivity, ShopViewActivity::class.java).apply {
                            putExtra("query", query)
                        }
                        startActivity(intent)
                    }
                    9 -> {
                        val query: String = "Tecnologia"
                        val intent = Intent(this@MainActivity, ShopViewActivity::class.java).apply {
                            putExtra("query", query)
                        }
                        startActivity(intent)
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
    }

    //Interfaz de devolucion de llamada de los datos en Firestore ^^
    interface FirestoreCallBack{
        fun onCallBack(arrayCategories: Array<StoreTypes>)
    }

}