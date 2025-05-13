package com.example.myapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.database.NoteDatabase
import com.example.myapplication.fragments.HomeFragment
import com.example.myapplication.fragments.LearnFragment
import com.example.myapplication.fragments.LettersFragment
import com.example.myapplication.repository.NoteRepository
import com.example.myapplication.viewmodel.NoteViewModel
import com.example.myapplication.viewmodel.NoteViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.lifecycle.ViewModel


class MainActivity : AppCompatActivity() {


    private lateinit var bottomNavigationView: BottomNavigationView
    lateinit var noteViewModel: NoteViewModel







    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        // ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
        //   val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
        //  v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
        //  insets
        setupViewModel()

        bottomNavigationView = findViewById(R.id.bottom_navigation)

        bottomNavigationView.setOnItemReselectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.bottom_home -> {
                    replaceFragment(HomeFragment())
                    true

                }

                R.id.bottom_letter -> {
                    replaceFragment(LettersFragment())
                    true

                }

                R.id.bottom_learn -> {
                    replaceFragment(LearnFragment())
                    true

                }

                else -> false

            }
        }
        replaceFragment(HomeFragment())
    }

    private fun setupViewModel(){
        val noteRepository =  NoteRepository(NoteDatabase(this))
        val viewModelProviderFactory = NoteViewModelFactory(application, noteRepository)
        noteViewModel = ViewModelProvider(this, viewModelProviderFactory)[NoteViewModel::class.java]

    }

    private fun replaceFragment(fragment: androidx.fragment.app.Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit()
    }


}
