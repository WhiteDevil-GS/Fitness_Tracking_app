package com.atilmohamine.fitnesstracker.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.atilmohamine.fitnesstracker.model.DailyFitnessModel
import com.atilmohamine.fitnesstracker.model.WeeklyFitnessModel
import com.atilmohamine.fitnesstracker.repository.FitnessRepository
import com.atilmohamine.fitnesstracker.repository.FitnessRepositoryImpl
import com.atilmohamine.fitnesstracker.repository.SharedPreferencesRepository
import com.atilmohamine.fitnesstracker.repository.SharedPreferencesRepositoryImpl

class FitnessViewModel : ViewModel() {

    private val fitnessRepo: FitnessRepository = FitnessRepositoryImpl()
    private val sharedPreferencesRepo: SharedPreferencesRepository = SharedPreferencesRepositoryImpl()

    /**
     * Fetches the daily fitness data and returns it as LiveData
     */
    fun getDailyFitnessData(context: Context): LiveData<DailyFitnessModel> {
        return fitnessRepo.getDailyFitnessData(context)
    }

    /**
     * Fetches the weekly fitness data and returns it as LiveData
     */
    fun getWeeklyFitnessData(context: Context): LiveData<WeeklyFitnessModel> {
        return fitnessRepo.getWeeklyFitnessData(context)
    }

    /**
     * Saves the objective steps to shared preferences
     */
    fun saveObjectiveSteps(context: Context, objectiveSteps: Int) {
        sharedPreferencesRepo.saveObjectiveSteps(context, objectiveSteps)
    }

    /**
     * Loads the saved objective steps from shared preferences
     */
    fun loadObjectiveSteps(context: Context): Int {
        return sharedPreferencesRepo.loadObjectiveSteps(context)
    }

}
