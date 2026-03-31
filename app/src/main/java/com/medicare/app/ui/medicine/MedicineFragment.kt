package com.medicare.app.ui.medicine

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.medicare.app.databinding.FragmentMedicineBinding
import com.medicare.app.notification.AlarmScheduler
import com.medicare.app.viewmodel.MedicineViewModel

class MedicineFragment : Fragment() {

    private var _binding: FragmentMedicineBinding? = null
    private val binding get() = _binding!!

    private lateinit var medicineViewModel: MedicineViewModel
    private lateinit var adapter: MedicineAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMedicineBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        medicineViewModel = ViewModelProvider(this)[MedicineViewModel::class.java]

        // Setup RecyclerView
        adapter = MedicineAdapter(
            onEdit = { medicine ->
                AddEditMedicineDialog(
                    context = requireContext(),
                    medicine = medicine,
                    onSave = { updated ->
                        medicineViewModel.update(updated)
                        AlarmScheduler.scheduleMedicineAlarm(requireContext(), updated)
                    }
                ).show()
            },
            onDelete = { medicine ->
                AlarmScheduler.cancelMedicineAlarm(requireContext(), medicine.id)
                medicineViewModel.delete(medicine)
            }
        )

        binding.recyclerMedicines.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerMedicines.adapter = adapter

        // Observe medicines
        medicineViewModel.allMedicines.observe(viewLifecycleOwner) { medicines ->
            adapter.submitList(medicines)
            binding.tvEmpty.visibility = if (medicines.isEmpty()) View.VISIBLE else View.GONE
        }

        // FAB — Add new medicine
        binding.fabAddMedicine.setOnClickListener {
            AddEditMedicineDialog(
                context = requireContext(),
                medicine = null,
                onSave = { newMedicine ->
                    medicineViewModel.insert(newMedicine) { generatedId ->
                        val medicineWithId = newMedicine.copy(id = generatedId.toInt())
                        AlarmScheduler.scheduleMedicineAlarm(requireContext(), medicineWithId)
                    }
                }
            ).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}