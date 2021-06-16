package com.sachin.runningtracker.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.sachin.runningtracker.R
import com.sachin.runningtracker.databinding.ActivityMainBinding
import com.sachin.runningtracker.other.Constant
import com.sachin.runningtracker.other.Constant.ACTION_SHOW_TRACKING_FRAGMENT
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        ////if the app was destroyed
        navigateToTrackingFragmentIfNeeded(intent)

        binding.bottomNavigationView.setupWithNavController(navHostFragment.findNavController())

        navHostFragment.findNavController().
                addOnDestinationChangedListener { _, destination, _ ->
                    when(destination.id){
                        R.id.settingsFragment, R.id.runFragment, R.id.statsFragment ->
                            binding.bottomNavigationView.visibility = View.VISIBLE
                        else ->
                            binding.bottomNavigationView.visibility = View.GONE
                     }
                }

    }

    //// if the app was just minimised and not destroyed
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if (intent != null) {
            navigateToTrackingFragmentIfNeeded(intent)
        }
    }

    private fun navigateToTrackingFragmentIfNeeded(intent: Intent){
        if (intent?.action == ACTION_SHOW_TRACKING_FRAGMENT){
            navHostFragment.findNavController().navigate(R.id.action_global_to_trackerFragment)
        }
    }
}
