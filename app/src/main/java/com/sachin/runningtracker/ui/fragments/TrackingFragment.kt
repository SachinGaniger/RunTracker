package com.sachin.runningtracker.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import com.sachin.runningtracker.R
import com.sachin.runningtracker.databinding.FragmentTrackingBinding
import com.sachin.runningtracker.other.Constant.ACTION_PAUSE_SERVICE
import com.sachin.runningtracker.other.Constant.ACTION_START_OR_RESUME_SERVICE
import com.sachin.runningtracker.other.Constant.MAP_ZOOM
import com.sachin.runningtracker.other.Constant.POLYLINE_COLOR
import com.sachin.runningtracker.other.Constant.POLYLINE_WIDTH
import com.sachin.runningtracker.services.TrackerService
import com.sachin.runningtracker.services.polyLine
import com.sachin.runningtracker.ui.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TrackingFragment : Fragment(R.layout.fragment_tracking) {


    private val viewModel: MainViewModel by viewModels()
    private lateinit var trackingFragmentBinding: FragmentTrackingBinding
    private var map: GoogleMap? = null

    private var isTracking = false
    private var pathPoints = mutableListOf<polyLine>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        trackingFragmentBinding = FragmentTrackingBinding.inflate(inflater, container, false)
        return trackingFragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        trackingFragmentBinding.mapView.onCreate(savedInstanceState)

        trackingFragmentBinding.mapView.getMapAsync {
            map = it
            addAllPolyLines()
        }

        subscribeToObserve()

        trackingFragmentBinding.btnToggleRun.setOnClickListener {
            toggleRun()
        }

    }

    private fun addLatestPolyLine() {

        if (pathPoints.isNotEmpty() && pathPoints.last().size > 1) {
            val secondLastLatLong: LatLng = pathPoints.last()[pathPoints.last().size - 2]
            val lastLatLong = pathPoints.last().last()

            /////polyline decorations
            val polylineOptions = PolylineOptions()
                .color(POLYLINE_COLOR)
                .width(POLYLINE_WIDTH)
                .add(secondLastLatLong)
                .add(lastLatLong)

            map?.addPolyline(polylineOptions)
        }
    }

    private fun addAllPolyLines(){
        for (polyline in pathPoints){
            val polyLineOptions = PolylineOptions()
                .color(POLYLINE_COLOR)
                .width(POLYLINE_WIDTH)
                .addAll(polyline)

            map?.addPolyline(polyLineOptions)
        }
    }



    private fun toggleRun(){
        if(isTracking){
            sendCommandToService(ACTION_PAUSE_SERVICE)
        } else{
            sendCommandToService(ACTION_START_OR_RESUME_SERVICE)
        }
    }

    private fun updateTracking(isTracking: Boolean){
        this.isTracking = isTracking
        if(!isTracking){
            trackingFragmentBinding.btnToggleRun.text = "Start"
            trackingFragmentBinding.btnFinishRun.visibility = View.VISIBLE
        } else {
            trackingFragmentBinding.btnToggleRun.text = "Stop"
            trackingFragmentBinding.btnFinishRun.visibility = View.GONE
        }
    }

    private fun subscribeToObserve(){
        TrackerService.isTracking.observe(viewLifecycleOwner, Observer {
            updateTracking(it)
        })

        TrackerService.pathPoints.observe(viewLifecycleOwner, Observer {
            pathPoints = it
            addLatestPolyLine()
            moveCameraToUser()
        })
    }



    private fun sendCommandToService(action: String) {

        Intent(requireContext(), TrackerService::class.java).also {
            it.action = action
            requireContext().startService(it)
        }

    }

    private fun moveCameraToUser(){
        if(pathPoints.isNotEmpty() && pathPoints.last().isNotEmpty()){
            map?.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                    pathPoints.last().last(), MAP_ZOOM
                )
            )
        }
    }

    override fun onResume() {
        super.onResume()
        trackingFragmentBinding.mapView?.onResume()
    }

    override fun onStart() {
        super.onStart()
        trackingFragmentBinding.mapView?.onStart()
    }

    override fun onStop() {
        super.onStop()
        trackingFragmentBinding.mapView?.onStop()
    }

    override fun onPause() {
        super.onPause()
        trackingFragmentBinding.mapView?.onPause()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        trackingFragmentBinding.mapView?.onLowMemory()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        trackingFragmentBinding.mapView?.onSaveInstanceState(outState)
    }
}