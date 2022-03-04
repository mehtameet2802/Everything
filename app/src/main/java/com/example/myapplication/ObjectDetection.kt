package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.myapplication.databinding.ActivityObjectDetectionBinding

class ObjectDetection : AppCompatActivity() {

    private lateinit var binding:ActivityObjectDetectionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityObjectDetectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.resultImage.visibility = View.INVISIBLE

        binding.btnImage.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(intent,1000)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 1000){
            if(resultCode == RESULT_OK){
                binding.viewImage.setImageURI(data!!.data)
            }
        }
    }
}