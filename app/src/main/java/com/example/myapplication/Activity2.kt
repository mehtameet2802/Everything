package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Activity2 : AppCompatActivity() {

    lateinit var data: ArrayList<information2.Result>
    lateinit var rv: RecyclerView

    lateinit var toggle: ActionBarDrawerToggle
    lateinit var drawer: DrawerLayout
    private lateinit var toolbar: Toolbar
    lateinit var nav: NavigationView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_2)

        nav = findViewById(R.id.left_list2)

        toolbar = findViewById(R.id.toolbar_list2)
        setSupportActionBar(toolbar)
        drawer = findViewById(R.id.drawer_list2)
        toggle = ActionBarDrawerToggle(this, drawer, toolbar, R.string.open, R.string.close)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toggle.isDrawerIndicatorEnabled = true
        toggle.drawerArrowDrawable.color = resources.getColor(R.color.white)
        drawer.addDrawerListener(toggle)
        toggle.syncState()

        rv = findViewById<RecyclerView>(R.id.rv2)
        rv.apply {
            layoutManager = LinearLayoutManager(this@Activity2)
        }

        val rf = Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/movie/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RetrofitData::class.java)

        val api_data = rf.getUpcoming(1)

        api_data.enqueue(object : Callback<information2> {
            override fun onResponse(call: Call<information2>, response: Response<information2>) {
                data = (response.body()?.results as ArrayList<information2.Result>)
                rv.apply {
                    adapter = Adapter2(data)
                }
            }

            override fun onFailure(call: Call<information2>, t: Throwable) {
                Log.d("error", t.message.toString())
            }

        })


        nav.setNavigationItemSelectedListener {
            drawer.closeDrawer(GravityCompat.START)
            when (it.itemId) {
                R.id.users -> {
                    val intent = Intent(this, UserList::class.java)
                    startActivity(intent)
                    finish()
                }
                R.id.list1 -> {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                R.id.list2 -> {
                    drawer.closeDrawer(GravityCompat.START)
                }
                R.id.pay -> {
                    val intent = Intent(this, payment::class.java)
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


        search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != "") {
                    val new_data = data.filter { movie ->
                        val s = (movie.title).lowercase()
                        newText!!.lowercase().let { s.startsWith(it) }
                    }
                    rv.adapter = Adapter2(new_data as ArrayList<information2.Result>)
                    rv.adapter?.notifyDataSetChanged()
                }
                if (newText == "") {
                    rv.adapter = Adapter2(data)
                    rv.adapter?.notifyDataSetChanged()
                }
                return true
            }

        })
        return true
    }
}