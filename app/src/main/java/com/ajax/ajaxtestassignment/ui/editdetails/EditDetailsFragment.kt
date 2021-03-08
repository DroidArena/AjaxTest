package com.ajax.ajaxtestassignment.ui.editdetails

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.core.content.getSystemService
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ajax.ajaxtestassignment.R
import com.ajax.ajaxtestassignment.data.model.Contact
import com.ajax.ajaxtestassignment.databinding.FragmentEditDetailsBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditDetailsFragment : Fragment() {
    companion object {
        const val ARG_ITEM_ID = "item_id"
    }

    private var _binding: FragmentEditDetailsBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<EditDetailsViewModel>()

    private val contactId by lazy {
        requireArguments().getLong(ARG_ITEM_ID, 0)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.toolbar.apply {
            setNavigationOnClickListener {
                findNavController().navigateUp()
            }
            setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.save -> {
                        validateAndSave()
                        true
                    }
                    else -> false
                }
            }
        }
        binding.email.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                validateAndSave()
                true
            } else {
                false
            }
        }
        binding.firstname.requestFocus()
        showKeyboard(binding.firstname)

        viewModel.setId(contactId)
        viewModel.loadingLiveData.observe(viewLifecycleOwner) { show ->
            setEnabledUiState(!show)
        }
        viewModel.saveLiveData.observe(viewLifecycleOwner) { e ->
            if (e == null) {
                Snackbar.make(binding.root, R.string.contact_saved, Snackbar.LENGTH_SHORT).apply {
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
        viewModel.errorLiveData.observe(viewLifecycleOwner) {
            Snackbar.make(binding.root, R.string.error, Snackbar.LENGTH_LONG).show()
        }
        viewModel.itemLiveData.observe(viewLifecycleOwner) { contact ->
            showContact(contact)
        }
    }

    fun showKeyboard(editText: EditText) {
        val inputManager: InputMethodManager = editText.context.getSystemService()!!
        inputManager.showSoftInput(editText, 0)
    }

    private fun setEnabledUiState(enabled: Boolean) {
        binding.firstname.isEnabled = enabled
        binding.lastname.isEnabled = enabled
        binding.email.isEnabled = enabled
        binding.toolbar.menu.findItem(R.id.save).isEnabled = enabled
    }

    private fun validateAndSave() {
        val firstname = binding.firstname.text.toString().trim()
        val lastname = binding.lastname.text.toString().trim()
        val email = binding.email.text.toString().trim()

        var hasErrors = false
        if (firstname.isBlank()) {
            binding.firstname.error = getString(R.string.error_first_name_empty)
            hasErrors = true
        } else {
            binding.firstname.error = null
        }
        if (lastname.isBlank()) {
            binding.lastname.error = getString(R.string.error_last_name_empty)
            hasErrors = true
        } else {
            binding.lastname.error = null
        }
        if (email.isBlank()) {
            binding.email.error = getString(R.string.error_email_empty)
            hasErrors = true
        } else {
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.email.error = getString(R.string.error_invalid_email)
                hasErrors = true
            } else {
                binding.email.error = null
            }
        }
        if (!hasErrors) {
            viewModel.save(firstname, lastname, email)
        }
    }

    private fun showContact(contact: Contact) {
        binding.toolbar.title = getString(R.string.fmt_name, contact.firstname, contact.lastname)
        binding.firstname.setText(contact.firstname)
        binding.lastname.setText(contact.lastname)
        binding.email.setText(contact.email)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}