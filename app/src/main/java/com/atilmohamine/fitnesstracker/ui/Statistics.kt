package com.atilmohamine.fitnesstracker.ui

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.atilmohamine.fitnesstracker.R
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.*

class Statistics : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_statistics, container, false)

        // BarChart for hourly progress
        val barChart = rootView.findViewById<BarChart>(R.id.progress_chart)
        barChart?.let { setupBarChart(it) }

        // PieChart for daily statistics
        val pieChart = rootView.findViewById<PieChart>(R.id.pieChart)
        pieChart?.let { setupPieChart(it) }

        // LineChart for progress over time
        val lineChart = rootView.findViewById<LineChart>(R.id.lineChart)
        lineChart?.let { setupLineChart(it) }

        return rootView
    }

    private fun setupBarChart(barChart: BarChart) {
        val entries = ArrayList<BarEntry>()

        // Example hourly data (replace with actual user activity data)
        val hourlyData = listOf(100, 200, 150, 300, 400, 250, 600)
        hourlyData.forEachIndexed { index, value ->
            entries.add(BarEntry(index.toFloat(), value.toFloat()))
        }

        val barDataSet = BarDataSet(entries, "Weekly Steps").apply {
            color = Color.BLUE
            valueTextColor = Color.BLACK
            valueTextSize = 12f
        }

        val barData = BarData(barDataSet)
        barChart.data = barData

        barChart.description = Description().apply {
            text = ""
            textColor = Color.WHITE
            textSize = 12f
        }

        barChart.animateY(1000)
    }

    private fun setupPieChart(pieChart: PieChart) {
        val entries = ArrayList<PieEntry>()

        // Example daily data: Replace these values with actual user data.
        val dailyMetrics = mapOf(
            "Calories Burnt" to 450f,
            "Distance (meters)" to 9144f,
            "Steps Count" to 12000f
        )

        // Add data to PieChart entries
        dailyMetrics.forEach { (metric, value) ->
            entries.add(PieEntry(value, metric))
        }

        // Configure PieDataSet
        val pieDataSet = PieDataSet(entries, "Daily Metrics").apply {
            colors = listOf(Color.MAGENTA, Color.BLUE, Color.DKGRAY)
            valueTextColor = Color.WHITE
            valueTextSize = 12f
        }

        // Configure PieData
        val pieData = PieData(pieDataSet)
        pieChart.data = pieData

        // Set PieChart description
        pieChart.description = Description().apply {
            text = "Daily Fitness Metrics"
            textColor = Color.GRAY
            textSize = 12f
        }

        // Disable the hole in the chart and animate
        pieChart.isDrawHoleEnabled = false
        pieChart.animateY(1000)
    }

    private fun setupLineChart(lineChart: LineChart) {
        val entries = ArrayList<Entry>()

        // Example progress over time data (replace with actual user data)
        val weeklyProgress = listOf(3.5f, 4.2f, 5.1f, 5.8f, 6.3f, 7.0f, 7.5f) // Example data
        weeklyProgress.forEachIndexed { index, value ->
            entries.add(Entry(index.toFloat(), value))
        }

        val lineDataSet = LineDataSet(entries, "Weekly Progress").apply {
            color = Color.GREEN
            valueTextColor = Color.BLACK
            valueTextSize = 12f
            lineWidth = 2f
            setCircleColor(Color.RED)
            circleRadius = 4f
        }

        val lineData = LineData(lineDataSet)
        lineChart.data = lineData

        lineChart.description = Description().apply {
            text = "Weekly Progress"
            textColor = Color.WHITE
            textSize = 12f
        }
        lineChart.animateX(1000)
    }
}
