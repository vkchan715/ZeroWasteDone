package com.example.assignment


import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import java.util.ArrayList


/**
 * A simple [Fragment] subclass.
 */
class Donation : Fragment() {
    private var mTextViewEmpty: TextView? = null
    private var mProgressBarLoading: ProgressBar? = null
    private var mImageViewEmpty: ImageView? = null
    private var mRecyclerView: RecyclerView? = null
    private var mListadapter: Donation.ListAdapter? = null
    private val data = ArrayList<DonationEntity>()
    lateinit var ref: DatabaseReference

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_donation, container, false)
        mRecyclerView = view.findViewById<View>(R.id.recyclerView) as RecyclerView
        mTextViewEmpty = view.findViewById<View>(R.id.textViewEmpty) as TextView
        mImageViewEmpty = view.findViewById<View>(R.id.imageViewEmpty) as ImageView
        mProgressBarLoading = view.findViewById<View>(R.id.progressBarLoading) as ProgressBar

        val layoutManager = LinearLayoutManager(activity)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        mRecyclerView!!.layoutManager = layoutManager

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
                    mListadapter = ListAdapter(data)
                    mRecyclerView!!.adapter = mListadapter
                }
            }

        })
        return view
    }
    inner class ListAdapter(private val dataList: ArrayList<DonationEntity>) :
        RecyclerView.Adapter<ListAdapter.ViewHolder>() {

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            internal var image: ImageView
            internal var textViewTitle: TextView

            init {
                this.image =  itemView.findViewById<View>(R.id.imageView)as ImageView
                this.textViewTitle = itemView.findViewById<View>(R.id.title) as TextView
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListAdapter.ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.donation_item, parent, false)

            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ListAdapter.ViewHolder, position: Int) {
            holder.textViewTitle.text = dataList[position].title
            Picasso.with(activity).load(dataList[position].uri).into(holder.image)

            holder.itemView.setOnClickListener {v->
                val progress :String
                progress = data[position].donatedPrice + "/" + data[position].targetPrice
                val i = Intent(v.context, DonatinoDetails::class.java)
                i.putExtra("img", data[position].uri)
                i.putExtra("title", data[position].title)
                i.putExtra("date", data[position].date)
                i.putExtra("description", data[position].detail)
                i.putExtra("price",progress)
                i.putExtra("id",data[position].id)
                v.context.startActivity(i)
            }
        }

        override fun getItemCount(): Int {
            return dataList.size
        }
    }


}
