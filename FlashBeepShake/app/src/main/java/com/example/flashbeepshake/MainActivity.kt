package com.example.flashbeepshake

import android.content.Context
import android.hardware.camera2.CameraManager
import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
        val cameraId = cameraManager.cameraIdList[0]
        val tone = ToneGenerator(AudioManager.STREAM_MUSIC, 100)
        val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        flashlightSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked)
                cameraManager.setTorchMode(cameraId, true)
            else
                cameraManager.setTorchMode(cameraId, false)
        }

        beepButton.setOnClickListener {
            tone.startTone(ToneGenerator.TONE_DTMF_3, 600)
        }

        vibrateButton.setOnClickListener {
            val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            Log.d("AAAAAAAH", vibrator.hasVibrator().toString())

            if (vibrator.hasVibrator()) { // Vibrator availability checking
                vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE))
            }
        }

    }
}
