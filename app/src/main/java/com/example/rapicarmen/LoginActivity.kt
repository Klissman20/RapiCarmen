package com.example.rapicarmen

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import android.widget.Button
import android.widget.EditText
import com.google.firebase.auth.FacebookAuthProvider
import androidx.appcompat.app.AlertDialog
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var auth_listener: FirebaseAuth.AuthStateListener

    private val GOOGLE_SIGN_IN=100
    private val FACEBOOK_SIGN_IN=200
    private val callbackManager = CallbackManager.Factory.create()

    var btn_signin: Button? = null
    var btn_signup: Button? = null
    var txt_email: EditText? = null
    var txt_pass: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        Thread.sleep(2000)
        setTheme(R.style.AppTheme)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btn_signin = findViewById<Button>(R.id.btn_signIn)
        btn_signup = findViewById<Button>(R.id.btn_signUp)
        txt_email = findViewById<EditText>(R.id.txt_email)
        txt_pass = findViewById<EditText>(R.id.txt_pass)

        auth = FirebaseAuth.getInstance()

        //Analytic Event
        val analytics= FirebaseAnalytics.getInstance(this)
        val bundle=Bundle()
        auth = FirebaseAuth.getInstance()
        bundle.putString("message","Integración de Firebase completa")
        analytics.logEvent("InitScreen", bundle)

        setup()
        session()
    }

    override fun onStart(){
        super.onStart()
        authLayout.visibility= View.VISIBLE
    }

    private fun session(){
        val prefs=getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        val email=prefs.getString ("email",null)
        val provider=prefs.getString("provider",null)
        if(email!=null&&provider!=null){
            authLayout.visibility= View.INVISIBLE
            showHome(email,ProviderType.valueOf(provider))
        }
    }

    private fun setup() {
        //Autenticación
        /*
        btn_signUp.setOnClickListener {
            if(txt_email.text.isNotEmpty() && txt_pass.text.isNotEmpty()){
                auth.createUserWithEmailAndPassword(txt_email.text.toString(),txt_pass.text.toString()).addOnCompleteListener {
                    if(it.isSuccessful){
                        showHome(it.result?.user?.email?:"",ProviderType.BASIC)

                    }else{
                        showAlert()
                    }
                }
            }
        }
         */

        btn_signin!!.setOnClickListener {
            if (txt_email!!.text.isNotEmpty() && txt_pass!!.text.isNotEmpty()) {
                auth.signInWithEmailAndPassword(txt_email!!.text.toString(), txt_pass!!.text.toString())
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            showHome(it.result?.user?.email ?: "", ProviderType.BASIC)

                        } else {
                            showAlert()
                        }
                    }
            }
        }

        googleButton.setOnClickListener {
            //Configurar autenticacion
            val googleConf=GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build()
            val googleClient= GoogleSignIn.getClient(this,googleConf)
            googleClient.signOut()
            startActivityForResult(googleClient.signInIntent, GOOGLE_SIGN_IN)
        }

        facebookButton.setOnClickListener {
            LoginManager.getInstance().logInWithReadPermissions(this, listOf("email"))
            LoginManager.getInstance().registerCallback(callbackManager,object: FacebookCallback<LoginResult>{
                override fun onSuccess(result: LoginResult?) {
                    result?.let {
                        val token = it.accessToken
                        val credential = FacebookAuthProvider.getCredential(token.token)
                        FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener {
                            if (it.isSuccessful) {
                                showHome(it.result?.user?.email ?: "", ProviderType.FACEBOOK)
                            } else {
                                showAlert()
                            }
                        }
                    }
                }

                override fun onCancel() {

                }

                override fun onError(error: FacebookException?) {
                    showAlert()
                }
            } )
        }
    }

    private fun showAlert() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Se ha producido un error autenticando al usuario")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun showHome(email:String, provider: ProviderType){
        val intent= Intent(this,MainActivity::class.java).apply{
            putExtra("email",email)
            putExtra("provider",provider.name)
        }
        startActivity(intent)
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode,resultCode,data)//FACEBOOK
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == GOOGLE_SIGN_IN) {//GOOGLE
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)

                if (account != null) {
                    val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                    FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener {
                        if (it.isSuccessful) {
                            showHome(account.email ?: "", ProviderType.GOOGLE)
                        } else {
                            showAlert()
                        }
                    }
                }
            } catch(e: ApiException){
                showAlert()
            }
        }
    }

}
