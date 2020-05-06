package com.johnhshapiro.rockpaperscissors

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_leader_board.*
import java.io.File

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)


        val filename = "rps_extreme_data"
        val file = File(this.filesDir, filename)
        if (file.exists()) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment, GameFragment())
                .commit()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_game -> {
                supportFragmentManager.beginTransaction().replace(R.id.fragment, GameFragment()).commit()
                return true
            }
            R.id.action_help -> {
                supportFragmentManager.beginTransaction().replace(R.id.fragment, HowToPlayFragment()).commit()
                return true
            }
            R.id.action_leader ->
            {
                supportFragmentManager.beginTransaction().replace(R.id.fragment, LeaderBoard()).commit()
                return true
            }
            else -> true
        }
    }
}
