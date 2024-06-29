package com.sample.stopwatchapp

import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.sample.stopwatchapp.databinding.ActivityStopwatchScreenBinding

class StopwatchScreen : AppCompatActivity() {
    private val binding: ActivityStopwatchScreenBinding by lazy {
        ActivityStopwatchScreenBinding.inflate(layoutInflater)
    }
    var isRunning = false
    var pauseAt: Long = 0
    var lapseList = ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        val autoStopTime = "00:05"
        var arrayAdapter =
            ArrayAdapter(this, android.R.layout.simple_list_item_activated_1, lapseList)
        binding.lapView.adapter = arrayAdapter
        binding.lapButton.setOnClickListener {
            lapseList.add("# LAP ${arrayAdapter.count + 1}: ${binding.chronometer.text}")
            arrayAdapter.notifyDataSetChanged()
            binding.lapView.setSelection(arrayAdapter.count - 1)
            binding.lapView.smoothScrollToPosition(arrayAdapter.count - 1)
        }

        binding.shareButton.setOnClickListener {
            val text = lapseList.toString()
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, text)
            startActivity(Intent.createChooser(intent, "Share"))
    }
//        binding.autoStopButton.setOnClickListener {
//            var dialog = Dialog(this)
//            dialog.setContentView(R.layout.stopwatch_dialog)
//            var numberPicker = dialog.findViewById<NumberPicker>(R.id.number_picker)
//            numberPicker.minValue = 1
//            numberPicker.maxValue = 300
//            dialog.findViewById<Button>(R.id.done).setOnClickListener {
//                Toast.makeText(
//                    this,
//                    "Time will auto stop in ${numberPicker.value} mins",
//                    Toast.LENGTH_LONG
//                ).show()
//                dialog.dismiss()
//            }
//            dialog.show()
//        }

        binding.backButton.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        binding.playButton.setOnClickListener {
            if (isRunning) {
                binding.playButton.setBackgroundResource(R.drawable.timer_play)
                isRunning = false
                binding.chronometer.isEnabled = false
                pauseAt = SystemClock.elapsedRealtime() - binding.chronometer.base
                binding.chronometer.stop()
                if(autoStopTime == binding.chronometer.text.toString()) {
                    Toast.makeText(this, "Time is up!", Toast.LENGTH_LONG).show()
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }

            } else {
                binding.playButton.setBackgroundResource(R.drawable.baseline_pause_24)
                isRunning = true
                binding.chronometer.isEnabled = true
                binding.chronometer.base = SystemClock.elapsedRealtime() - pauseAt
                binding.chronometer.start()
                if(autoStopTime == "${binding.chronometer.text}") {
                    Toast.makeText(this, "Time is up!", Toast.LENGTH_LONG).show()
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
            }
        }

        binding.resetButton.setOnClickListener {
            binding.chronometer.base = SystemClock.elapsedRealtime()
            pauseAt = 0
            binding.chronometer.stop()
            isRunning = false
            arrayAdapter.clear()
            binding.playButton.setBackgroundResource(R.drawable.timer_play)
        }
    }
}