package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class Login : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth

    lateinit var email:EditText
    lateinit var password: EditText
    companion object {
        private var RC_SIGN_IN = 100
    }

    private lateinit var mGoogleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mAuth = Firebase.auth

        supportActionBar?.hide()

        val login = findViewById<Button>(R.id.btnlogin)
        email = findViewById<EditText>(R.id.email)
        password = findViewById<EditText>(R.id.password1)

        login.setOnClickListener {
            val email1 = email.text.trim().toString()
            val password1 = password.text.trim().toString()
            if (email1 == "" && password1 == "") {
                Toast.makeText(this, "Please enter email id and username", Toast.LENGTH_SHORT)
                    .show()
            } else if (email1 == "") {
                Toast.makeText(this, "Please enter username", Toast.LENGTH_SHORT).show()
            } else if (password1 == "") {
                Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show()
            } else {
                emailSignIn(email1,password1)
                email.setText("")
                password.setText("")
            }
        }

        val signup = findViewById<TextView>(R.id.sign_up)
        signup.setOnClickListener {
            val intent = Intent(application, sign_up::class.java)
            startActivity(intent)
            email.setText("")
            password.setText("")
            finish()
        }

//        val phone = findViewById<Button>(R.id.plogin)
//        phone.setOnClickListener {
//            val intent = Intent(application, phoneLogin::class.java)
//            startActivity(intent)
//        }


        val google = findViewById<Button>(R.id.glogin)
        google.setOnClickListener {

            val currentUser = mAuth.currentUser
            updateUI(currentUser)

            // Configure sign-in to request the user's ID, email address, and basic
// profile. ID and basic profile are included in DEFAULT_SIGN_IN.
            // Configure sign-in to request the user's ID, email address, and basic
// profile. ID and basic profile are included in DEFAULT_SIGN_IN.
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
//
            // Build a GoogleSignInClient with the options specified by gso.
            mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

            signIn()
        }

        if(savedInstanceState != null){

            val sPassword = savedInstanceState.getInt("password")
            val sEmail = savedInstanceState.getInt("email")

            password.setText(sPassword.toString())
            email.setText(sEmail.toString())
        }

    }

    override fun onStart() {
        super.onStart()
        if (mAuth.currentUser != null && mAuth.currentUser!!.email != null) {
            val intent = Intent(this, UserList::class.java)
            startActivity(intent)
            finish()
        } else if (mAuth.currentUser != null) {
            val intent = Intent(this, UserList::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun signIn() {
        val signInIntent = Intent(mGoogleSignInClient.signInIntent)
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val user = completedTask.getResult(ApiException::class.java)
            firebaseAuthWithGoogle(user!!.idToken)
//            Toast.makeText(this,user.displayName,Toast.LENGTH_SHORT).show()
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.d("Message", e.toString())
            Toast.makeText(this, "Please sign in", Toast.LENGTH_SHORT).show()
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String?) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
//                    Log.d(TAG, "signInWithCredential:success")
                    val user = mAuth.currentUser
                    updateUI(user)
                    val intent = Intent(this, UserList::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    // If sign in fails, display a message to the user.
//                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    updateUI(null)
                }
            }
    }

    private fun emailSignIn(email: String, password: String) {
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val intent = Intent(application, UserList::class.java)
                    intent.putExtra("email", email)
                    startActivity(intent)
                    Toast.makeText(this, "Welcome " + {
                        mAuth.currentUser!!.email
                    }, Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Log.d("login error",task.exception.toString())
                    Toast.makeText(
                        this,
                        "Wrong email id or password \nPlease try again",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun updateUI(user: FirebaseUser?) {}


    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        outState.putString("email",email.text.toString())
        outState.putString("password",password.text.toString())
    }

}
