package com.udacity

import android.app.DownloadManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity() {

    private var downloadID: Long = 0
    private var selectedRadioIndex = -1
    private var radioButtonText = ""

    private lateinit var notificationManager: NotificationManager
    private lateinit var pendingIntent: PendingIntent
    private lateinit var action: NotificationCompat.Action

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        custom_button.isEnabled = false
        Toast.makeText(this, getText(R.string.select_option), Toast.LENGTH_LONG).show()

        custom_button.setOnClickListener {

            // Another Way to tell user to select file

//            if (selectedRadioIndex < 0) {
//                Toast.makeText(this, getText(R.string.select_option), Toast.LENGTH_LONG).show()
//            } else {
//                download()
//            }

            download()
        }

        radioGroup.setOnCheckedChangeListener { radioGroup, i ->
            val selectedButton = findViewById<RadioButton>(i)
            selectedRadioIndex = radioGroup.indexOfChild(selectedButton)
            radioButtonText = selectedButton.text.toString()
            custom_button.isEnabled = true
        }


    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
        }
    }

    private fun download() {
        val request =
            DownloadManager.Request(Uri.parse(URL_LIST[selectedRadioIndex]))
                .setTitle(getString(R.string.app_name))
                .setDescription(getString(R.string.app_description))
                .setRequiresCharging(false)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)

        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        downloadID =
            downloadManager.enqueue(request)// enqueue puts the download request in the queue.
    }

    companion object {
        private val URL_LIST = listOf<String>("https://github.com/bumptech/glide","https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter","https://github.com/square/retrofit")
        private const val URL =
            "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip"
        private const val CHANNEL_ID = "channelId"
    }

}
