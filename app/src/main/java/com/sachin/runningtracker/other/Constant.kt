package com.sachin.runningtracker.other

import android.graphics.Color

object Constant {

    const val RUNNING_DATABASE_NAME: String = "running_db"

    const val REQUEST_LOCATION_CODE = 0

    const val ACTION_START_OR_RESUME_SERVICE = "action_start_or_resume_service"
    const val ACTION_STOP_SERVICE = "action_stop_service"
    const val ACTION_PAUSE_SERVICE = "action_pause_service"
    const val ACTION_SHOW_TRACKING_FRAGMENT = "action_show_tracking_fragment"

    const val NOTIFICATION_CHANNEL_ID = "running_tracker_channel"
    const val NOTIFICATION_CHANNEL_NAME = "running_tracker"
    const val NOTIFICATION_ID = 1

    const val POLYLINE_COLOR = Color.RED
    const val POLYLINE_WIDTH = 8f

    const val MAP_ZOOM = 15f

    const val LOCATION_UPDATE_INTERVAL = 5000L
    const val FASTEST_LOCATION_INTERVAL = 2000L

}