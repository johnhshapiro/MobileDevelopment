package com.example.colorpicker

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File


class MainActivity : AppCompatActivity() {

    override fun finish() {
        val color = arrayListOf(redBar.progress, greenBar.progress, blueBar.progress)
        val returnIntent = Intent()
        returnIntent.putIntegerArrayListExtra("color", color)
        setResult(Activity.RESULT_OK, returnIntent)
        super.finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val filename = "savedColors"
        val file = File(this.filesDir, filename)

        var color = "000000"
        colorBox.setBackgroundColor(Color.BLACK)
        val textDescription = colorName as EditText

        var colorList = file.readLines()
        refreshColors(colorList)

        val info = intent.extras
        if (info != null) {
            if(info.containsKey("key")) {
                exportColorButton.visibility = View.VISIBLE
                exportColorButton.setOnClickListener {
                    finish()
                }
            }
        }

        redBar.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seek: SeekBar,
                                           progress: Int, fromUser: Boolean) {
                color = twoDigitHexValue(progress) + color.substring(2,6)
                colorBox.setBackgroundColor(Color.parseColor("#${color}"))
                redVal.text = progress.toString()
            }
            override fun onStartTrackingTouch(seek: SeekBar) {
            }
            override fun onStopTrackingTouch(seek: SeekBar) {
            }
        })

        greenBar.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seek: SeekBar,
                                           progress: Int, fromUser: Boolean) {
                color = color.substring(0,2) + twoDigitHexValue(progress) + color.substring(4,6)
                colorBox.setBackgroundColor(Color.parseColor("#${color}"))
                greenVal.text = progress.toString()
            }
            override fun onStartTrackingTouch(seek: SeekBar) {
            }
            override fun onStopTrackingTouch(seek: SeekBar) {
            }
        })

        blueBar.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seek: SeekBar,
                                           progress: Int, fromUser: Boolean) {
                color = color.substring(0,4) + twoDigitHexValue(progress)
                colorBox.setBackgroundColor(Color.parseColor("#${color}"))
                blueVal.text = progress.toString()
            }
            override fun onStartTrackingTouch(seek: SeekBar) {
            }
            override fun onStopTrackingTouch(seek: SeekBar) {
            }
        })

        save.setOnClickListener {

            val userColorName = textDescription.text.toString()

            if (userColorName.isNotEmpty()) {
                var nameTaken = false
                val stringToWrite = userColorName + "," + "${progressBarsToSingleString()}"
                colorList.forEach {
                    val colorData = it.split(",")
                    if (colorData[0].equals(userColorName)) {
                        nameTaken = true
                    }
                    if (!nameTaken) {
                        colorList.toMutableList().add(stringToWrite)
                    }
                }
                file.appendText("\n${stringToWrite.toLowerCase()}")
                colorList = file.readLines()
                refreshColors(colorList)
            }
        }
        pickSavedColor.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent:AdapterView<*>, view: View, position: Int, id: Long){
                // Display the selected item text on text view
                val colorValues = pickSavedColor.selectedItem.toString().split(",")
                redBar.progress = colorValues[1].toInt()
                greenBar.progress = colorValues[2].toInt()
                blueBar.progress = colorValues[3].toInt()
            }

            override fun onNothingSelected(parent: AdapterView<*>){
                // Another interface callback
            }
        }
    }

    fun twoDigitHexValue(colorProgress: Int): String {
        var colorValue = Integer.toHexString(colorProgress)
        if (colorValue.length == 1)
            colorValue = "0${colorValue}"
        return colorValue
    }
    private fun refreshColors(colorList: List<String>) {
        ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            colorList
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            pickSavedColor.adapter = adapter
        }

    }

    private fun progressBarsToSingleString (): String {
        return "${redBar.progress},${greenBar.progress},${blueBar.progress}"
    }
}
