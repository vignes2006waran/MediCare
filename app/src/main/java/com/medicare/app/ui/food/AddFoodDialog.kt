package com.medicare.app.ui.food

import android.app.AlertDialog
import android.app.TimePickerDialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import com.medicare.app.data.model.FoodSchedule
import com.medicare.app.databinding.DialogAddFoodBinding
import java.text.SimpleDateFormat
import java.util.*

class AddFoodDialog(
    private val context: Context,
    private val food: FoodSchedule?,
    private val onSave: (FoodSchedule) -> Unit
) {
    fun show() {
        val binding = DialogAddFoodBinding.inflate(LayoutInflater.from(context))
        var selectedTime = food?.time ?: "08:00"

        val mealTypes = listOf("Morning", "Afternoon", "Evening", "Night")
        val spinnerAdapter = ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, mealTypes)
        binding.spinnerMealType.adapter = spinnerAdapter

        food?.let {
            binding.tvSelectedTime.text = formatTo12Hour(it.time)
            binding.spinnerMealType.setSelection(mealTypes.indexOf(it.mealType))
        } ?: run {
            binding.tvSelectedTime.text = formatTo12Hour(selectedTime)
        }

        binding.btnPickTime.setOnClickListener {
            val parts = selectedTime.split(":")
            val hour = parts[0].toIntOrNull() ?: 8
            val minute = parts[1].toIntOrNull() ?: 0

            TimePickerDialog(context, { _, h, m ->
                selectedTime = String.format("%02d:%02d", h, m)
                binding.tvSelectedTime.text = formatTo12Hour(selectedTime)
            }, hour, minute, false).show() // false for AM/PM view
        }

        val dialog = AlertDialog.Builder(context)
            .setTitle(if (food == null) "Add Meal" else "Edit Meal")
            .setView(binding.root)
            .setPositiveButton("Save", null)
            .setNegativeButton("Cancel", null)
            .create()

        dialog.setOnShowListener {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                val mealType = binding.spinnerMealType.selectedItem.toString()

                onSave(FoodSchedule(
                    id = food?.id ?: 0,
                    mealType = mealType,
                    foodName = mealType,
                    time = selectedTime
                ))
                dialog.dismiss()
            }
        }
        dialog.show()
    }

    private fun formatTo12Hour(time24h: String): String {
        return try {
            val sdf24 = SimpleDateFormat("HH:mm", Locale.getDefault())
            val sdf12 = SimpleDateFormat("hh:mm a", Locale.getDefault())
            val date = sdf24.parse(time24h)
            sdf12.format(date!!)
        } catch (e: Exception) {
            time24h
        }
    }
}