package com.medicare.app.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.medicare.app.data.model.FoodSchedule

@Dao
interface FoodScheduleDao {

    @Query("SELECT * FROM food_schedules ORDER BY time ASC")
    fun getAllFoodSchedules(): LiveData<List<FoodSchedule>>

    @Query("SELECT * FROM food_schedules WHERE isEnabled = 1 ORDER BY time ASC")
    suspend fun getEnabledFoodSchedules(): List<FoodSchedule>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFoodSchedule(foodSchedule: FoodSchedule): Long

    @Update
    suspend fun updateFoodSchedule(foodSchedule: FoodSchedule)

    @Delete
    suspend fun deleteFoodSchedule(foodSchedule: FoodSchedule)

    @Query("DELETE FROM food_schedules WHERE id = :id")
    suspend fun deleteFoodScheduleById(id: Int)
}