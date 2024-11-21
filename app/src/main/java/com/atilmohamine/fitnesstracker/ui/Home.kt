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
            // Update UI with daily fitness data
            val caloriesBurned = dailyFitness.caloriesBurned
            val distance = dailyFitness.distance

            textViewCalories.text = caloriesBurned.toString()

            // Optionally convert distance to kilometers or miles
            val formattedDistance = String.format("%.2f", distance) // Change this based on user preference
            textViewDistance.text = formattedDistance

            // Update the progress bar with the current step count
            stepsProgressBar.progress = stepCount

            // Check if the user has reached their step goal
            if (stepCount >= goalSteps) {
                // Display a message when the step goal is reached
                Toast.makeText(requireContext(), "Congratulations! You've reached your step goal!", Toast.LENGTH_SHORT).show()
            }
        })

        return rootView
    }

    // SensorEventListener methods to track steps
    override fun onSensorChanged(event: SensorEvent?) {
        if (event == null) return
        if (event.sensor.type == Sensor.TYPE_STEP_COUNTER) {
            // Get the step count from the sensor event
            stepCount = event.values[0].toInt()

            // Save the updated step count in SharedPreferences
            sharedPreferences.edit().putInt("stepCount", stepCount).apply()

            // Update the UI with the new step count
            textViewSteps.text = stepCount.toString()
            textViewStepsBig.text = stepCount.toString()
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Optionally handle sensor accuracy changes
    }

    override fun onResume() {
        super.onResume()
        // Register the sensor listener when the fragment is in the foreground
        stepSensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
        }
    }

    override fun onPause() {
        super.onPause()
        // Unregister the sensor listener when the fragment is not in the foreground
        sensorManager.unregisterListener(this)
    }
}
