package com.example.myapplication

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class UpdateUser : AppCompatActivity() {

    lateinit var name:EditText
    lateinit var email:EditText
    lateinit var gender:EditText
    lateinit var status:EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_user)

        val intent = intent
        val dataId = intent.getStringExtra("id").toString()
        val dataName = intent.getStringExtra("name").toString()
        val dataEmail = intent.getStringExtra("email").toString()
        val dataStatus = intent.getStringExtra("status").toString()
        val dataGender = intent.getStringExtra("gender").toString()

        name = findViewById(R.id.up_name)
        email = findViewById(R.id.up_email)
        gender = findViewById(R.id.up_gender)
        status = findViewById(R.id.up_status)

        val save = findViewById<Button>(R.id.save)
        val update = findViewById<Button>(R.id.update)
        val delete = findViewById<Button>(R.id.delete)

        save.isInvisible = true

        name.isEnabled = false
        email.isEnabled = false
        gender.isEnabled = false
        status.isEnabled = false

        name.setText(dataName)
        email.setText(dataEmail)
        gender.setText(dataGender)
        status.setText(dataStatus)

        update.setOnClickListener {
            save.isVisible = true
            name.isEnabled = true
            email.isEnabled = true
            gender.isEnabled = true
            status.isEnabled = true
            update.isInvisible = true
        }

        val rf = Retrofit.Builder()
            .baseUrl("https://gorest.co.in/public/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RetrofitData::class.java)

        save.setOnClickListener {
            val builder1 = AlertDialog.Builder(this)
            builder1.setTitle("Confirm")
            builder1.setCancelable(false)
            builder1.setMessage("Are you sure you want to update the data")
            builder1.setPositiveButton("Save") { dialog, which ->
                if (name.text.isNullOrEmpty() || email.text.isNullOrEmpty() || gender.text.isNullOrEmpty() || status.text.isNullOrEmpty()) {
                    dialog.dismiss()
                    Toast.makeText(this, "Input data in all fields", Toast.LENGTH_SHORT).show()
                } else {
                    val data1 = data(
                        dataId,
                        email.text.toString(),
                        gender.text.toString(),
                        name.text.toString(),
                        status.text.toString()
                    )

                    val apiData = rf.updateUser(dataId, data1)

                    apiData.enqueue(object : Callback<data> {
                        override fun onResponse(call: Call<data>, response: Response<data>) {
                            if (response.isSuccessful) {
                                if (response.code() == 200) {
                                    Toast.makeText(
                                        this@UpdateUser,
                                        "Data update successfully",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    Log.d("data", response.body().toString())
                                    val intent1 = Intent(this@UpdateUser, UserList::class.java)
                                    startActivity(intent1)
                                    finish()
                                } else {
                                    Toast.makeText(
                                        this@UpdateUser,
                                        "Data not updated",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }

                        override fun onFailure(call: Call<data>, t: Throwable) {
                            Toast.makeText(
                                this@UpdateUser,
                                "Sme error occurred please try again",
                                Toast.LENGTH_SHORT
                            ).show()
                            Log.d("data", "some error")
                            Log.d("error", t.message.toString())
                            dialog.dismiss()
                            name.isEnabled = false
                            email.isEnabled = false
                            gender.isEnabled = false
                            status.isEnabled = false
                            save.isVisible = false
                            update.isVisible = true
                        }

                    })

                }


            }
            builder1.setNegativeButton("Cancel") { dialog, which ->
                dialog.dismiss()
            }
            val dialog1: AlertDialog = builder1.create()
            dialog1.show()
        }

        delete.setOnClickListener {
            val builder2 = AlertDialog.Builder(this)
            builder2.setTitle("Delete")
            builder2.setMessage("Are you sure you want to delete the user")
            builder2.setCancelable(false)
            builder2.setPositiveButton("Delete") { dialog, which ->

                val rf1 = Retrofit.Builder()
                    .baseUrl("https://gorest.co.in/public-api/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(RetrofitData::class.java)

                val api = rf1.deleteUser(dataId)

                api.enqueue(object : Callback<response> {
                    override fun onResponse(call: Call<response>, response: Response<response>) {
                        if (response.isSuccessful) {
                            if (response.code() == 200) {
                                Toast.makeText(
                                    this@UpdateUser,
                                    "Deleted Successfully",
                                    Toast.LENGTH_SHORT
                                ).show()
                                Log.d("response", response.body().toString())
                                val intent1 = Intent(this@UpdateUser, UserList::class.java)
                                startActivity(intent1)
                                finish()
                            }
                        } else {
                            Toast.makeText(
                                this@UpdateUser,
                                "Deletion was not successful",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<response>, t: Throwable) {
                        Log.d("error", "Some error occurred")
                        dialog.dismiss()
                    }

                })
            }
            builder2.setNegativeButton("Cancel") { dialog, which ->
                dialog.dismiss()
            }
            val dialog2: AlertDialog = builder2.create()
            dialog2.show()
        }

        if(savedInstanceState != null){
            val sName = savedInstanceState.getInt("name")
            val sStatus = savedInstanceState.getInt("status")
            val sGender = savedInstanceState.getInt("gender")
            val sEmail = savedInstanceState.getInt("email")

            save.isVisible = true
            name.isEnabled = true
            email.isEnabled = true
            gender.isEnabled = true
            status.isEnabled = true
            update.isInvisible = true

            name.setText(sName.toString())
            status.setText(sStatus.toString())
            gender.setText(sGender.toString())
            email.setText(sEmail.toString())
        }

    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        outState.putString("name",name.text.toString())
        outState.putString("status",status.text.toString())
        outState.putString("gender",gender.text.toString())
        outState.putString("email",email.text.toString())
    }
}