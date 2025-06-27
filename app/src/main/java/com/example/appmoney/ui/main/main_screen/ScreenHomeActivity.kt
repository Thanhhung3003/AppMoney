package com.example.appmoney.ui.main.main_screen

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.appmoney.R
import com.example.appmoney.databinding.ActivityScreenHomeBinding
import com.example.appmoney.ui.common.notification.ReminderWorker
import java.util.Calendar
import java.util.concurrent.TimeUnit

class ScreenHomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityScreenHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScreenHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        requestNotificationPermission()
        setDailyReminderWorker()
        navigateFragment(AppScreen.Input)
        binding.navHome.setOnItemSelectedListener { item ->
            handleFragment(item.itemId)
            true
        }
    }

    private fun requestNotificationPermission() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 1)
            }
        }
    }

    private fun setDailyReminderWorker() {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 8)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
        }

        if (calendar.timeInMillis < System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }

        val delay = calendar.timeInMillis - System.currentTimeMillis()
        val dailyReminderWorkRequest = PeriodicWorkRequestBuilder<ReminderWorker>(24,TimeUnit.HOURS)
            .setInitialDelay( delay, TimeUnit.MILLISECONDS)
            .build()

        WorkManager.getInstance(applicationContext).enqueueUniquePeriodicWork(
            "daily_reminder",
            ExistingPeriodicWorkPolicy.UPDATE,
            dailyReminderWorkRequest)
    }

    @Deprecated("This method has been deprecated in favor of using the\n      {@link OnBackPressedDispatcher} via {@link #getOnBackPressedDispatcher()}.\n      The OnBackPressedDispatcher controls how back button events are dispatched\n      to one or more {@link OnBackPressedCallback} objects.")
    override fun onBackPressed() {
        val canBack = this.supportFragmentManager.fragments.size > 1
        when {
            canBack -> super.onBackPressed()
            else -> {
                // cancel app
            }
        }
    }

    private fun handleFragment(itemId: Int) {
        val screen = when(itemId){
            R.id.item_input -> AppScreen.Input
            R.id.item_history_trans -> AppScreen.HistoryTrans
            R.id.item_report -> AppScreen.Report
            R.id.item_more -> AppScreen.More
            else -> return
        }
        navigateFragment(screen, isPopBackStack = true)
    }
}