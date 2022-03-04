package com.example.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import androidx.recyclerview.widget.RecyclerView

class Adapter(var data:ArrayList<information.Result>):RecyclerView.Adapter<Adapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.card_layout,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder:ViewHolder, position: Int) {
        val input = data[position]
        if(input!=null) {
            holder.bind(input)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class ViewHolder(v: View):RecyclerView.ViewHolder(v){
        var text = v.findViewById<TextView>(R.id.text)
        var image = v.findViewById<ImageView>(R.id.image)

        private val imageBase="https://image.tmdb.org/t/p/w500/"

        fun bind(data1: information.Result){
            text.setText(data1.title)
            Glide.with(itemView)
                .load(imageBase+data1.poster_path)
                .into(image)
        }
    }
}