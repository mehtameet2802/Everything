package com.example.myapplication

import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.*
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import java.util.*

class sign_up : AppCompatActivity() {
    var count = 0

    var storage = Firebase.storage
    var storageRef = storage.reference
    val mAuth = Firebase.auth
    val db = Firebase.firestore
    lateinit var imageUri: Uri
    lateinit var profile: ImageView
    lateinit var Email: EditText
    var iurl: String = ""

    lateinit var Password: EditText
    lateinit var Username: EditText
    lateinit var Birthdate: TextView

    override fun onCreate(savedInstanceState: Bundle?) {

        supportActionBar?.hide()
        count = 0

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        storage = FirebaseStorage.getInstance()
        storageRef = storage.reference

        Email = findViewById<EditText>(R.id.email2)
        Password = findViewById<EditText>(R.id.password)
        Username = findViewById<EditText>(R.id.username2)
        Birthdate = findViewById<TextView>(R.id.birthdate)

        val sign_up = findViewById<Button>(R.id.mTrailer)
        profile = findViewById<ImageView>(R.id.sign_up)
        val change = findViewById<Button>(R.id.change)
        val save = findViewById<Button>(R.id.save)


        Birthdate.setOnClickListener {

            // making calender
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            // showing calender and setting bithdate

            val date = DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { view, year, month, day ->
                    Birthdate.text = "" + day + "/" + month + "/" + year
                },
                year,
                month,
                day
            )
            date.show()
        }


        save.setOnClickListener {
            if (count == 1) {
                uploadpic()
            } else {
                Toast.makeText(this, "First change the image", Toast.LENGTH_SHORT).show()
            }
        }

        change.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
//            val openGalleryIntent =
//                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
//            startActivityForResult(openGalleryIntent, 1000)
            startActivityForResult(intent, 1000)
        }

        sign_up.setOnClickListener {
            val intent = Intent(application, Login::class.java)
            val email = Email.text.toString()
            val username = Username.text.toString()
            val birthdate = Birthdate.text.toString()
            val password = Password.text.toString()


            if (email == "" && username == "" && birthdate == "" && password == "") {
                Toast.makeText(
                    this,
                    "Please enter email id, username and birthdate",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (email == "") {
                Toast.makeText(this, "Please enter username", Toast.LENGTH_SHORT).show()
            } else if (username == "") {
                Toast.makeText(this, "Please enter username", Toast.LENGTH_SHORT).show()
            } else if (birthdate == "") {
                Toast.makeText(this, "Please enter birthdate", Toast.LENGTH_SHORT).show()
            } else if (password == "") {
                Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show()
            } else {

//                db.collection("users").whereEqualTo("Email", email.trim().toString())
//                    .get().addOnSuccessListener {
//                        val intent1 = Intent(this, Login::class.java)
//                        startActivity(intent1)
//                        Toast.makeText(
//                            this,
//                            "Your account already exits \nPlease sign in ",
//                            Toast.LENGTH_LONG
//                        ).show()
//                        finish()
//                    }
//                    .addOnFailureListener {
                firestore_data(email, username, birthdate, password.trim().toString(), iurl, intent)
//                    }
            }
        }

        if (savedInstanceState != null) {
            val sUserName = savedInstanceState.getInt("user_name")
            val sPassword = savedInstanceState.getInt("password")
            val sBirthdate = savedInstanceState.getInt("birthdate")
            val sEmail = savedInstanceState.getInt("email")


            Username.setText(sUserName.toString())
            Password.setText(sPassword.toString())
            Birthdate.text = sBirthdate.toString()
            Email.setText(sEmail.toString())
        }

    }


    fun firestore_data(
        email: String,
        username: String,
        birthdate: String,
        password: String,
        iurl: String,
        i: Intent,
    ) {
        val user = hashMapOf(
            "Birthdate" to birthdate,
            "Email" to email,
            "Username" to username,
            "Password" to password,
            "iurl" to iurl
        )

        db.collection("users").document(email).set(user)
            .addOnSuccessListener {
                Toast.makeText(this, "Please login", Toast.LENGTH_SHORT).show()
                mAuth.createUserWithEmailAndPassword(email, password.trim().toString())
                i.putExtra("email", email)
                startActivity(i)
                finish()
            }
            .addOnFailureListener { e -> Log.w(ContentValues.TAG, "Error adding the user") }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1000) {
            if (resultCode == RESULT_OK) {
                imageUri = data?.data!!
                profile.setImageURI(imageUri)
                count = 1
            }
        }
    }

    override fun onBackPressed() {
        val intent = Intent(this, Login::class.java)
        startActivity(intent)
        finish()
    }

    private fun uploadpic() {
        iurl = UUID.randomUUID().toString()

        val progress = ProgressDialog(this)
        progress.setMessage("Uploading file")
        progress.setCancelable(false)
        progress.show()

        val image = storageRef.child("images/$iurl")
        image.putFile(imageUri)
// Register observers to listen for when the download is done or if it fails
            .addOnFailureListener {
                // Handle unsuccessful uploads
                if (progress.isShowing) {
                    progress.dismiss()
                    Toast.makeText(this, "Failed to upload the image", Toast.LENGTH_SHORT)
                        .show()
                }
            }.addOnSuccessListener { taskSnapshot ->
                Toast.makeText(this, "Image has been uploaded", Toast.LENGTH_SHORT).show()
                if (progress.isShowing) {
                    progress.dismiss()
                }
                // taskSnapshot.metadata contains file metadata such as size, content-type, etc.


            }
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        outState.putString("user_name", Username.text.toString())
        outState.putString("birthdate", Birthdate.text.toString())
        outState.putString("password", Password.text.toString())
        outState.putString("email123", Email.text.toString())
    }

}