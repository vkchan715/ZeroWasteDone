package com.example.assignment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

class FoodDetail : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food_detail)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val i = intent
        val img = i.getStringExtra("img")
        val owner = i.getStringExtra("owner")
        val foodname = i.getStringExtra("item")
        val desc = i.getStringExtra("description")
        val ptime  = i .getStringExtra("pickuptime")
        val location = i.getStringExtra("location")

        var itemimg: ImageView = findViewById(R.id.itemImg)
        var ownernametext: TextView = findViewById(R.id.ownername)
        var foodnametext: TextView = findViewById(R.id.foodname)
        var fooddesctext: TextView = findViewById(R.id.fooddescription)
        var ptimetext: TextView = findViewById(R.id.pickuptime)
        var locationtext: TextView = findViewById(R.id.location)

        ownernametext.text = owner
        foodnametext.text = foodname
        fooddesctext.text = desc
        ptimetext.text = ptime
        locationtext.text = location
        var button: Button = findViewById(R.id.button)
        button.setOnClickListener{
            addfood()
        }
    }
    private fun addfood() {
        Toast.makeText(applicationContext, " Nice", Toast.LENGTH_LONG).show()
    }
}
