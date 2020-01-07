package com.example.assignment


import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment

import com.example.assignment.databinding.FragmentDonationBinding

/**
 * A simple [Fragment] subclass.
 */
class Donation : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        val binding:FragmentDonationBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_donation, container, false)
        return binding.root
    }


}
