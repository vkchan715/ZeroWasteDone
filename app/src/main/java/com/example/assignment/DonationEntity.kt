package com.example.assignment

class DonationEntity(val id:String,val title: String,val uri:String, val detail:String,val date:String, val targetPrice: String, val donatedPrice: String){
    constructor():this("","","","","","",""){

    }
}