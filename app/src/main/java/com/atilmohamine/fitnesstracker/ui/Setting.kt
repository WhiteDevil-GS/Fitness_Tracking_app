package com.atilmohamine.fitnesstracker.ui
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.NumberPicker
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.atilmohamine.fitnesstracker.R
import com.atilmohamine.fitnesstracker.viewmodel.FitnessViewModel

class Setting : Fragment() {

    private lateinit var stepsTextView: TextView
    private lateinit var changeObjButton: Button
    private val fitnessViewModel: FitnessViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_setting, container, false)

        stepsTextView = rootView.findViewById(R.id.steps)
        changeObjButton = rootView.findViewById(R.id.changeObj)

        showObjectiveSteps(rootView.context)
        changeObjButton.setOnClickListener {
            showObjectiveDialog(rootView.context)
        }

        return rootView
    }

    private fun showObjectiveSteps(context: Context) {
        // Load and display the objective steps from the ViewModel
        stepsTextView.text = fitnessViewModel.loadObjectiveSteps(context).toString()
    }

    private fun showObjectiveDialog(context: Context) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_objective, null)
        val dialogBuilder = AlertDialog.Builder(context)
            .setView(dialogView)
            .setTitle("Select Objective Steps")

        // Set up the NumberPicker for selecting the step goal
        val objectiveSeekBar = dialogView.findViewById<NumberPicker>(R.id.stepsPicker)

        // Set range from 10 to 10,000 steps
        objectiveSeekBar.minValue = 10  // minimum value 10 steps
        objectiveSeekBar.maxValue = 100 // maximum value 10,000 steps
        objectiveSeekBar.value = fitnessViewModel.loadObjectiveSteps(context) // Use the current step value loaded from storage

        // Formatter to display the value as 10, 20, 30 steps, etc.
        objectiveSeekBar.setFormatter(object : NumberPicker.Formatter {
            override fun format(value: Int): String {
                return "$value steps" // Display the value in steps (no multiplication needed)
            }
        })

        // Save the new objective steps when the user clicks "Save"
        dialogBuilder.setPositiveButton("Save") { _, _ ->
            val newObjectiveSteps = objectiveSeekBar.value // Get the value in steps
            fitnessViewModel.saveObjectiveSteps(context, newObjectiveSteps) // Save the value
            showObjectiveSteps(context) // Update the displayed step objective
            Toast.makeText(context, "Objective steps saved", Toast.LENGTH_SHORT).show()
        }

        // Show the dialog
        dialogBuilder.create().show()
    }
}
