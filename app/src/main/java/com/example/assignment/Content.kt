package com.example.assignment

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_content.*

class Content : AppCompatActivity() {
    lateinit var donationFragment: Donation
    lateinit var foodFragment:Food
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_content)
//        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
//        val navController = this.findNavController(R.id.layout)
//        drawerLayout = binding.drawerLayout
//        NavigationUI.setupActionBarWithNavController(this,navController)
//        NavigationUI.setupWithNavController(binding.navView, navController)
//        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout)
//        toolbar = supportActionBar!!
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
            }
            true
        }

        val navView:NavigationView = findViewById(R.id.navView)
        navView.setNavigationItemSelectedListener { menuItem ->
            when(menuItem.itemId){
                R.id.logout ->{
                    FirebaseAuth.getInstance().signOut()
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }
    }
    //    override fun onSupportNavigateUp(): Boolean {
//        val navController = this.findNavController(R.id.layout)
//        return NavigationUI.navigateUp(navController, drawerLayout)
//    }

}
