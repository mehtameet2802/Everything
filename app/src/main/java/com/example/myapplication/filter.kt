package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.core.view.isInvisible
import androidx.core.view.isVisible

class filter : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flter)

        val group = findViewById<RadioGroup>(R.id.rd1)
        val btId = findViewById<RadioButton>(R.id.rd_id)
        val btEmail = findViewById<RadioButton>(R.id.rd_email)
        val upper = findViewById<TextView>(R.id.upper)
        val lower = findViewById<TextView>(R.id.lower)

        val range1 = findViewById<EditText>(R.id.range1)
        val range2 = findViewById<EditText>(R.id.range2)

        val contains = findViewById<TextView>(R.id.contains)
        val contain = findViewById<EditText>(R.id.ed_contains)

        range1.isInvisible = true
        range2.isInvisible = true
        upper.isInvisible = true
        lower.isInvisible = true
        contains.isInvisible = true
        contain.isInvisible = true

        btId.setOnClickListener {
            contains.isInvisible = true
            contain.isInvisible = true
            range1.isVisible = true
            range2.isVisible = true
            upper.isVisible = true
            lower.isVisible = true


        }

        btEmail.setOnClickListener {
            range1.isInvisible = true
            range2.isInvisible = true
            upper.isInvisible = true
            lower.isInvisible = true
            contains.isVisible = true
            contain.isVisible = true



        }

    }
}