package com.medicare.app.ui.food

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.medicare.app.data.model.FoodSchedule
import com.medicare.app.databinding.ItemFoodBinding

class FoodAdapter(
    private val onEdit: (FoodSchedule) -> Unit,
    private val onDelete: (FoodSchedule) -> Unit
) : ListAdapter<FoodSchedule, FoodAdapter.FoodViewHolder>(DiffCallback()) {

    inner class FoodViewHolder(private val binding: ItemFoodBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(food: FoodSchedule) {
            val emoji = when (food.mealType) {
                "Morning" -> "🌅"
                "Afternoon" -> "☀️"
                "Evening" -> "🌇"
                "Night" -> "🌙"
                else -> "🍽️"
            }
            binding.tvMealType.text = "$emoji ${food.mealType}"
            binding.tvFoodName.text = food.foodName
            binding.tvFoodTime.text = "⏰ ${food.time}"
            binding.btnEdit.setOnClickListener { onEdit(food) }
            binding.btnDelete.setOnClickListener { onDelete(food) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        val binding = ItemFoodBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FoodViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) = holder.bind(getItem(position))

    class DiffCallback : DiffUtil.ItemCallback<FoodSchedule>() {
        override fun areItemsTheSame(oldItem: FoodSchedule, newItem: FoodSchedule) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: FoodSchedule, newItem: FoodSchedule) = oldItem == newItem
    }
}