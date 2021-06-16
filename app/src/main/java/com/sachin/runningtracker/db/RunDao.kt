package com.sachin.runningtracker.db

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.Dao

@Dao
interface RunDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRun(run: Run)

    @Delete
    suspend fun deleteRun(run: Run)

    @Query("SELECT * FROM running_table ORDER BY timestamp DESC")
    fun getAllRunsSortedByDate(): LiveData<List<Run>>

    @Query("SELECT * FROM running_table ORDER BY timeInMilli DESC")
    fun getAllRunsSortedByTimeInMilli(): LiveData<List<Run>>

    @Query("SELECT * FROM running_table ORDER BY caloriesBurnt DESC")
    fun getAllRunsSortedByCaloriesBurnt(): LiveData<List<Run>>

    @Query("SELECT * FROM running_table ORDER BY avgSpeed DESC")
    fun getAllRunsSortedByAvgSpeed(): LiveData<List<Run>>

    @Query("SELECT * FROM running_table ORDER BY distance DESC")
    fun getAllRunsSortedByDistance(): LiveData<List<Run>>

    @Query("SELECT SUM(timeInMilli) FROM running_table")
    fun getTotalTimeInMilli(): LiveData<Long>

    @Query("SELECT SUM(caloriesBurnt) FROM running_table")
    fun getTotalCaloriesBurnt(): LiveData<Long>

    @Query("SELECT SUM(distance) FROM running_table")
    fun getTotalDistance(): LiveData<Int>

    @Query("SELECT AVG(timeInMilli) FROM running_table")
    fun getTotalAvgSpeed(): LiveData<Float>


}