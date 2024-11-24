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
import androidx.core.app.ActivityCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.atilmohamine.fitnesstracker.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.fitness.FitnessOptions
import com.google.android.gms.fitness.data.DataType
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    companion object {
        private const val GOOGLE_FIT_PERMISSIONS_REQUEST_CODE = 1
    }

    private lateinit var bottomNavigationView: BottomNavigationView

    // Check if the device is running Android Q (API 29) or later
    private val runningQOrLater =
        android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q

    // Define required Google Fit data types
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

        // Check permissions and initialize
        checkPermissionsAndInitialize()
    }

    /**
     * Check for necessary permissions and handle initialization.
     */
    private fun checkPermissionsAndInitialize() {
        if (permissionApproved()) {
            requestGoogleFitPermissions()
        } else {
            Log.i(TAG, "Permissions not granted. Loading app without Google Fit integration.")
            initializeNavigation()
        }
    }

    /**
     * Request Google Fit permissions if not already granted.
     */
    private fun requestGoogleFitPermissions() {
        if (!GoogleSignIn.hasPermissions(getGoogleAccount(), fitnessOptions)) {
            GoogleSignIn.requestPermissions(
                this,
                GOOGLE_FIT_PERMISSIONS_REQUEST_CODE,
                getGoogleAccount(),
                fitnessOptions
            )
        } else {
            initializeNavigation()
        }
    }

    /**
     * Initializes the Bottom Navigation and Navigation Controller.
     */
    private fun initializeNavigation() {
        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        val navController = findNavController(R.id.nav_controller)
        bottomNavigationView.setupWithNavController(navController)
    }

    /**
     * Retrieve the Google account for Google Fit access.
     */
    private fun getGoogleAccount() = GoogleSignIn.getAccountForExtension(this, fitnessOptions)

    /**
     * Check if the required location permission is approved (for Android Q or later).
     */
    private fun permissionApproved(): Boolean {
        return if (runningQOrLater) {
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            true // Permissions are not required for pre-Q versions
        }
    }

    /**
     * Handle activity results, such as permission responses.
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            GOOGLE_FIT_PERMISSIONS_REQUEST_CODE -> {
                if (resultCode == Activity.RESULT_OK) {
                    Log.i(TAG, "Google Fit permissions granted.")
                } else {
                    Toast.makeText(
                        this,
                        "Google Fit permissions denied. Limited functionality.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                initializeNavigation()
            }
            else -> super.onActivityResult(requestCode, resultCode, data)
        }
    }

    /**
     * Show a toast message for better user feedback.
     */
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
