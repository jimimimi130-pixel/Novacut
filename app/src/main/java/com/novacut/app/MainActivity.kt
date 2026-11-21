package com.novacut.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    // شاشة NOVA CUT الرئيسية
    // 1. معاينة الفيديو فوق
    // 2. Timeline جوة
    // 3. شريط التأثيرات 100+ تأثير
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // هنا راح نربط محرك التأثيرات NovaEngine
    }
}
