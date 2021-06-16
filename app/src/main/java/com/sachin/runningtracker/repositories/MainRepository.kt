package com.sachin.runningtracker.repositories

import com.sachin.runningtracker.db.Run
import com.sachin.runningtracker.db.RunDao
import javax.inject.Inject


class MainRepository @Inject constructor(
    private val runDao: RunDao
) {

    suspend fun insertRun(run: Run) = runDao.insertRun(run)

    suspend fun deleteRun(run: Run) = runDao.deleteRun(run)

    fun getAllRunSortedByDate() = runDao.getAllRunsSortedByDate()

    fun getAllRunsSortedByTimeInMilli() = runDao.getAllRunsSortedByTimeInMilli()

    fun getAllRunsSortedByCaloriesBurnt() = runDao.getAllRunsSortedByCaloriesBurnt()

    fun getAllRunsSortedByAvgSpeed() = runDao.getAllRunsSortedByAvgSpeed()

    fun getAllRunsSortedByDistance() = runDao.getAllRunsSortedByDistance()

    fun getTotalTimeInMilli() = runDao.getTotalTimeInMilli()

    fun getTotalCaloriesBurnt() = runDao.getTotalCaloriesBurnt()

    fun getTotalDistance() = runDao.getTotalDistance()

    fun getTotalAvgSpeed() = runDao.getTotalAvgSpeed()

}