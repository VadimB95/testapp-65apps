package com.example.employees65apps.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.employees65apps.databinding.EmployeeItemBinding
import com.example.employees65apps.domain.EmployeeWithSpecialties
import com.example.employees65apps.viewmodels.EmployeesViewModel

/**
 * Adapter for the employees list
 */
class EmployeesAdapter(private val viewModel: EmployeesViewModel) :
    ListAdapter<EmployeeWithSpecialties, EmployeesAdapter.EmployeeViewHolder>(DiffCallbackEmployees()) {

    override fun onBindViewHolder(holder: EmployeeViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(viewModel, item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmployeeViewHolder {
        return EmployeeViewHolder.from(parent)
    }

    class EmployeeViewHolder private constructor(val binding: EmployeeItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(viewModel: EmployeesViewModel, item: EmployeeWithSpecialties) {
            binding.viewModel = viewModel
            binding.employee = item
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): EmployeeViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = EmployeeItemBinding.inflate(layoutInflater, parent, false)

                return EmployeeViewHolder(binding)
            }
        }
    }
}

/**
 * Callback for calculating the diff between two non-null items in a list
 */
class DiffCallbackEmployees : DiffUtil.ItemCallback<EmployeeWithSpecialties>() {
    override fun areItemsTheSame(
        oldItem: EmployeeWithSpecialties,
        newItem: EmployeeWithSpecialties
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: EmployeeWithSpecialties,
        newItem: EmployeeWithSpecialties
    ): Boolean {
        return oldItem == newItem
    }
}