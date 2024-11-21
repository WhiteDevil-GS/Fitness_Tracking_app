package com.atilmohamine.fitnesstracker.ui

import android.Manifest
import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.atilmohamine.fitnesstracker.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.fitness.Fitness
import com.google.android.gms.fitness.FitnessOptions
import com.google.android.gms.fitness.data.DataType
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private val GOOGLE_FIT_PERMISSIONS_REQUEST_CODE = 1
    private lateinit var bottomNavigationView: BottomNavigationView

    private val runningQOrLater =
        android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q

    private val fitnessOptions = FitnessOptions.builder()
        .addDataType(DataType.TYPE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
        .addDataType(DataType.AGGREGATE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
        .addDataType(DataType.TYPE_CALORIES_EXPENDED, FitnessOptions.ACCESS_READ)
        .addDataType(DataType.AGGREGATE_CALORIES_EXPENDED, FitnessOptions.ACCESS_READ)
        .addDataType(DataType.TYPE_DISTANCE_DELTA, FitnessOptions.ACCESS_READ)
        .addDataType(DataType.AGGREGATE_DISTANCE_DELTA, FitnessOptions.ACCESS_READ)
        .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkPermissionsAndRun(GOOGLE_FIT_PERMISSIONS_REQUEST_CODE)
    }

    private fun checkPermissionsAndRun(fitActionRequestCode: Int) {
        if (permissionApproved()) {
            fitSignIn(fitActionRequestCode)
        } else {
            // Permissions are not mandatory, so we skip requesting if not granted
            Log.i(TAG, "Google Fit permissions not granted, continuing without it.")
            loadNavController() // Proceed without Google Fit permissions
        }
    }

    private fun loadNavController() {
        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        val navController = findNavController(R.id.nav_controller)
        bottomNavigationView.setupWithNavController(navController) // Setup the NavController directly
    }

    private fun fitSignIn(requestCode: Int) {
        if (!GoogleSignIn.hasPermissions(getGoogleAccount(), fitnessOptions)) {
            // Request permissions from the user
            GoogleSignIn.requestPermissions(
                this,
                requestCode,
                getGoogleAccount(),
                fitnessOptions
            )
        } else {
            loadNavController() // Proceed to the next step if permissions are granted
        }
    }

    private fun getGoogleAccount() = GoogleSignIn.getAccountForExtension(this, fitnessOptions)

    private fun permissionApproved(): Boolean {
        return if (runningQOrLater) {
            PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        } else {
            true
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode) {
            Activity.RESULT_OK -> when (requestCode) {
                GOOGLE_FIT_PERMISSIONS_REQUEST_CODE -> loadNavController()
            }
            else -> {
                Toast.makeText(this, "Permission not granted", Toast.LENGTH_LONG).show()
                loadNavController()
            }
        }
    }
}
