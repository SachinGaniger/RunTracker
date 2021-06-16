package com.sachin.runningtracker.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.sachin.runningtracker.R
import com.sachin.runningtracker.databinding.FragmentSetupBinding
import com.sachin.runningtracker.ui.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

class SetUpFragment : Fragment(R.layout.fragment_setup) {

    private lateinit var setUpFragmentSetupBinding: FragmentSetupBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        setUpFragmentSetupBinding = FragmentSetupBinding.inflate(inflater, container, false)

        return setUpFragmentSetupBinding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpFragmentSetupBinding.tvContinue.setOnClickListener {
            findNavController().navigate(R.id.action_setUpFragment_to_runFragment)
        }

    }
}