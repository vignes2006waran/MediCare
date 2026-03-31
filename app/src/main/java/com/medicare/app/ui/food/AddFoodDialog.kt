package com.medicare.app.ui.food

import android.app.AlertDialog
import android.app.TimePickerDialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import com.medicare.app.data.model.FoodSchedule
import com.medicare.app.databinding.DialogAddFoodBinding

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
            binding.tvSelectedTime.text = it.time
            binding.spinnerMealType.setSelection(mealTypes.indexOf(it.mealType))
        }

        binding.btnPickTime.setOnClickListener {
            val parts = selectedTime.split(":")
            TimePickerDialog(context, { _, h, m ->
                selectedTime = String.format("%02d:%02d", h, m)
                binding.tvSelectedTime.text = selectedTime
            }, parts[0].toIntOrNull() ?: 8, parts[1].toIntOrNull() ?: 0, true).show()
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

                // Since foodName is removed from UI, we'll use mealType as the name or leave it empty
                onSave(FoodSchedule(
                    id = food?.id ?: 0,
                    mealType = mealType,
                    foodName = mealType, // Use mealType as name since input is removed
                    time = selectedTime
                ))
                dialog.dismiss()
            }
        }
        dialog.show()
    }
}