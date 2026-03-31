package com.medicare.app.ui.medicine

import android.app.AlertDialog
import android.app.TimePickerDialog
import android.content.Context
import android.view.LayoutInflater
import com.medicare.app.data.model.Medicine
import com.medicare.app.databinding.DialogAddMedicineBinding
import java.text.SimpleDateFormat
import java.util.*

class AddEditMedicineDialog(
    private val context: Context,
    private val medicine: Medicine?,
    private val onSave: (Medicine) -> Unit
) {

    fun show() {
        val binding = DialogAddMedicineBinding.inflate(LayoutInflater.from(context))
        var selectedTime = medicine?.time ?: "08:00"

        // Pre-fill if editing
        medicine?.let {
            binding.etMedicineName.setText(it.name)
            binding.etDosage.setText(it.dosage)
            binding.etNotes.setText(it.notes)
            binding.tvSelectedTime.text = formatTo12Hour(it.time)
            if (it.beforeFood) binding.rbBeforeFood.isChecked = true
            else binding.rbAfterFood.isChecked = true
        } ?: run {
            binding.tvSelectedTime.text = formatTo12Hour(selectedTime)
        }

        // Time Picker
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
            .setTitle(if (medicine == null) "Add Medicine" else "Edit Medicine")
            .setView(binding.root)
            .setPositiveButton("Save", null)
            .setNegativeButton("Cancel", null)
            .create()

        dialog.setOnShowListener {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                val name = binding.etMedicineName.text.toString().trim()
                val dosage = binding.etDosage.text.toString().trim()
                val notes = binding.etNotes.text.toString().trim()
                val beforeFood = binding.rbBeforeFood.isChecked

                if (name.isEmpty()) {
                    binding.etMedicineName.error = "Medicine name is required"
                    return@setOnClickListener
                }
                if (dosage.isEmpty()) {
                    binding.etDosage.error = "Dosage is required"
                    return@setOnClickListener
                }

                val newMedicine = Medicine(
                    id = medicine?.id ?: 0,
                    name = name,
                    dosage = dosage,
                    time = selectedTime,
                    beforeFood = beforeFood,
                    notes = notes
                )
                onSave(newMedicine)
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