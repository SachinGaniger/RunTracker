package com.sachin.runningtracker.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.sachin.runningtracker.repositories.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class StatsViewModel @Inject constructor(
    mainRepository: MainRepository
) : ViewModel() {
    
}