package com.johnhshapiro.rockpaperscissors

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.os.CountDownTimer
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlin.random.Random

class MainActivity : AppCompatActivity(), SensorEventListener {

    lateinit var  sensorManager: SensorManager
    var x: Float = 0F
    var y: Float = 0F

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        print("every thing is alright")
    }

    override fun onSensorChanged(event: SensorEvent?) {
        when(event?.sensor?.type) {
            Sensor.TYPE_ACCELEROMETER -> {
                x = event.values?.get(0)!!
            }
            Sensor.TYPE_GYROSCOPE -> {
                y = event.values?.get(1)!!
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val noString = getString(R.string.no_string)
        val cheerString = getString(R.string.cheer_string)
        val choices = arrayOf(
            getString(R.string.rock_string),
            getString(R.string.paper_string),
            getString(R.string.scissor_string))
        val winner = getString(R.string.win)
        val loser = getString(R.string.lose)
        val drawer = getString(R.string.draw)
        var streakCount = 0
        val manager = supportFragmentManager

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensorManager.registerListener(
            this,
            sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
            SensorManager.SENSOR_DELAY_NORMAL
        )
        sensorManager.registerListener(
            this,
            sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE),
            SensorManager.SENSOR_DELAY_NORMAL
        )

        play_game.setOnClickListener {
            val randomOpponentChoice = Random.nextInt(3)
            var userChoice: Int
            var result: String

            val xInitial: Float = x
            var xMax = 0F
            var xDelta: Float

            val yInitial: Float = y
            var yMax = 0F
            var yDelta: Float

            val timer = object: CountDownTimer(3000,1) {
                override fun onTick(millisUntilFinished: Long) {
                    xDelta = x - xInitial
                    yDelta = y - yInitial
                    if (xDelta > xMax)
                        xMax = x - xInitial
                    if (yDelta > yMax)
                        yMax = y - yInitial
                    countdown.text = ((millisUntilFinished + 1000) / 1000).toString()
                }

                override fun onFinish() {
                    countdown.text = cheerString
                    userChoice =
                        if (xMax > 5 && xMax > yMax)
                            2
                        else if (yMax > 3 && yMax > xMax)
                            1
                        else
                            0

                    when {
                        (randomOpponentChoice == 0 && userChoice == 1) || (randomOpponentChoice == 1 && userChoice == 2) || (randomOpponentChoice == 2 && userChoice == 0) -> {
                            result = winner
                            streakCount++
                        }
                        (randomOpponentChoice == 2 && userChoice == 1) || (randomOpponentChoice == 1 && userChoice == 0) || (randomOpponentChoice == 0 && userChoice == 2) -> {
                            result = loser
                            streakCount = 0
                        }
                        else -> result = drawer
                    }
                    streak_count.text = streakCount.toString()
                    user_move.text = choices[userChoice]
                    opponent_choice.text = choices[randomOpponentChoice]
                    result_display.text = result
                }
            }
            timer.start()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
