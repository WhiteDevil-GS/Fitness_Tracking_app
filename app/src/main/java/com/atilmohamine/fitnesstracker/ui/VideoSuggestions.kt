package com.atilmohamine.fitnesstracker.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.atilmohamine.fitnesstracker.R

class VideoSuggestions : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: VideoAdapter
    private val videoList = listOf(
        Video("How to Stay Fit", "https://www.youtube.com/watch?v=JzAShhATDl8", "https://img.youtube.com/vi/JzAShhATDl8/0.jpg"),
        Video("Best Home Workouts", "https://www.youtube.com/watch?v=KeNObkhENKQ", "https://img.youtube.com/vi/KeNObkhENKQ/0.jpg"),
        Video("Diet Tips for Fitness", "https://youtu.be/i639p4GmnIo?si=n6lNg9Ke-OMi4Cj4", "https://img.youtube.com/vi/i639p4GmnIo/0.jpg"),
        Video("Diet Plan And Weight Gain", "https://youtube.com/shorts/NtJKTlV7S6U?si=HGfQDkJcFz75OskY", "https://img.youtube.com/vi/NtJKTlV7S6U/0.jpg"),
        Video("Full Body Workout", "https://youtu.be/UIPvIYsjfpo?si=gtJ5iTWlwE-QQZNG", "https://img.youtube.com/vi/UIPvIYsjfpo/default.jpg"),
        Video("Morning Stretch Routine", "https://youtu.be/t2jel6q1GRk?si=iPbT3EJa4t0f5YY8", "https://img.youtube.com/vi/t2jel6q1GRk/default.jpg"),
        Video("Yoga for Flexibility", "https://youtu.be/CgzVARJ19gE?si=-ALvtDZUSSBRZWTH", "https://img.youtube.com/vi/CgzVARJ19gE/default.jpg"),
        Video("Advanced Cardio Exercises", "https://youtu.be/yplP5cLuyf4?si=j1a0MkhToSAuenWe", "https://img.youtube.com/vi/yplP5cLuyf4/default.jpg"),
        Video("30 Day Gym Transformation", "https://youtu.be/8i4G1zYBO6A?si=3dlvYeBDDwE3GrDn", "https://img.youtube.com/vi/8i4G1zYBO6A/default.jpg"),
        Video("HIIT Workout for Beginners", "https://youtu.be/jWCm9piAwAU?si=B8nQWbiuUMgTKmoM", "https://img.youtube.com/vi/jWCm9piAwAU/default.jpg"),
        Video("5-Minute Abs Workout", "https://youtu.be/kWodISSgIqI?si=ZqWpyKNSdMCByH5e", "https://img.youtube.com/vi/kWodISSgIqI/default.jpg"),
        Video("Lose Belly Fat Fast", "https://youtu.be/WOzDq9uisrI?si=xtrcwLfcD_wg1Jcf", "https://img.youtube.com/vi/WOzDq9uisrI/default.jpg")
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the fragment's layout
        val rootView = inflater.inflate(R.layout.fragment_video_suggestions, container, false)

        // Initialize RecyclerView
        recyclerView = rootView.findViewById(R.id.rv_video_suggestions)
        recyclerView.layoutManager = LinearLayoutManager(context)

        // Initialize the adapter with the video list
        adapter = VideoAdapter(videoList)

        // Set the adapter to the RecyclerView
        recyclerView.adapter = adapter

        return rootView
    }
}
