package com.atilmohamine.fitnesstracker.model

data class HourlyFitnessModel(
    val hour: Int,
    val caloriesBurned: Int,
    val stepCount: Int,
    val distance: Float
)
