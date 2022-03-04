package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class Adapter_List(var data:List<data>,val context: Context):RecyclerView.Adapter<Adapter_List.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.list_layout,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val input = data[position]
        if(input!=null){
            holder.bind(input)
        }

        holder.itemView.setOnClickListener {
            val intent = Intent(context,UpdateUser::class.java)
            intent.putExtra("id",input.id.toString())
            intent.putExtra("email",input.email.toString())
            intent.putExtra("gender",input.gender.toString())
            intent.putExtra("name",input.name.toString())
            intent.putExtra("status",input.status.toString())
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class ViewHolder(v:View):RecyclerView.ViewHolder(v){
        val id = v.findViewById<TextView>(R.id.id)
        val email = v.findViewById<TextView>(R.id.email)
        fun bind(data1: data){
            id.setText(data1.id.toString())
            email.setText(data1.email)
        }
    }
}