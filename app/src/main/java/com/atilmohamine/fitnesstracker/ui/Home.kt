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

        // Check if it's a new day to reset the step count and goal flag
        val currentDay = Calendar.getInstance().get(Calendar.DAY_OF_YEAR)
        val savedDay = sharedPreferences.getInt("savedDay", -1)
        if (currentDay != savedDay) {
            sharedPreferences.edit().apply {
                putInt("baselineStepCount", 0)
                putInt("savedDay", currentDay)
                putBoolean("goalReached", false) // Reset goal flag for new day
            }.apply()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_home, container, false)

        // Initialize UI components
        textViewSteps = rootView.findViewById(R.id.steps)
        textViewStepsBig = rootView.findViewById(R.id.steps_big)
        textViewCalories = rootView.findViewById(R.id.burned_calories)
        textViewDistance = rootView.findViewById(R.id.distance)
        stepsProgressBar = rootView.findViewById(R.id.stepsProgressBar)

        val goalSteps = fitnessViewModel.loadObjectiveSteps(requireContext())
        stepsProgressBar.max = goalSteps

        // Retrieve saved data from SharedPreferences
        val savedSteps = sharedPreferences.getInt("stepCount", 0)
        val goalReached = sharedPreferences.getBoolean("goalReached", false)

        // Update the UI with saved data
        updateStepData(savedSteps)

        // Initialize SensorManager and step sensor
        sensorManager = requireActivity().getSystemService(SensorManager::class.java)
        stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

        // Register the sensor listener
        stepSensor?.also { sensor ->
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_UI)
        }

        fitnessViewModel.getDailyFitnessData(requireContext()).observe(viewLifecycleOwner, Observer { dailyFitness ->
            stepsProgressBar.progress = stepCount
        })

        return rootView
    }


    override fun onSensorChanged(event: SensorEvent?) {
        if (event == null) return
        if (event.sensor.type == Sensor.TYPE_STEP_COUNTER) {
            val cumulativeSteps = event.values[0].toInt()
            val baselineSteps = sharedPreferences.getInt("baselineStepCount", 0)

            if (baselineSteps == 0) {
                sharedPreferences.edit().putInt("baselineStepCount", cumulativeSteps).apply()
            }

            stepCount = cumulativeSteps - baselineSteps
            sharedPreferences.edit().putInt("stepCount", stepCount).apply()

            updateStepData(stepCount)
        }
    }

    private fun updateStepData(steps: Int) {
        textViewSteps.text = steps.toString()
        textViewStepsBig.text = steps.toString()

        val caloriesBurned = steps * 0.04
        textViewCalories.text = String.format("%.2f", caloriesBurned)

        val distance = steps * 0.78 / 1000 // Convert to kilometers
        textViewDistance.text = String.format("%.2f", distance)

        stepsProgressBar.progress = steps

        val goalSteps = fitnessViewModel.loadObjectiveSteps(requireContext())
        val goalReached = sharedPreferences.getBoolean("goalReached", false)

        if (steps >= goalSteps && !goalReached) {
            Toast.makeText(requireContext(), "Congratulations! You've reached your step goal!", Toast.LENGTH_SHORT).show()
            sharedPreferences.edit().putBoolean("goalReached", true).apply() // Mark goal as reached
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

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