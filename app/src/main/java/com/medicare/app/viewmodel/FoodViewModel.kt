package com.medicare.app.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.medicare.app.data.db.AppDatabase
import com.medicare.app.data.model.FoodSchedule
import com.medicare.app.repository.FoodRepository
import kotlinx.coroutines.launch

class FoodViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: FoodRepository
    val allFoodSchedules: LiveData<List<FoodSchedule>>

    init {
        val foodDao = AppDatabase.getDatabase(application).foodScheduleDao()
        repository = FoodRepository(foodDao)
        allFoodSchedules = repository.allFoodSchedules
    }

    fun insert(foodSchedule: FoodSchedule, onResult: (Long) -> Unit) = viewModelScope.launch {
        val id = repository.insert(foodSchedule)
        onResult(id)
    }

    fun update(foodSchedule: FoodSchedule) = viewModelScope.launch {
        repository.update(foodSchedule)
    }

    fun delete(foodSchedule: FoodSchedule) = viewModelScope.launch {
        repository.delete(foodSchedule)
    }

    fun deleteById(id: Int) = viewModelScope.launch {
        repository.deleteById(id)
    }

    suspend fun getEnabledFoodSchedules(): List<FoodSchedule> {
        return repository.getEnabledFoodSchedules()
    }
}