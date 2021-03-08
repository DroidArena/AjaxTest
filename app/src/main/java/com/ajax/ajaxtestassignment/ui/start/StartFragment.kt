package com.ajax.ajaxtestassignment.ui.start

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ajax.ajaxtestassignment.R
import com.ajax.ajaxtestassignment.data.model.Contact
import com.ajax.ajaxtestassignment.databinding.FragmentStartBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StartFragment : Fragment() {
    private var _binding: FragmentStartBinding? = null

    private val binding get() = _binding!!

    private val viewModel by viewModels<StartViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.apply {
            inflateMenu(R.menu.main)
            setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.refresh -> {
                        viewModel.refresh()
                        true
                    }
                    else -> false
                }
            }
        }

        val adapter = ContactAdapter(object : ContactAdapter.ItemCLickListener {
            override fun onItemClick(contact: Contact) {
                findNavController().navigate(StartFragmentDirections.showItemDetail(contact.id))
            }

            override fun onItemRemove(id: Long) {
                viewModel.delete(id)
            }
        })
        binding.itemList.adapter = adapter

        viewModel.loadingLiveData.observe(viewLifecycleOwner) { show ->
            binding.progress.isVisible = show
        }
        viewModel.errorLiveData.observe(viewLifecycleOwner) {
            Snackbar.make(binding.root, R.string.error, Snackbar.LENGTH_LONG).show()
        }
        viewModel.contactsLiveData.observe(viewLifecycleOwner) { list ->
            adapter.submitList(list)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}