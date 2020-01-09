package com.example.assignment

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.util.ArrayList

class MakeDonation : AppCompatActivity() {
    private val data = ArrayList<DonationEntity>()
    lateinit var ref: DatabaseReference
    var num: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_make_donation)
        var i = intent
        val id = i.getStringExtra("donateid")
        val amounttext= findViewById<EditText>(R.id.amountEdit)
        ref = FirebaseDatabase.getInstance().getReference("Donation")

        ref.addValueEventListener(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                if(p0!!.exists()){
                    data.clear()
                    for(h in p0.children){
                        val donate = h.getValue(DonationEntity::class.java)
                        data.add(donate!!)
                    }
                }
            }
        })
        var donatebtn: Button = findViewById(R.id.donate)
        donatebtn.setOnClickListener{
            for(i in 0..data.size){
                if(data[i].id.equals(id)){
                    num = i
                    break
                }
            }
            var a= amounttext.text.toString().toInt()
            a = a+ data[num].donatedPrice.toInt()
            updateDonation(id, data[num].title, data[num].uri, data[num].detail, data[num].date, data[num].targetPrice, a.toString())
            startActivity(Intent(this,Donation::class.java))
        }
    }
    fun updateDonation(id:String, title: String, uri:String,  detail:String, date:String,  targetPrice: String,  donatedPrice: String):Boolean{
        ref = FirebaseDatabase.getInstance().getReference("Donation").child(id)
        val d:DonationEntity = DonationEntity(id,title,uri,detail,date,targetPrice,donatedPrice)
        ref.setValue(d)
        return true
    }
}
