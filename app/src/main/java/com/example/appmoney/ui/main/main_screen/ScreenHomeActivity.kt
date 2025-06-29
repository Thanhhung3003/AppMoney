package com.example.appmoney.ui.main.main_screen

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.appmoney.R
import com.example.appmoney.databinding.ActivityScreenHomeBinding

class ScreenHomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityScreenHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScreenHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navigateFragment(AppScreen.Input)
        binding.navHome.setOnItemSelectedListener { item ->
            handleFragment(item.itemId)
            true
        }
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
            R.id.item_different -> AppScreen.Different
            else -> return
        }
        navigateFragment(screen, isPopBackStack = true)
    }
}