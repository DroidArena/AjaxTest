package com.ajax.ajaxtestassignment.ui.editdetails

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ajax.ajaxtestassignment.R

class EditDetailsFragment : Fragment() {

    companion object {
        fun newInstance() = EditDetailsFragment()
    }

    private lateinit var viewModel: EditDetailsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.edit_details_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(EditDetailsViewModel::class.java)
        // TODO: Use the ViewModel
    }

}