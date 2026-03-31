package com.medicare.app.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "medicines")
data class Medicine(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val dosage: String,        // e.g. "1 tablet", "5ml"
    val time: String,          // e.g. "08:00"
    val beforeFood: Boolean,   // true = before food, false = after food
    val isEnabled: Boolean = true,
    val notes: String = ""
)