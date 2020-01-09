package com.example.assignment

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_food_detail.*

class FoodDetail : AppCompatActivity() {
lateinit var context: Context
    lateinit var databaseRef: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food_detail)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val i = intent
        val owner = i.getStringExtra("owner")
        val foodname = i.getStringExtra("item")
        val desc = i.getStringExtra("description")
        val ptime  = i .getStringExtra("pickuptime")
        val location = i.getStringExtra("location")

        var ownernametext: TextView = findViewById(R.id.ownername)
        var foodnametext: TextView = findViewById(R.id.foodname)
        var fooddesctext: TextView = findViewById(R.id.fooddescription)
        var ptimetext: TextView = findViewById(R.id.pickuptime)
        var locationtext: TextView = findViewById(R.id.location)

        Picasso.with(this).load(i.getStringExtra("img")).into(itemImg)
        ownernametext.text = owner
        foodnametext.text = foodname
        fooddesctext.text = desc
        ptimetext.text = ptime
        locationtext.text = location
        var button: Button = findViewById(R.id.button)
        button.setOnClickListener{
            requestfood()
        }
    }
    private fun requestfood() {
        databaseRef = FirebaseDatabase.getInstance().getReference("Requests")
        val uid = FirebaseAuth.getInstance().currentUser!!.uid
        val i = intent
        val id= i.getStringExtra("id")
        val desc = i.getStringExtra("description")
        val img = i.getStringExtra("img")
        val owner = i.getStringExtra("owner")
        val foodid= i.getStringExtra("foodid")
        val foodname = i.getStringExtra("item")
        val status = "Requested"
        val ptime  = i .getStringExtra("pickuptime")
        val location = i.getStringExtra("location")
        val createDate = i.getStringExtra("createDate")
        val req = Requests(uid,owner,foodid,foodname,status)
        databaseRef.child(foodid).setValue(req)
        updateFoodStatus(id,img,foodid,foodname,desc,ptime,location,createDate)
        Toast.makeText(applicationContext, "Item Requested", Toast.LENGTH_LONG).show()
    }
    fun updateFoodStatus(userid: String,uri:String,foodId:String,name:String,desc:String,date:String,locate: String,createDate:String):Boolean{
        val status = "Requested"
        databaseRef = FirebaseDatabase.getInstance().getReference("Foods").child(foodId)
        val food: FoodEntity = FoodEntity(userid,uri,foodId,name,desc,date,locate,createDate,status)
        databaseRef.setValue(food)
        return true
    }
}
