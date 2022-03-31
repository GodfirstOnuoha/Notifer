package com.godfirst.notifer

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import com.godfirst.notifer.databinding.ActivityMainBinding
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    companion object {
        private const val CHANNEL_ID = "notifier_channel"
        private const val CHANNEL_NAME = "notifier"
        private const val NOTIFICATION_ID = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        createChannel(CHANNEL_ID, CHANNEL_NAME)

        val calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
        }
        val message = "Today is ${getMainDay(calendar.get(Calendar.DAY_OF_WEEK))}, " +
                "the ${getMainDate(calendar.get(Calendar.DAY_OF_MONTH))} day in the month of " +
                "${getMainMonth(calendar.get(Calendar.MONTH))} which is the ${getMainDate(1 + calendar.get(Calendar.MONTH))} month. \n" +
                "It remains ${365 - calendar.get(Calendar.DAY_OF_YEAR)} day(s) or ${52 - calendar.get(Calendar.WEEK_OF_YEAR)} week(s) for the year to finish. \n" +
                "#KodeCamp2.0"

        binding.apply {
            notifyButton.setOnClickListener {
                notifyImage.setImageResource(R.drawable.ic_notifications_active)
                notifyLayout.setBackgroundColor(Color.CYAN)
                val notificationManager =
                    getSystemService(NotificationManager::class.java) as NotificationManager
                notificationManager.sendNotification(message, this@MainActivity)

                Handler(Looper.getMainLooper()).postDelayed({
                    binding.notifyImage.setImageResource(R.drawable.ic_notifications)
                    binding.notifyLayout.setBackgroundColor(Color.WHITE)
                }, 5000)
            }
        }
    }

    private fun getMainDay(day: Int): String {
        var mainDay = ""
        when (day) {
            1 -> mainDay = "Sunday"
            2 -> mainDay = "Monday"
            3 -> mainDay = "Tuesday"
            4 -> mainDay = "Wednesday"
            5 -> mainDay = "Thursday"
            6 -> mainDay = "Friday"
            7 -> mainDay = "Saturday"
        }
        return mainDay
    }

    private fun getMainDate(day: Int): String {
        var mainDate = ""
        mainDate = when (day) {
            1, 21, 31 -> "${day}st"
            2, 22 -> "${day}nd"
            3, 23 -> "${day}rd"
            else -> "${day}th"
        }
        return mainDate
    }

    private fun getMainMonth(day: Int): String {
        var mainMonth = ""
        when (day) {
            0 -> mainMonth = "January"
            1 -> mainMonth = "February"
            2 -> mainMonth = "March"
            3 -> mainMonth = "April"
            4 -> mainMonth = "May"
            5 -> mainMonth = "June"
            6 -> mainMonth = "July"
            7 -> mainMonth = "August"
            8 -> mainMonth = "September"
            9 -> mainMonth = "October"
            10 -> mainMonth = "November"
            11 -> mainMonth = "December"
        }
        return mainMonth
    }

    private fun createChannel(channelId: String, channelName: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                setShowBadge(false)
            }

            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.GREEN
            notificationChannel.enableVibration(true)
            notificationChannel.description = "Notifer means of sending notification"

            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    fun NotificationManager.sendNotification(message: String, context: Context) {
        val image =
            BitmapFactory.decodeResource(context.resources, R.drawable.notifer)
        val bigPictureStyle = NotificationCompat.BigPictureStyle()
            .bigPicture(image)
            .bigLargeIcon(image)
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.notifer_small)
            .setContentTitle("Fact about the YEAR!")
            .setAutoCancel(true)
            .setStyle(bigPictureStyle)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setLargeIcon(image)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
        notify(NOTIFICATION_ID, builder.build())
    }
}