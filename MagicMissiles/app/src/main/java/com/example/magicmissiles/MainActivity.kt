package com.example.magicmissiles

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.fragment_buttons.*
import kotlinx.android.synthetic.main.fragment_damage_output.*
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var numMissiles = 0

        less.setOnClickListener {
            if (numMissiles > 0) {
                numMissiles--
                missileCount.text = numMissiles.toString()
            }
        }
        more.setOnClickListener {
            numMissiles++
            missileCount.text = numMissiles.toString()
        }

        fire.setOnClickListener {
            var totalDamage = 0
            for (i in 1..numMissiles) {
                totalDamage += Random.nextInt(1,5)
            }
            damageAmnt.text = totalDamage.toString()
        }
    }
}
