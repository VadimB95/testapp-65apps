package com.example.employees65apps.ui

import android.os.Bundle
import android.view.*
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.DividerItemDecoration.VERTICAL
import com.example.employees65apps.R
import com.example.employees65apps.databinding.FragmentSpecialtiesBinding
import com.example.employees65apps.viewmodels.SpecialtiesViewModel
import com.example.employees65apps.viewmodels.StaffApiStatus
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

/**
 * A fragment representing a list of specialties
 */
@AndroidEntryPoint
class SpecialtiesFragment : Fragment() {
    private val viewModel: SpecialtiesViewModel by viewModels()

    private lateinit var binding: FragmentSpecialtiesBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Set toolbar title
        activity?.setTitle(R.string.specialties_label)

        // Setup binding
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_specialties,
            container, false
        )
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        setupAdapter()
        setupSwipeRefreshLayout()
        setupRefreshStatusObserver()
        setupToolbar()
        setupNavigation()

        return binding.root
    }

    /**
     * Setup navigation listener
     */
    private fun setupNavigation() {
        viewModel.navigateToSelectedSpecialty.observe(viewLifecycleOwner) {
            if (it != null) {
                val action = SpecialtiesFragmentDirections
                    .actionSpecialtiesFragmentToEmployeesFragment(it)
                findNavController().navigate(action)
                viewModel.navigateToSelectedSpecialtyComplete()
            }
        }
    }

    /**
     * Setup toolbar with options menu
     */
    private fun setupToolbar() {
        setHasOptionsMenu(true)
    }

    /**
     * Setup specialties list adapter
     */
    private fun setupAdapter() {
        val adapter = SpecialtiesAdapter(viewModel)
        binding.specialtiesList.adapter = adapter

        viewModel.specialties.observe(viewLifecycleOwner, {
            it?.let {
                adapter.submitList(it)
            }
        })

        val decoration = DividerItemDecoration(context, VERTICAL)
        binding.specialtiesList.addItemDecoration(decoration)
    }

    /**
     * Setup swipe refresh layout listener
     */
    private fun setupSwipeRefreshLayout() {
        val refreshLayout = binding.specialtiesSwipeRefresh
        refreshLayout.setColorSchemeResources(
            R.color.secondaryColor,
            R.color.primaryColor
        )
        refreshLayout.setOnRefreshListener {
            viewModel.refreshDataFromRepository()
        }
    }

    /**
     * Setup refresh status observer to control UI elements depending on refresh status
     */
    private fun setupRefreshStatusObserver() {
        viewModel.refreshStatus.observe(viewLifecycleOwner) {
            it?.let { refreshStatus ->
                when (refreshStatus) {
                    StaffApiStatus.LOADING -> onNetworkLoading()
                    StaffApiStatus.DONE -> onNetworkDone()
                    StaffApiStatus.ERROR -> onNetworkError()
                }
            }
        }
    }

    /**
     * When refresh status is 'loading' show refresh circle
     */
    private fun onNetworkLoading() {
        binding.specialtiesSwipeRefresh.isRefreshing = true
    }

    /**
     * When refresh status is 'done' hide refresh circle and hide network error image
     */
    private fun onNetworkDone() {
        binding.specialtiesSwipeRefresh.isRefreshing = false
        binding.networkErrorImage.visibility = ImageView.GONE
    }

    /**
     * When refresh status is 'error' show snackbar and network error image (in case of no cached
     * data exist
     */
    private fun onNetworkError() {
        binding.specialtiesSwipeRefresh.isRefreshing = false

        if (viewModel.specialties.value.isNullOrEmpty()) {
            // If no cached data exist, show snackbar and no internet connection image
            Snackbar.make(
                binding.root, resources.getText(R.string.network_error_message_no_cache),
                Snackbar.LENGTH_LONG
            ).show()
            binding.networkErrorImage.visibility = ImageView.VISIBLE
        } else {
            // If cached data exist, show snackbar
            Snackbar.make(
                binding.root, resources.getText(R.string.network_error_message),
                Snackbar.LENGTH_LONG
            ).show()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem) =
        when (item.itemId) {
            R.id.menu_refresh -> {
                viewModel.refreshDataFromRepository()
                true
            }
            else -> false
        }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu, menu)
    }
}

