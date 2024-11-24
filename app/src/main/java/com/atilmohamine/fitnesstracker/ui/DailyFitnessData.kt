package com.atilmohamine.fitnesstracker.ui

data class DailyFitnessData(
    val stepCount: Int = 0,
    val caloriesBurned: Float = 0.0f,
    val distanceTraveled: Float = 0.0f, // in kilometers
    val activeMinutes: Int = 0, // Total active minutes for the day
    val date: String = "" // The date for this data in "yyyy-MM-dd" format
) {
    /**
     * Provides a summary of the daily fitness data.
     */
    fun getSummary(): String {
        return """
            Date: $date
            Steps: $stepCount
            Calories Burned: ${"%.2f".format(caloriesBurned)} kcal
            Distance: ${"%.2f".format(distanceTraveled)} km
            Active Minutes: $activeMinutes minutes
        """.trimIndent()
    }

    /**
     * Checks if this day's fitness goal is achieved.
     * @param stepGoal The target number of steps for the day.
     * @return True if the goal is achieved, false otherwise.
     */
    fun isGoalAchieved(stepGoal: Int): Boolean {
        return stepCount >= stepGoal
    }

    /**
     * Calculates the remaining steps needed to reach the daily goal.
     * @param stepGoal The target number of steps for the day.
     * @return Remaining steps, or 0 if the goal is achieved.
     */
    fun stepsRemaining(stepGoal: Int): Int {
        return if (stepCount >= stepGoal) 0 else stepGoal - stepCount
    }
}
