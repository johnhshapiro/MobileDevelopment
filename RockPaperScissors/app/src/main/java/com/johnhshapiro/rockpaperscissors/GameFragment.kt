package com.johnhshapiro.rockpaperscissors

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_game.*
import java.io.File
import kotlin.random.Random

class GameFragment : Fragment(), SensorEventListener {

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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_game, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val filename = "rps_extreme_data"
        val file = File(activity?.filesDir, filename)
        val username = file.readText()

        var bestStreakCount = "0"
        var userHighScore = "0"
        val db = Firebase.firestore
        val highestScoreRef = db.collection("highestScore").document("goat")
        highestScoreRef.addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w("exists", "Listen failed.", e)
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {
                Log.d("has data", "Current data: ${snapshot.data}")
                bestStreakCount = snapshot.getString("score")!!
            } else {
                Log.d("whoops", "Current data: null")
            }
        }
        val currentUserRef = db.collection("highScores").document(username)
        currentUserRef.addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w("exists", "Listen failed.", e)
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {
                Log.d("has data", "Current data: ${snapshot.data}")
                try {
                    userHighScore = snapshot.getString("score")!!
                } catch(e: Error) {
                    Log.e("Snapshot", e.toString())
                }
                best_streak_count.text = userHighScore
            } else {
                Log.d("whoops", "Current data: null")
            }
        }

        val cheerString = getString(R.string.cheer_string)
        val choices = arrayOf(
            getString(R.string.rock_string),
            getString(R.string.paper_string),
            getString(R.string.scissor_string))
        val winner = getString(R.string.win)
        val loser = getString(R.string.lose)
        val drawer = getString(R.string.draw)
        var streakCount = 0

        this.sensorManager = requireActivity().getSystemService(Context.SENSOR_SERVICE) as SensorManager
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
                            val data = hashMapOf(
                                "username" to username,
                                "score" to streakCount.toString()
                            )
                            if (streakCount > bestStreakCount.toInt()) {
                                db.collection("highestScore").document("goat").update("score", streakCount.toString())
                                db.collection("highScores").document(username).set(data)
                            }
                            if (streakCount > userHighScore.toInt()) {
                                db.collection("highScores").document(username).set(data)
                            }
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

}
