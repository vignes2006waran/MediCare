package com.medicare.app.ui.medicine

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.medicare.app.data.model.Medicine
import com.medicare.app.databinding.ItemMedicineBinding

class MedicineAdapter(
    private val onEdit: (Medicine) -> Unit,
    private val onDelete: (Medicine) -> Unit
) : ListAdapter<Medicine, MedicineAdapter.MedicineViewHolder>(DiffCallback()) {

    inner class MedicineViewHolder(private val binding: ItemMedicineBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(medicine: Medicine) {
            binding.tvMedicineName.text = medicine.name
            binding.tvDosage.text = medicine.dosage
            binding.tvTime.text = "⏰ ${medicine.time}"
            binding.tvFoodTiming.text = if (medicine.beforeFood) "🍽️ Before Food" else "🍽️ After Food"

            if (medicine.notes.isNotEmpty()) {
                binding.tvNotes.visibility = android.view.View.VISIBLE
                binding.tvNotes.text = "📝 ${medicine.notes}"
            } else {
                binding.tvNotes.visibility = android.view.View.GONE
            }

            binding.btnEdit.setOnClickListener { onEdit(medicine) }
            binding.btnDelete.setOnClickListener { onDelete(medicine) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MedicineViewHolder {
        val binding = ItemMedicineBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return MedicineViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MedicineViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class DiffCallback : DiffUtil.ItemCallback<Medicine>() {
        override fun areItemsTheSame(oldItem: Medicine, newItem: Medicine) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Medicine, newItem: Medicine) =
            oldItem == newItem
    }
}