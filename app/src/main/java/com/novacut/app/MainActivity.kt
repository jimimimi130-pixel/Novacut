package com.novacut.app

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import android.widget.VideoView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var videoView: VideoView
    private lateinit var pickText: TextView

    private val pickVideo = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        if (uri != null) {
            pickText.visibility = View.GONE
            videoView.visibility = View.VISIBLE
            videoView.setVideoURI(uri)
            videoView.start()
            Toast.makeText(this, "تم اختيار الفيديو ✅", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        videoView = findViewById(R.id.videoView)
        pickText = findViewById(R.id.pickVideoText)

        pickText.setOnClickListener {
            pickVideo.launch("video/*")
        }
        videoView.setOnClickListener {
            pickVideo.launch("video/*")
        }
    }
}
