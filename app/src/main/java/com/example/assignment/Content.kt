package com.example.assignment

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_content.*

class Content : AppCompatActivity() {
    lateinit var donationFragment: Donation
    lateinit var foodFragment:Food
    lateinit var volunteer: Volunteer
    lateinit var ref: DatabaseReference
    var name = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_content)
        ref = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().currentUser!!.uid)
        ref.addValueEventListener(object: ValueEventListener {
            override fun onCancelled(databaseError: DatabaseError) {
            }
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                name = dataSnapshot.child("username").value.toString()
                val nv = findViewById<NavigationView>(R.id.navView)
                val menu:Menu = nv.menu
                val menuitem: MenuItem =menu.findItem(R.id.account)
                menuitem.title = name
                menuitem.isEnabled = false

            }

        })
        val bottomNavigation: BottomNavigationView = findViewById(R.id.navigationView)

        donationFragment = Donation()
        supportFragmentManager.beginTransaction().replace(R.id.framelayout,donationFragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit()

        bottomNavigation.setOnNavigationItemSelectedListener { item->
            when(item.itemId){
                R.id.navigation_donation ->{
                    donationFragment = Donation()
                    supportFragmentManager.beginTransaction().replace(R.id.framelayout,donationFragment)
                        .commit()
                }
                R.id.navigation_food ->{
                    foodFragment = Food()
                    supportFragmentManager.beginTransaction().replace(R.id.framelayout,foodFragment)
                        .commit()
                }
                R.id.navigation_team->{
                     volunteer= Volunteer()
                    supportFragmentManager.beginTransaction().replace(R.id.framelayout,volunteer)
                        .commit()
                }
            }
            true
        }

        val navView:NavigationView = findViewById(R.id.navView)
        navView.setNavigationItemSelectedListener { menuItem ->
            when(menuItem.itemId) {
                R.id.logout -> {
                    FirebaseAuth.getInstance().signOut()
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
                R.id.myrequests -> {
                startActivity(Intent(this, MyRequests::class.java))
                        finish ()
                }
                R.id.mylisting->{
                    startActivity(Intent(this, MyListing::class.java))
                    finish ()
                }
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }
    }

}
