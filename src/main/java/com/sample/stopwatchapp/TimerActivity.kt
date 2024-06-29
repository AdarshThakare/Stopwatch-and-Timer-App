package com.sample.stopwatchapp

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.os.SystemClock
import android.widget.Button
import android.widget.Chronometer
import android.widget.NumberPicker
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.sample.stopwatchapp.databinding.ActivityTimerBinding

class TimerActivity : AppCompatActivity() {
    private val binding: ActivityTimerBinding by lazy {
        ActivityTimerBinding.inflate(layoutInflater)
    }
    var timeTextView: String = "0:00"
    var isRunning = false
    private lateinit var countDownTimer: CountDownTimer
    private var timeLeftInMillis: Long = 0 // 1 minute in milliseconds
    private var totalSecs = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        binding.backButton.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        binding.setTimeButton.setOnClickListener {
            var dialog = Dialog(this)
            dialog.setContentView(R.layout.stopwatch_dialog)
            var minsPicker = dialog.findViewById<NumberPicker>(R.id.mins_picker)
            minsPicker.minValue = 0
            minsPicker.maxValue = 60
            var secsPicker = dialog.findViewById<NumberPicker>(R.id.secs_picker)
            secsPicker.minValue = 0
            secsPicker.maxValue = 59

            dialog.findViewById<Button>(R.id.done).setOnClickListener {
                if(minsPicker.value == 60){
                    secsPicker.maxValue = 0
                }
                if (secsPicker.value < 10) {
                    timeTextView = "${minsPicker.value}:0${secsPicker.value}"
                } else {
                    timeTextView = "${minsPicker.value}:${secsPicker.value}"
                }
                totalSecs = secsPicker.value + minsPicker.value * 60
                Toast.makeText(
                    this,
                    "Time set is " + timeTextView,
                    Toast.LENGTH_SHORT
                ).show()
                timeLeftInMillis= (minsPicker.value * 60000 + secsPicker.value * 1000).toLong()
                dialog.dismiss()
                binding.chronometer.text = timeTextView
            }
            dialog.show()
        }

        binding.playButton.setOnClickListener {
            if (isRunning) {
                binding.playButton.setBackgroundResource(R.drawable.timer_play)
                isRunning = false
                binding.chronometer.isEnabled = false
                countDownTimer.cancel()

            } else {
                binding.playButton.setBackgroundResource(R.drawable.baseline_pause_24)
                isRunning = true
                binding.chronometer.isEnabled = true
                startTimer()

            }
        }

        binding.resetButton.setOnClickListener {
            binding.playButton.setBackgroundResource(R.drawable.timer_play)
            isRunning = false
            binding.chronometer.isEnabled = false
            countDownTimer.cancel()
            binding.chronometer.text = timeTextView // 1 minute in milliseconds
            timeLeftInMillis = totalSecs*1000L
        }
    }
    // Define the startTimer method
    private fun startTimer() {
        countDownTimer = object : CountDownTimer(timeLeftInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeftInMillis = millisUntilFinished
                updateCountDownText()
            }

            override fun onFinish() {
                isRunning = false
                binding.playButton.setBackgroundResource(R.drawable.timer_play)
                timeLeftInMillis = 0
                updateCountDownText()
            }
        }.start()
    }

    // Update the TextView with the remaining time
    private fun updateCountDownText() {
        val minutes = (timeLeftInMillis / 1000) / 60
        val seconds = (timeLeftInMillis / 1000) % 60
        val timeFormatted = String.format("%02d:%02d", minutes, seconds)
        binding.chronometer.text = timeFormatted
    }
}
