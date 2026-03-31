package com.medicare.app.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "food_schedules")
data class FoodSchedule(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val mealType: String,    // "Morning", "Afternoon", "Evening", "Night"
    val foodName: String,    // e.g. "Rice & Curry"
    val time: String,        // e.g. "07:30"
    val isEnabled: Boolean = true
)