package com.sachin.runningtracker.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Build
import android.os.Looper
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.model.LatLng
import com.sachin.runningtracker.R
import com.sachin.runningtracker.other.Constant.ACTION_PAUSE_SERVICE
import com.sachin.runningtracker.other.Constant.ACTION_SHOW_TRACKING_FRAGMENT
import com.sachin.runningtracker.other.Constant.ACTION_START_OR_RESUME_SERVICE
import com.sachin.runningtracker.other.Constant.ACTION_STOP_SERVICE
import com.sachin.runningtracker.other.Constant.FASTEST_LOCATION_INTERVAL
import com.sachin.runningtracker.other.Constant.LOCATION_UPDATE_INTERVAL
import com.sachin.runningtracker.other.Constant.NOTIFICATION_CHANNEL_ID
import com.sachin.runningtracker.other.Constant.NOTIFICATION_CHANNEL_NAME
import com.sachin.runningtracker.other.Constant.NOTIFICATION_ID
import com.sachin.runningtracker.other.TrackingUtility
import com.sachin.runningtracker.ui.MainActivity
import timber.log.Timber

typealias polyLine = MutableList<LatLng>
typealias polyLines = MutableList<polyLine>

class TrackerService : LifecycleService() {

    var isFirstRun = true
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient



    companion object {
        val isTracking = MutableLiveData<Boolean>()
        val pathPoints = MutableLiveData<polyLines>( )
    }

    override fun onCreate() {
        super.onCreate()
        postInitialTrackValues()
        fusedLocationProviderClient = FusedLocationProviderClient(this)

        isTracking.observe(this, Observer {
            updateLocation(it)
        })
    }

    private fun postInitialTrackValues(){
        isTracking.postValue(false)
        pathPoints.postValue(mutableListOf())
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        intent?.let {
            when(intent.action){
                ACTION_START_OR_RESUME_SERVICE ->{
                    if(isFirstRun){
                        startForegroundService()
                        isFirstRun = false
                    } else {
                        startForegroundService()
                        Timber.d("Resuming Service...")
                    }
                }

                ACTION_STOP_SERVICE  ->{
                    Timber.d("Service stopped")
                }

                ACTION_PAUSE_SERVICE ->{
                    Timber.d("Service paused")
                    pauseService()
                }
            }
        }

        return super.onStartCommand(intent, flags, startId)

    }


     private val locationCallback = object : LocationCallback(){
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)

            if(isTracking.value!!){
                locationResult?.locations?.let {locations ->
                    for (location in locations){
                        addPathPoint(location)
                        Timber.d("COORDINATES: ${location.longitude}, ${location.latitude}" )
                    }
                }
            }

        }
    }

    /////add last coordinate to polyline of polyline list
    private fun addPathPoint(location: Location?){
        location?.let {

            ///get the values of coords
            val position = LatLng(location.latitude, location.longitude)
            pathPoints.value?.apply {
                ////adding the values to last polylines list
                last().add(position)
                pathPoints.postValue(this)
            }
        }
    }

    /////initial track values
    private fun addEmptyPolyLine() = pathPoints.value?.apply {
        add(mutableListOf())
        pathPoints.postValue(this)
    } ?: pathPoints.postValue(mutableListOf(mutableListOf()))

    private fun updateLocation(isTracking: Boolean){
        if (isTracking){
            if(TrackingUtility.hasLocationPermission(this)){
                val request = LocationRequest().apply {
                        interval = LOCATION_UPDATE_INTERVAL
                        fastestInterval = FASTEST_LOCATION_INTERVAL
                        priority = PRIORITY_HIGH_ACCURACY
                }

                fusedLocationProviderClient.requestLocationUpdates(
                    request,
                    locationCallback,
                    Looper.getMainLooper()
                )
            }
        } else {
            fusedLocationProviderClient.removeLocationUpdates(locationCallback)
        }
    }

    private fun pauseService(){
        isTracking.postValue(false)
    }

    private fun startForegroundService(){

        addEmptyPolyLine()

        isTracking.postValue(true)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE)
        as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            createNotificationChannel(notificationManager)
        }

        val notificationBuilder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setAutoCancel(false)
            .setOngoing(true)////notification cant be swiped
            .setSmallIcon(R.drawable.ic_run)
            .setContentTitle("Running Tracker")
            .setContentText("00:00:00")
            .setContentIntent(getMainActivityPendingIntent())


        startForeground(NOTIFICATION_ID, notificationBuilder.build())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager: NotificationManager){

        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            NOTIFICATION_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_LOW
            )
            notificationManager.createNotificationChannel(channel)
    }

    private fun getMainActivityPendingIntent() = PendingIntent.getActivity(
        this,
        0,
        Intent(this, MainActivity::class.java).also {
            it.action = ACTION_SHOW_TRACKING_FRAGMENT
        },
        PendingIntent.FLAG_UPDATE_CURRENT
    )

}