package com.example.employees65apps.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.employees65apps.databinding.SpecialtyItemBinding
import com.example.employees65apps.domain.Specialty
import com.example.employees65apps.viewmodels.SpecialtiesViewModel

/**
 * Adapter for the specialties list
 */
class SpecialtiesAdapter(private val viewModel: SpecialtiesViewModel) :
    ListAdapter<Specialty, SpecialtiesAdapter.SpecialtyViewHolder>(DiffCallbackSpecialties()) {

    override fun onBindViewHolder(holder: SpecialtyViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(viewModel, item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpecialtyViewHolder {
        return SpecialtyViewHolder.from(parent)
    }

    class SpecialtyViewHolder private constructor(val binding: SpecialtyItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(viewModel: SpecialtiesViewModel, item: Specialty) {
            binding.viewModel = viewModel
            binding.specialty = item
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): SpecialtyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = SpecialtyItemBinding.inflate(layoutInflater, parent, false)

                return SpecialtyViewHolder(binding)
            }
        }
    }
}

/**
 * Callback for calculating the diff between two non-null items in a list.
 */
class DiffCallbackSpecialties : DiffUtil.ItemCallback<Specialty>() {
    override fun areItemsTheSame(oldItem: Specialty, newItem: Specialty): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Specialty, newItem: Specialty): Boolean {
        return oldItem == newItem
    }
}