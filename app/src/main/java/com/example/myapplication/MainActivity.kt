package com.example.myapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.fragments.HomeFragment
import com.example.myapplication.fragments.LearnFragment
import com.example.myapplication.fragments.LettersFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {


    private lateinit var bottomNavigationView: BottomNavigationView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        // ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
        //   val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
        //  v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
        //  insets


        bottomNavigationView = findViewById(R.id.bottom_navigation)

        bottomNavigationView.setOnItemReselectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.bottom_home -> {
                    replaceFragment(HomeFragment())
                    true

                }R.id.bottom_letter -> {
                replaceFragment(LettersFragment())
                true

            }R.id.bottom_learn -> {
                replaceFragment(LearnFragment())
                true

            }
            else -> false

            }
        }
replaceFragment(HomeFragment())
    }
    private fun replaceFragment(fragment: androidx.fragment.app.Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit()
    } }
