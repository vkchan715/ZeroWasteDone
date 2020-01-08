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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_make_donation)
        var i = intent
        val id = i.getStringExtra("donateid")
        var amounttext: EditText = findViewById(R.id.amount)
        var a:Int = amounttext.toString().toInt()
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
                    a = a + data[i].donatedPrice.toInt()
                    updateDonation(data[i].id, data[i].title, data[i].uri, data[i].detail, data[i].date, data[i].targetPrice, a.toString())
                    break
                }
            }
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
