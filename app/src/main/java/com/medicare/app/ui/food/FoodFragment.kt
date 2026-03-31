package com.medicare.app.ui.food

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.medicare.app.databinding.FragmentFoodBinding
import com.medicare.app.notification.AlarmScheduler
import com.medicare.app.viewmodel.FoodViewModel

class FoodFragment : Fragment() {

    private var _binding: FragmentFoodBinding? = null
    private val binding get() = _binding!!
    private lateinit var foodViewModel: FoodViewModel
    private lateinit var adapter: FoodAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentFoodBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        foodViewModel = ViewModelProvider(this)[FoodViewModel::class.java]

        adapter = FoodAdapter(
            onEdit = { food ->
                AddFoodDialog(requireContext(), food) { updated ->
                    foodViewModel.update(updated)
                    AlarmScheduler.scheduleFoodAlarm(requireContext(), updated)
                }.show()
            },
            onDelete = { food ->
                AlarmScheduler.cancelFoodAlarm(requireContext(), food.id)
                foodViewModel.delete(food)
            }
        )

        binding.recyclerFood.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerFood.adapter = adapter

        foodViewModel.allFoodSchedules.observe(viewLifecycleOwner) { schedules ->
            adapter.submitList(schedules)
            binding.tvEmpty.visibility = if (schedules.isEmpty()) View.VISIBLE else View.GONE
        }

        binding.fabAddFood.setOnClickListener {
            AddFoodDialog(requireContext(), null) { newFood ->
                foodViewModel.insert(newFood)
                AlarmScheduler.scheduleFoodAlarm(requireContext(), newFood)
            }.show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}