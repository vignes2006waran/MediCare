package com.medicare.app.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.medicare.app.data.model.Medicine

@Dao
interface MedicineDao {

    @Query("SELECT * FROM medicines ORDER BY time ASC")
    fun getAllMedicines(): LiveData<List<Medicine>>

    @Query("SELECT * FROM medicines WHERE isEnabled = 1 ORDER BY time ASC")
    suspend fun getEnabledMedicines(): List<Medicine>

    @Query("SELECT * FROM medicines WHERE id = :id")
    suspend fun getMedicineById(id: Int): Medicine?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMedicine(medicine: Medicine): Long

    @Update
    suspend fun updateMedicine(medicine: Medicine)

    @Delete
    suspend fun deleteMedicine(medicine: Medicine)

    @Query("DELETE FROM medicines WHERE id = :id")
    suspend fun deleteMedicineById(id: Int)
}