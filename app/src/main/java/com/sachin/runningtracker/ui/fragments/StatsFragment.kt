package com.sachin.runningtracker.ui.fragments

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.sachin.runningtracker.R
import com.sachin.runningtracker.ui.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class StatsFragment : Fragment(R.layout.fragment_stats) {


    private val viewModel : MainViewModel by viewModels()

}