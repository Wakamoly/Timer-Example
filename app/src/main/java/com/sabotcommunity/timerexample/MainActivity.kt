package com.sabotcommunity.timerexample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.text.format.DateUtils
import android.view.View
import android.widget.*
import kotlin.math.roundToLong

class MainActivity : AppCompatActivity() {
    private var timerProgress: ProgressBar? = null
    private var timeRemainingText: TextView? = null
    private var timeRemainingMillis: TextView? = null
    private var timerEdittext: EditText? = null
    private var timerSpinner: Spinner? = null
    private var startButton: Button? = null
    private var timer: CountDownTimer? = null
    private var timesUp: TextView? = null

    private var timeChoosen: Int = 0
    private var timeMultiplier: Double = 0.0
    private var timerLengthSeconds: Long = 0
    private var secondsRemaining: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        timerProgress = findViewById(R.id.timer_progress)
        timeRemainingText = findViewById(R.id.time_remaining_text)
        timeRemainingMillis = findViewById(R.id.time_remaining_millis)
        timerEdittext = findViewById(R.id.timer_edittext)
        timerSpinner = findViewById(R.id.timer_spinner)
        startButton = findViewById(R.id.start_button)
        timesUp = findViewById(R.id.time_up)

        initOptions()

        startButton?.setOnClickListener{
            if (timerEdittext?.text!!.isNotBlank() && timeMultiplier != 0.0){
                timeChoosen = timerEdittext?.text.toString().toInt()
                if (timer != null){
                    timer?.cancel()
                }
                startTimer()
            }else{
                Toast.makeText(this, "Please select a number/option!", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun initOptions(){
        val timerOptions: MutableList<String?> = ArrayList()
        timerOptions.add(0, "Select an option!")
        timerOptions.add("Milliseconds")
        timerOptions.add("Seconds")
        timerOptions.add("Minutes")
        timerOptions.add("Days")
        timerOptions.add("Hours")
        timerOptions.add("Weeks")
        timerOptions.add("Months")
        timerOptions.add("Years")
        timerOptions.add("Centuries?")
        val arrayAdapter: ArrayAdapter<String?> = ArrayAdapter<String?>(this, android.R.layout.simple_list_item_1, timerOptions)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        timerSpinner?.adapter = arrayAdapter
        timerSpinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                if (parent.getItemAtPosition(position) != "Select an option!")  {
                    val option = parent.getItemAtPosition(position).toString()
                    timeMultiplier = when (option){
                        "Milliseconds" -> { 0.001 }
                        "Seconds" -> { 1.0 }
                        "Minutes" -> { 60.0 }
                        "Hours" -> { 3600.0 }
                        "Days" -> { 86400.0 }
                        "Weeks" -> { 604800.0 }
                        "Months" -> { 2629746.0 }
                        "Years" -> { 31556952.0 }
                        "Centuries?" -> { 3155695200.0 }
                        "Select an option!" -> { 0.0 }
                        else -> { 0.0 }
                    }
                    Toast.makeText(parent.context, "Selected: $option", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun startTimer(){
        timesUp?.visibility = View.INVISIBLE
        timerLengthSeconds = (timeChoosen * timeMultiplier).roundToLong()
        secondsRemaining = timerLengthSeconds
        timerProgress?.max = timerLengthSeconds.toInt()
        timer = object : CountDownTimer(timerLengthSeconds * 1000, 1000) {
            override fun onFinish() = onTimeFinished()
            override fun onTick(millisUntilFinished: Long) {
                secondsRemaining = millisUntilFinished / 1000
                updateTimerUI()
            }
        }.start()
        timerProgress?.visibility = View.VISIBLE
    }

    private fun onTimeFinished(){
        timerProgress?.progress = 0
        timer?.cancel()
        timesUp?.visibility = View.VISIBLE
    }

    private fun updateTimerUI(){
        val displayText = formatDuration(secondsRemaining)
        timeRemainingMillis?.text = displayText
        timerProgress?.progress = (timerLengthSeconds - secondsRemaining).toInt()
    }

    private fun formatDuration(seconds: Long): String = if (seconds < 60) {
        "$seconds seconds"
    } else {
        DateUtils.formatElapsedTime(seconds)
    }

}