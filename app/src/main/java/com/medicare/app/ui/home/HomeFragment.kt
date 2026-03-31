package com.medicare.app.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.medicare.app.R
import com.medicare.app.databinding.FragmentHomeBinding
import com.medicare.app.viewmodel.FoodViewModel
import com.medicare.app.viewmodel.MedicineViewModel
import java.text.SimpleDateFormat
import java.util.*

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var medicineViewModel: MedicineViewModel
    private lateinit var foodViewModel: FoodViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        medicineViewModel = ViewModelProvider(this)[MedicineViewModel::class.java]
        foodViewModel = ViewModelProvider(this)[FoodViewModel::class.java]

        // Set greeting and date
        setGreetingAndDate()

        // Observe medicine count
        medicineViewModel.allMedicines.observe(viewLifecycleOwner) { medicines ->
            val count = medicines.size
            binding.tvMedicineCount.text = "$count Medicine${if (count != 1) "s" else ""}"
            binding.tvMedicineSubtitle.text = if (count == 0) "No medicines added yet" else "Tap to manage"
        }

        // Observe food count
        foodViewModel.allFoodSchedules.observe(viewLifecycleOwner) { schedules ->
            val count = schedules.size
            binding.tvFoodCount.text = "$count Meal${if (count != 1) "s" else ""}"
            binding.tvFoodSubtitle.text = if (count == 0) "No meals added yet" else "Tap to manage"
        }

        // Card click listeners
        binding.cardMedicine.setOnClickListener {
            findNavController().navigate(R.id.medicineFragment)
        }

        binding.cardFood.setOnClickListener {
            findNavController().navigate(R.id.foodFragment)
        }

        binding.cardChat.setOnClickListener {
            findNavController().navigate(R.id.chatFragment)
        }
    }

    private fun setGreetingAndDate() {
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        val greeting = when {
            hour < 12 -> "Good Morning 🌤️"
            hour < 17 -> "Good Afternoon ☀️"
            hour < 21 -> "Good Evening 🌙"
            else -> "Good Night 🌛"
        }
        binding.tvGreeting.text = greeting

        val dateFormat = SimpleDateFormat("EEEE, dd MMM yyyy", Locale.getDefault())
        binding.tvDate.text = dateFormat.format(Date())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}