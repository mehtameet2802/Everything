package com.example.myapplication

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.collections.ArrayList

class UserList : AppCompatActivity() {

    val mAuth:FirebaseAuth = Firebase.auth
    lateinit var toggle: ActionBarDrawerToggle
    lateinit var drawer: DrawerLayout
    private lateinit var toolbar: Toolbar
    lateinit var nav: NavigationView

    lateinit var data: ArrayList<data>
    lateinit var rv: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_list)

        nav = findViewById(R.id.left_user)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        drawer = findViewById(R.id.drawer_user)
        toggle = ActionBarDrawerToggle(this, drawer, toolbar, R.string.open, R.string.close)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toggle.isDrawerIndicatorEnabled = true
        toggle.drawerArrowDrawable.color = resources.getColor(R.color.white)
        drawer.addDrawerListener(toggle)
        toggle.syncState()

        val bt = findViewById<FloatingActionButton>(R.id.fv3)

//        lateinit var data:ArrayList<data>
        rv = findViewById<RecyclerView>(R.id.rv3)

        rv.apply {
            layoutManager = LinearLayoutManager(this@UserList)
        }

//      https://gorest.co.in/public/v2/users
        val rf = Retrofit.Builder()
            .baseUrl("https://gorest.co.in/public/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RetrofitData::class.java)

        val api_data = rf.user()

        api_data.enqueue(object : Callback<List<data>> {
            override fun onFailure(call: Call<List<data>>, t: Throwable) {
                Log.d("error", t.message.toString())
                Log.d("error", "Some problem")
            }

            override fun onResponse(call: Call<List<data>>, response: Response<List<data>>) {
                data = response.body() as ArrayList<data>
                Log.d("data", data.toString())
                if (data != null) {
                    rv.apply {
                        adapter = Adapter_List(response.body() as ArrayList<data>, this@UserList)
                    }
                } else {
                    Log.d("error", "data is null")
                }
            }
        }
        )

        bt.setOnClickListener {
            val dialog = Dialog(this)
            dialog.setContentView(R.layout.create_dialog)
            dialog.setCancelable(false)
            val cancel = dialog.findViewById<Button>(R.id.cancel)
            val create = dialog.findViewById<Button>(R.id.create)
            val name = dialog.findViewById<EditText>(R.id.name)
            val email = dialog.findViewById<EditText>(R.id.email)
            val status = dialog.findViewById<EditText>(R.id.status)
            val gender = dialog.findViewById<EditText>(R.id.gender)
            cancel.setOnClickListener {
                Toast.makeText(this, "User not create", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
            create.setOnClickListener {
                if (name.text.isNullOrEmpty() || email.text.isNullOrEmpty() || status.text.isNullOrEmpty() || gender.text.isNullOrEmpty()) {
                    Toast.makeText(
                        this,
                        "Please make sure you have entered data in all fields",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(this, "user created", Toast.LENGTH_SHORT).show()

                    val post_data = data(
                        "",
                        email.text.toString(),
                        gender.text.toString(),
                        name.text.toString(),
                        status.text.toString()
                    )

                    val post = rf.createUser(post_data)
                    post.enqueue(object : Callback<data> {
                        override fun onResponse(
                            call: Call<data>,
                            response: Response<data>
                        ) {
                            if (response.code() == 201) {
                                Toast.makeText(
                                    this@UserList,
                                    "Data added successfully",
                                    Toast.LENGTH_SHORT
                                ).show()
                                Log.d("data", response.body().toString())
                                email.setText("")
                            } else if (response.code() == 422) {
                                Toast.makeText(
                                    this@UserList,
                                    "Email already used",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                        override fun onFailure(call: Call<data>, t: Throwable) {
                            Toast.makeText(
                                this@UserList,
                                "Data not added try again",
                                Toast.LENGTH_SHORT
                            ).show()
                            Log.d("error", "Data not added successfully")
                        }

                    })
                    dialog.dismiss()
                }
            }
            dialog.show()
        }

        nav.setNavigationItemSelectedListener {
            drawer.closeDrawer(GravityCompat.START)
            when (it.itemId) {
                R.id.users -> {
                    drawer.closeDrawer(GravityCompat.START)
                }
                R.id.list1 -> {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                R.id.list2 -> {
                    val intent = Intent(this, Activity2::class.java)
                    startActivity(intent)
                    finish()
                }
                R.id.logout ->{
                    val intent = Intent(this,Login::class.java)
                    startActivity(intent)
                    finish()
                    mAuth.signOut()
                }
                R.id.pay -> {
                    val intent = Intent(this,payment::class.java)
                    startActivity(intent)
                    finish()
                }
                R.id.chat -> {
                    val intent = Intent(this,Chatbot::class.java)
                    startActivity(intent)
                    finish()
                }
                R.id.detection -> {
                    val intent = Intent(this,ObjectDetection::class.java)
                    startActivity(intent)
                }
            }
            true
        }

    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        val search_btn = menu.findItem(R.id.search)
        val search = search_btn?.actionView as SearchView
        search.queryHint = "Search Here"


        val sort = menu.findItem(R.id.sort)
        val asc = menu.findItem(R.id.asc)
        val dsc = menu.findItem(R.id.dsc)
        val filter = menu.findItem(R.id.filter)
        val filter_id = menu.findItem(R.id.f_id)
        val filter_email = menu.findItem(R.id.f_email)

        asc.setOnMenuItemClickListener {
            data.sortWith { o1, o2 -> o1.email.toString().compareTo(o2.email.toString()) }
            rv.adapter!!.notifyDataSetChanged()
            true
        }

        dsc.setOnMenuItemClickListener {
            data.sortWith { o1, o2 -> o2.email.toString().compareTo(o1.email.toString()) }
            rv.adapter!!.notifyDataSetChanged()
            true
        }

        search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != "") {
                    val new_data = data.filter { all_users ->
                        val s = (all_users.email)!!.lowercase()
                        s.startsWith(newText!!.lowercase())
                    }
                    rv.adapter = Adapter_List(new_data as ArrayList<data>, this@UserList)
                    rv.adapter?.notifyDataSetChanged()
                }
                if (newText == "") {
                    rv.adapter = Adapter_List(data, this@UserList)
                    rv.adapter?.notifyDataSetChanged()
                }
                return true
            }

        })
        return true
    }

}
