package com.example.assignment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso

class VolunteerDetails : AppCompatActivity() {
lateinit var ref: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_volunteer_details)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val i = intent
        val img = i.getStringExtra("img")
        val title = i.getStringExtra("title")
        val date = i.getStringExtra("date")
        val desc = i.getStringExtra("description")
        val price = i.getStringExtra("progress")
        val location = i.getStringExtra("location")
        val phoneno = i.getStringExtra("phonenumber")
        val targetnum=i.getStringExtra("targetnum")
        val num=i.getStringExtra("num")
        val id=i.getStringExtra("id")

        var itemimg: ImageView = findViewById(R.id.itemImg)
        var titletext: TextView = findViewById(R.id.title)
        var datetext: TextView = findViewById(R.id.date)
        var desctext: TextView = findViewById(R.id.description)
        var pricetext: TextView = findViewById(R.id.price)
        var locationtext: TextView = findViewById(R.id.location)
        var phonetext: TextView = findViewById(R.id.phonenumber)


        Picasso.with(this).load(img).into(itemimg)
        titletext.text = title
        datetext.text = date
        desctext.text = desc
        pricetext.text = price
        locationtext.text = location
        phonetext.text = phoneno
        var button: Button = findViewById(R.id.donate)

        button.setOnClickListener {
            updatevolunteer(img,
                title,
                date,
                desc,
                price,
                location,
                phoneno,targetnum,num,id)
            Toast.makeText(applicationContext, "You are joined", Toast.LENGTH_LONG).show()
        }
    }
    fun updatevolunteer(img:String,title:String,date:String,desc:String,price:String,location:String,phoneno:String,target:String,num:String,id:String):Boolean{
        val n = Integer.parseInt(num) + 1
        ref = FirebaseDatabase.getInstance().getReference("Volunteer").child(id)
        val v:VolunteerEntity= VolunteerEntity(id,title,img,desc,date,location,phoneno,target,n.toString())
        ref.setValue(v)
        return true
    }
}
