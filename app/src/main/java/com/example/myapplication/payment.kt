package com.example.myapplication

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieAnimationView
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import org.json.JSONObject

class payment : AppCompatActivity(),PaymentResultListener {

    val TAG:String = payment::class.toString()

    lateinit var anime: LottieAnimationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)


        Checkout.preload(applicationContext)

        val amount = findViewById<EditText>(R.id.amount)
        val button: Button = findViewById(R.id.pay)
        button.setOnClickListener {
            val value = amount.text.toString().toDouble()*100

            startPayment(value.toString())

//        anime = findViewById(R.id.payment)
//        anime.isInvisible = true
        }
    }

    fun startPayment(amount:String) {
        /*
        *  You need to pass current activity in order to let Razorpay create CheckoutActivity
        * */
        val co = Checkout()

        co.setKeyID("rzp_test_C33WGVfe5ULgc9")

        try {
            val options = JSONObject()
            options.put("name", "Razorpay Corp")
            options.put("description", "Demoing Charges")
            //You can omit the image option to fetch the image from dashboard
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png")
            options.put("theme.color", "#3399cc");
            options.put("currency", "INR");
//            options.put("order_id", "order_DBJOWzybf0sJbb");
            options.put("amount", amount)//pass amount in currency subunits

//            val retryObj = JSONObject();
//            retryObj.put("enabled", true);
//            retryObj.put("max_count", 4);
//            options.put("retry", retryObj);

            val prefill = JSONObject()
            prefill.put("email", "meetmehta2811@gmail.com")
            prefill.put("contact", "9867023748")

            options.put("prefill", prefill)
            co.open(this, options)
        } catch (e: Exception) {
            Toast.makeText(this, "Error in payment: " + e.message, Toast.LENGTH_LONG).show()
            Log.d("payment error",e.toString())
            e.printStackTrace()
        }
    }

    override fun onPaymentSuccess(p0: String?) {
        Toast.makeText(this, "Payment Successful", Toast.LENGTH_SHORT).show()

    }

    override fun onPaymentError(p0: Int, p1: String?) {
        Toast.makeText(this, "Payment Not Successful", Toast.LENGTH_SHORT).show()

    }
}
