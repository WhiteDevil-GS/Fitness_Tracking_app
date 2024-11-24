package com.atilmohamine.fitnesstracker.ui

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.atilmohamine.fitnesstracker.R
import com.atilmohamine.fitnesstracker.viewmodel.FitnessViewModel
import java.util.*

class Home : Fragment(), SensorEventListener {

    private lateinit var textViewSteps: TextView
    private lateinit var textViewStepsBig: TextView
    private lateinit var textViewCalories: TextView
    private lateinit var textViewDistance: TextView
    private lateinit var stepsProgressBar: ProgressBar

    private val fitnessViewModel: FitnessViewModel by viewModels()

    private lateinit var sensorManager: SensorManager
    private var stepSensor: Sensor? = null
    private var stepCount = 0
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = requireContext().getSharedPreferences("FitnessData", 0)

        // Check if it's a new day to reset the step count
        val currentDay = Calendar.getInstance().get(Calendar.DAY_OF_YEAR)
        val savedDay = sharedPreferences.getInt("savedDay", -1)
        if (currentDay != savedDay) {
            // Reset step count for new day
            stepCount = 0
            sharedPreferences.edit().putInt("stepCount", stepCount).apply()
            sharedPreferences.edit().putInt("savedDay", currentDay).apply()
        } else {
            stepCount = sharedPreferences.getInt("stepCount", 0) // Load saved step count if available
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_home, container, false)

        textViewSteps = rootView.findViewById(R.id.steps)
        textViewStepsBig = rootView.findViewById(R.id.steps_big)
        textViewCalories = rootView.findViewById(R.id.burned_calories)
        textViewDistance = rootView.findViewById(R.id.distance)
        stepsProgressBar = rootView.findViewById(R.id.stepsProgressBar)

        // Fetch the user's step goal (maximum steps)
        val goalSteps = fitnessViewModel.loadObjectiveSteps(requireContext())
        stepsProgressBar.max = goalSteps

        // Initialize the SensorManager and step counter sensor
        sensorManager = requireActivity().getSystemService(SensorManager::class.java)
        stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

        // Start listening to the step sensor
        stepSensor?.also { sensor ->
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_UI)
        }

        // Observe changes to the daily fitness data
        fitnessViewModel.getDailyFitnessData(requireContext()).observe(viewLifecycleOwner, Observer { dailyFitness ->
            val goalSteps = fitnessViewModel.loadObjectiveSteps(requireContext())
            // Update the progress bar with the current step count
            stepsProgressBar.progress = stepCount

            // Check if the user has reached their step goal
            if (stepCount >= goalSteps) {
                Toast.makeText(requireContext(), "Congratulations! You've reached your step goal!", Toast.LENGTH_SHORT).show()
            }
        })

        return rootView
    }

    // SensorEventListener methods to track steps
    override fun onSensorChanged(event: SensorEvent?) {
        if (event == null) return
        if (event.sensor.type == Sensor.TYPE_STEP_COUNTER) {
            stepCount = event.values[0].toInt()

            // Save the updated step count in SharedPreferences
            sharedPreferences.edit().putInt("stepCount", stepCount).apply()

            // Update UI
            updateStepData(stepCount)
        }
    }

    private fun updateStepData(steps: Int) {
        textViewSteps.text = steps.toString()
        textViewStepsBig.text = steps.toString()

        // Calculate calories burned (assuming ~0.04 calories per step)
        val caloriesBurned = steps * 0.04
        textViewCalories.text = String.format("%.2f", caloriesBurned)

        // Calculate distance (assuming average step length ~0.78 meters)
        val distance = steps * 0.78 / 1000 // Convert to kilometers
        textViewDistance.text = String.format("%.2f km", distance)

        // Update the progress bar
        stepsProgressBar.progress = steps
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Optionally handle sensor accuracy changes
    }

    override fun onResume() {
        super.onResume()
        stepSensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }
}
