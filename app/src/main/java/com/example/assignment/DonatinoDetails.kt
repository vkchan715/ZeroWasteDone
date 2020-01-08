package com.example.assignment

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso

class DonatinoDetails : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_donatino_details)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        var i = intent
        val img = i.getStringExtra("img")
        val title = i.getStringExtra("title")
        val date = i.getStringExtra("date")
        val desc = i.getStringExtra("description")
        val price  = i .getStringExtra("price")
        val location = i.getStringExtra("location")

        var itemimg: ImageView = findViewById(R.id.itemImg)
        var titletext: TextView = findViewById(R.id.title)
        var datetext: TextView = findViewById(R.id.date)
        var desctext: TextView = findViewById(R.id.description)
        var pricetext: TextView = findViewById(R.id.price)
        var donatebtn: Button = findViewById(R.id.donate)

        Picasso.with(this).load(img).into(itemimg)
        titletext.text = title
        datetext.text = date
        desctext.text = desc
        pricetext.text = price
//        donatebtn.setOnClickListener{
//            i = Intent(this, FoodDetail::class.java)
//
//        }
    }
}
