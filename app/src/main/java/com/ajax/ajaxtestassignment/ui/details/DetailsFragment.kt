package com.ajax.ajaxtestassignment.ui.details

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
import com.ajax.ajaxtestassignment.databinding.FragmentDetailsBinding
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsFragment : Fragment() {
    companion object {
        const val ARG_ITEM_ID = "item_id"
    }

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<DetailsViewModel>()

    private val contactId by lazy {
        requireArguments().getLong(ARG_ITEM_ID, 0)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.delete -> {
                    setEnabledUiState(false)
                    viewModel.delete(contactId)
                    true
                }
                else -> false
            }
        }
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
        binding.fab.setOnClickListener {
            findNavController().navigate(DetailsFragmentDirections.showItemEditDetail(contactId))
        }

        viewModel.setId(contactId)
        viewModel.itemLiveData.observe(viewLifecycleOwner) { contact ->
            showContact(contact)
        }
        viewModel.deleteLiveData.observe(viewLifecycleOwner) { e ->
            if (e == null) {
                Snackbar.make(binding.root, R.string.contact_deleted, Snackbar.LENGTH_SHORT).apply {
                    addCallback(object :
                        Snackbar.Callback() {
                        override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                            findNavController().navigateUp()
                        }
                    })
                    show()
                }
            } else {
                setEnabledUiState(true)
                Snackbar.make(binding.root, R.string.error, Snackbar.LENGTH_LONG).show()
            }
        }
    }

    private fun setEnabledUiState(enabled: Boolean) {
        binding.toolbar.menu.findItem(R.id.delete).isEnabled = enabled
        binding.fab.isEnabled = enabled
    }

    private fun showContact(contact: Contact) {
        val name = getString(R.string.fmt_name, contact.firstname, contact.lastname)

        binding.toolbar.title = name
        binding.name.text = name
        binding.email.text = contact.email

        if (!contact.pictureLarge.isNullOrEmpty()) {
            binding.avatar.isVisible = true
            Glide.with(this)
                .load(contact.pictureLarge)
                .into(binding.avatar)
        } else {
            binding.avatar.isVisible = false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}