package com.example.myapplication

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
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

class MainActivity : AppCompatActivity() {

    lateinit var data: ArrayList<information.Result>
    lateinit var rv: RecyclerView

    lateinit var toggle: ActionBarDrawerToggle
    lateinit var drawer: DrawerLayout
    private lateinit var toolbar: Toolbar
    lateinit var nav: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        nav = findViewById(R.id.left_list1)

        toolbar = findViewById(R.id.toolbar_list1)
        setSupportActionBar(toolbar)
        drawer = findViewById(R.id.drawer_list1)
        toggle = ActionBarDrawerToggle(this, drawer, toolbar, R.string.open, R.string.close)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toggle.isDrawerIndicatorEnabled = true
        toggle.drawerArrowDrawable.color = resources.getColor(R.color.white)
        drawer.addDrawerListener(toggle)
        toggle.syncState()


        rv = findViewById(R.id.rv)

        rv.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
        }

        val rf = Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/movie/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RetrofitData::class.java)

        val api_data = rf.getMovies(1)

        api_data.enqueue(object : Callback<information> {
            override fun onResponse(call: Call<information>, response: Response<information>) {
                data = (response.body()?.results as ArrayList<information.Result>)
                rv.apply {
                    adapter = Adapter(data)
                }
            }

            override fun onFailure(call: Call<information>, t: Throwable) {
                Log.e(TAG, t.message.toString())
                Log.d("error", "some problem")
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
                    drawer.closeDrawer(GravityCompat.START)

                }
                R.id.list2 -> {
                    val intent = Intent(this, Activity2::class.java)
                    startActivity(intent)
                    finish()
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
                    rv.adapter = Adapter(new_data as ArrayList<information.Result>)
                    rv.adapter?.notifyDataSetChanged()
                }
                if (newText == "") {
                    rv.adapter = Adapter(data)
                    rv.adapter?.notifyDataSetChanged()
                }
                return true
            }

        })
        return true
    }


}