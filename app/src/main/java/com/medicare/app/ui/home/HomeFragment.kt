package com.medicare.app.ui.home

import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
    
    private val handler = Handler(Looper.getMainLooper())
    private val updateTimeRunnable = object : Runnable {
        override fun run() {
            setGreetingAndDate()
            handler.postDelayed(this, 1000) // Update every second for "real-time"
        }
    }

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

        // Start real-time clock and greeting
        handler.post(updateTimeRunnable)

        // Observe medicine count
        medicineViewModel.allMedicines.observe(viewLifecycleOwner) { medicines ->
            val count = medicines.size
            binding.tvMedicineCount.text = "$count Medicine${if (count != 1) "s" else ""}"
            binding.tvMedicineSubtitle.text = if (count == 0) "No medicines added yet" else "Track your daily doses"
        }

        // Observe food count
        foodViewModel.allFoodSchedules.observe(viewLifecycleOwner) { schedules ->
            val count = schedules.size
            binding.tvFoodCount.text = "$count Meal${if (count != 1) "s" else ""}"
            binding.tvFoodSubtitle.text = if (count == 0) "No meals added yet" else "Manage your meal schedule"
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
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        
        val timeFormat = SimpleDateFormat("hh:mm:ss a", Locale.getDefault())
        val currentTime = timeFormat.format(calendar.time)

        val greetingText = when {
            hour < 12 -> "Good Morning 🌤️"
            hour < 17 -> "Good Afternoon ☀️"
            hour < 21 -> "Good Evening 🌙"
            else -> "Good Night 🌛"
        }
        
        binding.tvGreeting.text = "$greetingText\n$currentTime"

        val dateFormat = SimpleDateFormat("EEEE, dd MMM yyyy", Locale.getDefault())
        binding.tvDate.text = dateFormat.format(calendar.time)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        handler.removeCallbacks(updateTimeRunnable)
        _binding = null
    }
}