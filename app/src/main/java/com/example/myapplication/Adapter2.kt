package com.example.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class Adapter2(var data:ArrayList<information2.Result>):RecyclerView.Adapter<Adapter2.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.card_layout,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder:ViewHolder, position: Int) {
        val input = data[position]
        if(input!=null){
            holder.bind(input)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }



    class ViewHolder(v: View):RecyclerView.ViewHolder(v){
        val image = v.findViewById<ImageView>(R.id.image)
        val text = v.findViewById<TextView>(R.id.text)

        private val imageBase="https://image.tmdb.org/t/p/w500/"

        fun bind(data2:information2.Result){
            text.setText(data2.title)
            Glide.with(itemView)
                .load(imageBase+data2.poster_path)
                .into(image)
        }
    }

}