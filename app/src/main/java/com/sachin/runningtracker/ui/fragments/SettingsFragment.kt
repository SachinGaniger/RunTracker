package com.sachin.runningtracker.ui.fragments

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.sachin.runningtracker.R
import com.sachin.runningtracker.ui.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SettingsFragment : Fragment(R.layout.fragment_settings) {


    private val viewModel : MainViewModel by viewModels()

}