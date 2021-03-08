package com.ajax.ajaxtestassignment.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ajax.ajaxtestassignment.R
import com.ajax.ajaxtestassignment.data.model.Contact
import com.ajax.ajaxtestassignment.databinding.FragmentDetailsBinding
import com.bumptech.glide.Glide
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

        viewModel.setId(contactId)
        viewModel.itemLiveData.observe(viewLifecycleOwner) { contact ->
            showContact(contact)
        }
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
        binding.fab.setOnClickListener {
            findNavController().navigate(DetailsFragmentDirections.showItemEditDetail(contactId))
        }
        return binding.root
    }

    private fun showContact(contact: Contact) {
        val name = getString(R.string.fmt_name, contact.firstname, contact.lastname)

        binding.toolbar.title = name
        binding.name.text = name
        binding.email.text = contact.email

        Glide.with(this)
            .load(contact.pictureLarge)
            .into(binding.avatar)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}