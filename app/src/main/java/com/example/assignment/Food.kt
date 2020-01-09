package com.example.assignment


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import java.util.*


/**
 * A simple [Fragment] subclass.
 */
class Food : Fragment() {
    private var mTextViewEmpty: TextView? = null
    private var mProgressBarLoading: ProgressBar? = null
    private var mImageViewEmpty: ImageView? = null
    private var mRecyclerView: RecyclerView? = null
    private var mListadapter: ListAdapter? = null
    private val data = ArrayList<FoodEntity>()
    private val data1 = ArrayList<User>()
    lateinit var ref: DatabaseReference
    lateinit var ref1: DatabaseReference
    private var oname: String = ""
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_food, container, false)

        val fab = view.findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            addFood()
        }

        mRecyclerView = view.findViewById<View>(R.id.recyclerView) as RecyclerView
        mTextViewEmpty = view.findViewById<View>(R.id.textViewEmpty) as TextView
        mImageViewEmpty = view.findViewById<View>(R.id.imageViewEmpty) as ImageView
        mProgressBarLoading = view.findViewById<View>(R.id.progressBarLoading) as ProgressBar

        val layoutManager = LinearLayoutManager(activity)
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
                        if(food!!.userid!= FirebaseAuth.getInstance().currentUser!!.uid&& food!!.status != "Requested"){
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
                if(p0!!.exists()){
                    for(h in p0.children){
                        val u = h.getValue(User::class.java)
                        data1.add(u!!)
                    }
                }
            }

        })
        return view
    }

    private fun addFood(){
        activity?.let{
            val intent = Intent (it, AddFood::class.java)
            it.startActivity(intent)
        }
    }
    inner class ListAdapter(private val dataList: ArrayList<FoodEntity>) :
        RecyclerView.Adapter<ListAdapter.ViewHolder>() {

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            internal var image: ImageView
            internal var textViewName: TextView
            internal var textViewOwner: TextView
            internal var textViewDate: TextView

            init {
                this.image =  itemView.findViewById<View>(R.id.imageView)as ImageView
                this.textViewName = itemView.findViewById<View>(R.id.ownername) as TextView
                this.textViewOwner = itemView.findViewById<View>(R.id.itemname) as TextView
                this.textViewDate = itemView.findViewById<View>(R.id.status) as TextView
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListAdapter.ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.food_item, parent, false)

            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ListAdapter.ViewHolder, position: Int) {
            for(i in 0..data1.size){
                if(data1[i].id.equals(dataList[position].userid)){
                    oname = data1[i].username
                    break
                }
            }
            Picasso.with(activity).load(dataList[position].uri).into(holder.image)
            holder.textViewName.text = dataList[position].name
            holder.textViewOwner.text = oname
            holder.textViewDate.text = dataList[position].createDate

            holder.itemView.setOnClickListener {v->
                val i = Intent(v.context, FoodDetail::class.java)
                i.putExtra("img", data[position].uri)
                i.putExtra("owner", oname)
                i.putExtra("item", data[position].name)
                i.putExtra("description", data[position].desc)
                i.putExtra("pickuptime", data[position].date)
                i.putExtra("location", data[position].locate)
                i.putExtra("status",data[position].status)
                i.putExtra("foodid",data[position].foodId)
                i.putExtra("createDate",data[position].createDate)
                i.putExtra("id",data[position].userid)
                v.context.startActivity(i)
            }
        }

        override fun getItemCount(): Int {
            return dataList.size
        }
    }


}
