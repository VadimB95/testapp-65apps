package com.example.employees65apps.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.employees65apps.R
import com.example.employees65apps.databinding.FragmentEmployeeDetailedBinding

/**
 * A fragment representing employee detailed information
 */
class EmployeeDetailedFragment : Fragment() {
    private val args: EmployeeDetailedFragmentArgs by navArgs()
    private lateinit var binding: FragmentEmployeeDetailedBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Set toolbar title
        activity?.setTitle(R.string.employee_info_label)

        // Setup binding
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_employee_detailed,
            container, false
        )
        binding.lifecycleOwner = this
        binding.employee = args.employee

        return binding.root
    }

}