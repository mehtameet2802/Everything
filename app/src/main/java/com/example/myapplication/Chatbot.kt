package com.example.myapplication

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Constants.OPEN_GOOGLE
import com.example.myapplication.Constants.OPEN_SEARCH
import com.example.myapplication.Constants.RECIEVE_ID
import com.example.myapplication.Constants.SEND_ID
import kotlinx.coroutines.*

class Chatbot : AppCompatActivity() {

    private lateinit var adapter:MsgAdapter
    private val botList = listOf("Peter","Iza","Liza","Igor","Jarvis")
    lateinit var rv:RecyclerView
    lateinit var msg:EditText
    lateinit var send:Button
    var messagesList = mutableListOf<msg>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chatbot)

        rv = findViewById(R.id.rvMsg)
        msg = findViewById(R.id.msg)
        send = findViewById(R.id.send)

        recyclerView()

        clickEvents()

        val random = (0..4).random()
        customMessage("Hello! Today you're speaking with ${botList[random]}, how may I help?")
        
    }

    private fun clickEvents() {
        send.setOnClickListener {
            sendMsg()
        }

        msg.setOnClickListener{
            GlobalScope.launch {
                delay(100)
                withContext(Dispatchers.Main){
                    rv.scrollToPosition(adapter.itemCount-1)
                }
            }
        }
    }

    private fun recyclerView(){
        adapter = MsgAdapter()
        rv.adapter = adapter
        rv.layoutManager = LinearLayoutManager(this)
    }


    override fun onStart() {
        super.onStart()

        GlobalScope.launch {
            delay(100)
            withContext(Dispatchers.Main){
                rv.scrollToPosition(adapter.itemCount-1)
            }
        }

    }

    private fun sendMsg(){
        val message = msg.text.toString()
        val timeStamp = Time.timeStamp()

        if(message.isNotEmpty()){
            messagesList.add(msg(message, SEND_ID, timeStamp))
            msg.setText("")

            adapter.insertMsg(msg(message,SEND_ID,timeStamp))
            rv.scrollToPosition(adapter.itemCount-1)

            botResponse(message)

        }

    }

    private fun botResponse(message: String) {

        val timeStamp = Time.timeStamp()

        GlobalScope.launch {
            delay(1000)
            withContext(Dispatchers.Main){
                val response = BotResponse.basicResponse(message)
                messagesList.add(msg(message, RECIEVE_ID, timeStamp))
                adapter.insertMsg(msg(response, RECIEVE_ID,timeStamp))

                rv.scrollToPosition(adapter.itemCount-1)

                when(response){
                    OPEN_GOOGLE -> {
                        val site = Intent(Intent.ACTION_VIEW)
                        site.data = Uri.parse("https://www.google.com/")
                        startActivity(site)
                    }
                    OPEN_SEARCH -> {
                        val site = Intent(Intent.ACTION_VIEW)
                        val searchTerm:String? = message.substringAfterLast("search")
                        site.data = Uri.parse("https://www.google.com/search?&q=$searchTerm")
                        startActivity(site)
                    }
                }
            }
        }
    }

    private fun customMessage(message: String) {
        GlobalScope.launch {
            delay(1000)
            withContext(Dispatchers.Main){
                val timeStamp = Time.timeStamp()
                messagesList.add(msg(message, RECIEVE_ID, timeStamp))
                adapter.insertMsg(msg(message,RECIEVE_ID,timeStamp))
                rv.scrollToPosition(adapter.itemCount-1)
            }
        }
    }
}