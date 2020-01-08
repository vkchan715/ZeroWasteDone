package com.example.assignment

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.util.ArrayList

class MyRequests : AppCompatActivity() {

    private var mRecyclerView: RecyclerView? =null
    private lateinit var ref: DatabaseReference
    lateinit var ref1: DatabaseReference
    private val data = ArrayList<Requests>()
    private var mListadapter: ListAdapter? = null
    private val data1 = ArrayList<User>()
    private var oname: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_requests)

        mRecyclerView = findViewById(R.id.recyclerView)

        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        mRecyclerView!!.layoutManager = layoutManager

        ref = FirebaseDatabase.getInstance().getReference("Requests")

        ref.addValueEventListener(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                if(p0!!.exists()){
                    data.clear()
                    for(h in p0.children){
                        val req = h.getValue(Requests::class.java)
                        if(req!!.uid== FirebaseAuth.getInstance().currentUser!!.uid){
                            data.add(req!!)
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
                if(p0!!.exists()){
                    for(h in p0.children){
                        val u = h.getValue(User::class.java)
                        data1.add(u!!)
                    }
                }
            }

        })
    }
    inner class ListAdapter(private val dataList: ArrayList<Requests>) :
        RecyclerView.Adapter<ListAdapter.ViewHolder>() {

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            internal var textViewName: TextView
            internal var textViewOwner: TextView
            internal var textViewStatus: TextView

            init {
                this.textViewName = itemView.findViewById<View>(R.id.itemname) as TextView
                this.textViewOwner = itemView.findViewById<View>(R.id.ownername) as TextView
                this.textViewStatus = itemView.findViewById<View>(R.id.status) as TextView
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListAdapter.ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.request_item, parent, false)

            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ListAdapter.ViewHolder, position: Int) {
            for(i in 0..data1.size){
                if(data1[i].id.equals(dataList[position].uid)){
                    oname = data1[i].username
                    break
                }
            }
            holder.textViewName.text = dataList[position].foodname
            holder.textViewOwner.text = oname
            if(dataList[position].status == "Requested"){
                holder.textViewStatus.setTextColor(Color.parseColor("#e1b00e"))
                holder.textViewStatus.text = "Pending"
            }
            else {
                holder.textViewStatus.text = ""
            }

        }

        override fun getItemCount(): Int {
            return dataList.size
        }
    }
}
