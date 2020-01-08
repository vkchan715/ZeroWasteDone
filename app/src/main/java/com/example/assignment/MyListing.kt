package com.example.assignment

import android.content.Context
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import java.util.ArrayList

class MyListing : AppCompatActivity() {
    private var mRecyclerView: RecyclerView? =null
    private lateinit var ref: DatabaseReference
    lateinit var ref1: DatabaseReference
    lateinit var Requestref: DatabaseReference
    private val data = ArrayList<FoodEntity>()
    private var mListadapter: ListAdapter? = null
    private val data1 = ArrayList<User>()
    private val data2 = ArrayList<Requests>()
    private var requid: String = ""
    private var reqName: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_requests)

        mRecyclerView = findViewById(R.id.recyclerView)

        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        mRecyclerView!!.layoutManager = layoutManager

        ref = FirebaseDatabase.getInstance().getReference("Foods")

        ref.addValueEventListener(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                if(p0!!.exists()){
                    data.clear()
                    for(h in p0.children){
                        val food = h.getValue(FoodEntity::class.java)
                        if(food!!.userid == FirebaseAuth.getInstance().currentUser!!.uid){
                            data.add(food!!)
                        }

                    }
                    mListadapter = ListAdapter(data)
                    mRecyclerView!!.adapter = mListadapter
                }
            }

        })
        ref1 = FirebaseDatabase.getInstance().getReference("Users")

        ref1.addValueEventListener(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }
            override fun onDataChange(p0: DataSnapshot) {
                data1.clear()
                if(p0!!.exists()){
                    for(h in p0.children){
                        val u = h.getValue(User::class.java)
                        data1.add(u!!)
                    }
                }
            }

        })
        Requestref = FirebaseDatabase.getInstance().getReference("Requests")
        Requestref.addValueEventListener(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }
            override fun onDataChange(p0: DataSnapshot) {
                data2.clear()
                if(p0!!.exists()){
                    for(h in p0.children){
                        val u = h.getValue(Requests::class.java)
                        data2.add(u!!)
                    }
                }
            }

        })
    }
    inner class ListAdapter(private val dataList: ArrayList<FoodEntity>) :
        RecyclerView.Adapter<ListAdapter.ViewHolder>() {

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            internal var imgview: ImageView
            internal var textViewName: TextView
            internal var textViewStatus: TextView
            internal var textViewRequestby: TextView
            internal lateinit var btnApp: Button

            init {
                this.imgview = itemView.findViewById<View>(R.id.imageView) as ImageView
                this.textViewName = itemView.findViewById<View>(R.id.itemname) as TextView
                this.textViewStatus = itemView.findViewById<View>(R.id.status) as TextView
                this.textViewRequestby = itemView.findViewById<View>(R.id.textViewRequestby) as TextView
                this.btnApp = itemView.findViewById<View>(R.id.btnApp) as Button
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListAdapter.ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.listing_item, parent, false)

            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ListAdapter.ViewHolder, position: Int) {
            var reqby = "Requested by: "
            if(dataList[position].status.equals("Requested")) {
                for (i in 0..data2.size) {
                    if (dataList[position].foodId.equals(data2[i].foodid)) {
                        requid = data2[i].uid
                        break
                    }
                }
                for (i in 0..data1.size) {
                    if (data1[i].id.equals(requid)) {
                        reqName = data1[i].username
                        break
                    }
                }
                holder.textViewRequestby.text = reqby + reqName
            }else{
                holder.textViewRequestby.text = ""
                holder.btnApp.visibility = View.GONE
            }
            Picasso.with(this@MyListing).load(dataList[position].uri).into(holder.imgview)
            holder.textViewName.text = dataList[position].name
            holder.textViewStatus.text = dataList[position].status

            holder.btnApp.setOnClickListener {
                updateFoodStatus(dataList[position].userid,
                    dataList[position].uri,
                    dataList[position].foodId,
                    dataList[position].name,
                    dataList[position].desc,
                    dataList[position].createDate,
                    dataList[position].locate,
                    dataList[position].date)
            }
        }

        override fun getItemCount(): Int {
            return dataList.size
        }
        fun updateFoodStatus(userid: String,uri:String,foodId:String,name:String,desc:String,date:String,locate: String,createDate:String):Boolean{
            val status = "Approved"
            ref = FirebaseDatabase.getInstance().getReference("Foods").child(foodId)
            val food: FoodEntity = FoodEntity(userid,uri,foodId,name,desc,date,locate,createDate,status)
            ref.setValue(food)
            return true
        }
    }
}
