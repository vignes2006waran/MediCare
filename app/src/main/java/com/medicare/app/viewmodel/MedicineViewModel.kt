package com.medicare.app.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.medicare.app.data.db.AppDatabase
import com.medicare.app.data.model.Medicine
import com.medicare.app.repository.MedicineRepository
import kotlinx.coroutines.launch

class MedicineViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: MedicineRepository
    val allMedicines: LiveData<List<Medicine>>

    init {
        val medicineDao = AppDatabase.getDatabase(application).medicineDao()
        repository = MedicineRepository(medicineDao)
        allMedicines = repository.allMedicines
    }

    fun insert(medicine: Medicine, onResult: (Long) -> Unit) = viewModelScope.launch {
        val id = repository.insert(medicine)
        onResult(id)
    }

    fun update(medicine: Medicine) = viewModelScope.launch {
        repository.update(medicine)
    }

    fun delete(medicine: Medicine) = viewModelScope.launch {
        repository.delete(medicine)
    }

    fun deleteById(id: Int) = viewModelScope.launch {
        repository.deleteById(id)
    }

    suspend fun getEnabledMedicines(): List<Medicine> {
        return repository.getEnabledMedicines()
    }
}