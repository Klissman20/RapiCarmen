package com.example.rapicarmen

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.example.rapicarmen.adapter.PageAdapter
import com.example.rapicarmen.fragment.GridViewFragment
import com.example.rapicarmen.fragment.RecyclerFragment
import com.example.rapicarmen.model.Shop
import com.facebook.login.LoginManager
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.android.material.tabs.TabLayout
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
    private var tabLayout: TabLayout? = null
    private var viewPager: ViewPager? = null
    lateinit var mAdView : AdView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById<Toolbar>(R.id.toolbare)
        setSupportActionBar(toolbar)

        tabLayout = findViewById(R.id.tab_layout)
        viewPager = findViewById(R.id.viewPager)

        val bundle= intent.extras
        val email= bundle?.getString("email")
        provider = bundle?.getString("provider")

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

        setupViewPager()

        MobileAds.initialize(this, "ca-app-pub-4260371648761225~4573656018")
        //MobileAds.initialize(this) {}
        mAdView = findViewById(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)
    }

    private fun agregarFragment(): ArrayList<Fragment>{
        val frag: ArrayList<Fragment> = arrayListOf(GridViewFragment.newInstance(),
            RecyclerFragment.newInstance())
        return  frag
    }

    private fun setupViewPager(){
        viewPager!!.adapter = PageAdapter(
            supportFragmentManager,
            agregarFragment()
        )
        tabLayout!!.setupWithViewPager(viewPager)

        tabLayout!!.getTabAt(0)!!.text = "CATEGORIAS"
        tabLayout!!.getTabAt(1)!!.text = "TODAS LAS TIENDAS"
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
            /*R.id.agregar -> {
                //onAddItemsClicked()
                true
            }*/
            R.id.signOut -> {
                signOut(provider)
                true
            }
            else -> super.onOptionsItemSelected(item)
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

}