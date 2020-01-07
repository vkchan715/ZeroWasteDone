package com.example.assignment


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.assignment.databinding.FragmentFoodBinding


/**
 * A simple [Fragment] subclass.
 */
class Food : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        val binding: FragmentFoodBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_food, container, false)
        val fab = binding.fab
        fab.setOnClickListener {
            addFood()
        }
        return binding.root
    }

    private fun addFood(){
        activity?.let{
            val intent = Intent (it, AddFood::class.java)
            it.startActivity(intent)
        }
    }

}
