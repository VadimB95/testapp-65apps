package com.example.employees65apps.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.employees65apps.R
import com.example.employees65apps.databinding.FragmentEmployeesBinding
import com.example.employees65apps.viewmodels.EmployeesViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * A fragment representing list of employees having selected specialty
 */
@AndroidEntryPoint
class EmployeesFragment : Fragment() {
    private val args: EmployeesFragmentArgs by navArgs()

    @Inject
    lateinit var employeesViewModelFactory: EmployeesViewModel.AssistedFactory

    private val viewModel: EmployeesViewModel by viewModels {
        EmployeesViewModel.provideFactory(
            employeesViewModelFactory,
            args.specialty
        )
    }

    private lateinit var binding: FragmentEmployeesBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Set toolbar title
        activity?.setTitle(R.string.employees_list_label)

        // Setup binding
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_employees,
            container, false
        )
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        setupAdapter()
        setupNavigation()

        return binding.root
    }

    /**
     * Setup employees list adapter
     */
    private fun setupAdapter() {
        val employeesList = binding.employeesList
        val adapter = EmployeesAdapter(viewModel)
        employeesList.adapter = adapter

        viewModel.employees.observe(viewLifecycleOwner, {
            it?.let {
                adapter.submitList(it)
            }
        })

        // Disable animation
        employeesList.itemAnimator = null
    }

    /**
     * Setup navigation listener
     */
    private fun setupNavigation() {
        viewModel.navigateToSelectedEmployee.observe(viewLifecycleOwner) {
            if (it != null) {
                val action = EmployeesFragmentDirections
                    .actionEmployeesFragmentToEmployeeDetailedFragment(it)
                findNavController().navigate(action)
                viewModel.navigateToSelectedEmployeeComplete()
            }
        }
    }
}