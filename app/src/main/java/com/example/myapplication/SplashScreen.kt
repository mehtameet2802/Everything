package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.WindowManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.auth.User
import com.google.firebase.ktx.Firebase

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val mAuth = Firebase.auth

        supportActionBar?.hide()

        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)


        Handler(Looper.getMainLooper()).postDelayed({
            if (mAuth.currentUser == null) {
                val intent = Intent(this, Login::class.java)
                startActivity(intent)
            } else {
                val intent = Intent(this, UserList::class.java)
                startActivity(intent)
            }
            finish()
        }, 3000)
    }
}
