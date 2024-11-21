package com.atilmohamine.fitnesstracker.ui

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.atilmohamine.fitnesstracker.R
import com.bumptech.glide.Glide // Or use Picasso

class VideoAdapter(private val videoList: List<Video>) :
    RecyclerView.Adapter<VideoAdapter.VideoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_video_suggestion, parent, false)
        return VideoViewHolder(view)
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        val video = videoList[position]
        holder.titleTextView.text = video.title

        // Load the image using Glide (or Picasso)
        Glide.with(holder.itemView.context)
            .load(video.imageUrl) // Use the URL for the thumbnail image
            .into(holder.thumbnailImageView)

        holder.itemView.setOnClickListener {
            // Open video URL in a browser
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(video.url))
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount() = videoList.size

    class VideoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titleTextView: TextView = view.findViewById(R.id.tv_video_title)
        val thumbnailImageView: ImageView = view.findViewById(R.id.iv_video_thumbnail)
    }
}
