package com.example.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Constants.RECIEVE_ID
import com.example.myapplication.Constants.SEND_ID


class MsgAdapter : RecyclerView.Adapter<MsgAdapter.MsgViewHolder>() {

    var msgList = mutableListOf<msg>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MsgViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.messages, parent, false)
        return MsgViewHolder(view)
    }

    override fun onBindViewHolder(holder: MsgViewHolder, position: Int) {
        val curMsg = msgList[position]
        when (curMsg.id) {
            SEND_ID -> {
                holder.send.apply {
                    text = curMsg.msg
                    visibility = View.VISIBLE
                }
                holder.recieve.visibility = View.GONE
            }
            RECIEVE_ID -> {
                holder.recieve.apply {
                    text = curMsg.msg
                    visibility = View.VISIBLE
                }
                holder.send.visibility = View.GONE
            }
        }
    }

    override fun getItemCount(): Int {
        return msgList.size
    }

    inner class MsgViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var send = itemView.findViewById<TextView>(R.id.sendMessage)
        var recieve = itemView.findViewById<TextView>(R.id.recieveMessage)

        init {
            itemView.setOnClickListener {
                msgList.removeAt(adapterPosition)
                notifyItemRemoved(adapterPosition)
            }
        }
    }

    fun insertMsg(message:msg){
        this.msgList.add(message)
        notifyItemInserted(msgList.size)
    }
}