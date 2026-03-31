package com.medicare.app.repository

import androidx.lifecycle.LiveData
import com.medicare.app.data.db.FoodScheduleDao
import com.medicare.app.data.model.FoodSchedule

class FoodRepository(private val foodScheduleDao: FoodScheduleDao) {

    val allFoodSchedules: LiveData<List<FoodSchedule>> = foodScheduleDao.getAllFoodSchedules()

    suspend fun insert(foodSchedule: FoodSchedule): Long {
        return foodScheduleDao.insertFoodSchedule(foodSchedule)
    }

    suspend fun update(foodSchedule: FoodSchedule) {
        foodScheduleDao.updateFoodSchedule(foodSchedule)
    }

    suspend fun delete(foodSchedule: FoodSchedule) {
        foodScheduleDao.deleteFoodSchedule(foodSchedule)
    }

    suspend fun deleteById(id: Int) {
        foodScheduleDao.deleteFoodScheduleById(id)
    }

    suspend fun getEnabledFoodSchedules(): List<FoodSchedule> {
        return foodScheduleDao.getEnabledFoodSchedules()
    }
}