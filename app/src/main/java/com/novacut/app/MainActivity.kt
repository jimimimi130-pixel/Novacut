package com.novacut.app

import android.graphics.Color
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import android.widget.VideoView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.widget.EditText

class MainActivity : AppCompatActivity() {

    private lateinit var videoView: VideoView
    private lateinit var pickText: TextView
    private lateinit var overlay: View
    private lateinit var overlayText: TextView
    private var videoUri: Uri? = null
    private var speed = 1f
    private var filterIndex = 0
    private var mediaPlayer: MediaPlayer? = null

    private val pickVideo = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        if (uri != null) {
            videoUri = uri
            pickText.visibility = View.GONE
            videoView.setVideoURI(uri)
            videoView.start()
            Toast.makeText(this, "تم اختيار الفيديو ✅", Toast.LENGTH_SHORT).show()
        }
    }
    private val pickMusic = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        if (uri != null) {
            mediaPlayer?.release()
            mediaPlayer = MediaPlayer.create(this, uri)
            mediaPlayer?.isLooping = true
            mediaPlayer?.start()
            Toast.makeText(this, "تم تشغيل الموسيقى 🎵", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        videoView = findViewById(R.id.videoView)
        pickText = findViewById(R.id.pickVideoText)
        overlay = findViewById(R.id.effectOverlay)
        overlayText = findViewById(R.id.overlayText)

        pickText.setOnClickListener { pickVideo.launch("video/*") }
        videoView.setOnClickListener {
            if (videoUri == null) pickVideo.launch("video/*")
            else if (videoView.isPlaying) videoView.pause() else videoView.start()
        }
        videoView.setOnPreparedListener { mp ->
            mp.isLooping = true
        }

        fun needVideo(): Boolean {
            if (videoUri == null) { Toast.makeText(this, "اختر فيديو اولاً 🎬", Toast.LENGTH_SHORT).show(); return false }
            return true
        }
        fun setEffect(color: String, name: String) {
            if (!needVideo()) return
            overlay.setBackgroundColor(Color.parseColor(color))
            Toast.makeText(this, "تأثير $name ✅", Toast.LENGTH_SHORT).show()
        }

        findViewById<TextView>(R.id.effectNova).setOnClickListener { setEffect("#33FF7A00", "NOVA 🔥") }
        findViewById<TextView>(R.id.effectVHS).setOnClickListener { setEffect("#3300FF00", "VHS 📺") }
        findViewById<TextView>(R.id.effectGlitch).setOnClickListener { setEffect("#33FF00FF", "Glitch ⚡") }
        findViewById<TextView>(R.id.effectNeon).setOnClickListener { setEffect("#33A855F7", "Neon 💜") }
        findViewById<TextView>(R.id.effectRain).setOnClickListener { setEffect("#3300D4FF", "مطر 🌧️") }
        findViewById<TextView>(R.id.effectFire).setOnClickListener { setEffect("#66FF4400", "نار 🔥") }

        findViewById<TextView>(R.id.btnTrim).setOnClickListener {
            if (!needVideo()) return@setOnClickListener
            videoView.seekTo(0); videoView.start()
            Toast.makeText(this, "تم القص: رجعنا للبداية ✂️ - اسحب الفيديو للقص الكامل بالتحديث الجاي", Toast.LENGTH_LONG).show()
        }
        findViewById<TextView>(R.id.btnText).setOnClickListener {
            if (!needVideo()) return@setOnClickListener
            val input = EditText(this); input.hint = "اكتب النص..."
            AlertDialog.Builder(this).setTitle("اضافة نص 🔤").setView(input)
                .setPositiveButton("اضافة") { _, _ ->
                    overlayText.text = input.text.toString(); overlayText.visibility = View.VISIBLE
                }.setNegativeButton("الغاء", null).show()
        }
        findViewById<TextView>(R.id.btnMusic).setOnClickListener { pickMusic.launch("audio/*") }
        findViewById<TextView>(R.id.btnFilter).setOnClickListener {
            if (!needVideo()) return@setOnClickListener
            val colors = listOf("#00000000", "#33FFFFFF", "#33000000", "#33FFD700", "#3300FFFF")
            filterIndex = (filterIndex + 1) % colors.size
            overlay.setBackgroundColor(Color.parseColor(colors[filterIndex]))
            Toast.makeText(this, "تم تغيير الفلتر 🎨", Toast.LENGTH_SHORT).show()
        }
        findViewById<TextView>(R.id.btnSpeed).setOnClickListener {
            if (!needVideo()) return@setOnClickListener
            speed = if (speed == 1f) 2f else if (speed == 2f) 0.5f else 1f
            videoView.setOnPreparedListener { mp -> mp.isLooping = true; try { mp.playbackParams = mp.playbackParams.setSpeed(speed) } catch(e: Exception){} }
            videoView.seekTo(videoView.currentPosition)
            Toast.makeText(this, "السرعة صارت ${speed}x ⏩", Toast.LENGTH_SHORT).show()
        }
        findViewById<TextView>(R.id.btnExport).setOnClickListener {
            if (!needVideo()) return@setOnClickListener
            Toast.makeText(this, "جاري التصدير... راح ينحفظ الفيديو بالتحديث الجاي 📤", Toast.LENGTH_LONG).show()
        }
    }
}
