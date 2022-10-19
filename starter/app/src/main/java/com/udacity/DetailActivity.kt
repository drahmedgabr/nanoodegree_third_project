package com.udacity

import android.app.NotificationManager
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_detail.*

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)

        val notificationManager = ContextCompat.getSystemService(
            this,
            NotificationManager::class.java
        ) as NotificationManager

        notificationManager.cancelNotifications()

        val status = intent.getBooleanExtra("status", false)
        val fileName = intent.getStringExtra("file_name")


        if (status) {
            status_value.text = "Success"
        } else {
            status_value.text = "Fail"
            status_value.setTextColor(Color.RED)
        }

        file_name_value.text = fileName

        back_button.setOnClickListener {
            finish()
        }

    }

}
