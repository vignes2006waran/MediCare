package com.medicare.app.repository

import androidx.lifecycle.LiveData
import com.medicare.app.data.db.MedicineDao
import com.medicare.app.data.model.Medicine

class MedicineRepository(private val medicineDao: MedicineDao) {

    val allMedicines: LiveData<List<Medicine>> = medicineDao.getAllMedicines()

    suspend fun insert(medicine: Medicine): Long {
        return medicineDao.insertMedicine(medicine)
    }

    suspend fun update(medicine: Medicine) {
        medicineDao.updateMedicine(medicine)
    }

    suspend fun delete(medicine: Medicine) {
        medicineDao.deleteMedicine(medicine)
    }

    suspend fun deleteById(id: Int) {
        medicineDao.deleteMedicineById(id)
    }

    suspend fun getEnabledMedicines(): List<Medicine> {
        return medicineDao.getEnabledMedicines()
    }

    suspend fun getMedicineById(id: Int): Medicine? {
        return medicineDao.getMedicineById(id)
    }
}