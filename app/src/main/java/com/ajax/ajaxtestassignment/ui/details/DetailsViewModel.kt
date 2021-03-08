package com.ajax.ajaxtestassignment.ui.details

import androidx.lifecycle.*
import com.ajax.ajaxtestassignment.data.ContactsRepository
import com.ajax.ajaxtestassignment.data.model.Contact
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(private val contactsRepository: ContactsRepository) :
    ViewModel() {

    private val idLiveData = MutableLiveData<Long>()

    private val _itemLiveData = MediatorLiveData<Contact>().apply {
        addSource(idLiveData) { id ->
            loadContact(id)
        }
    }
    val itemLiveData: LiveData<Contact>
        get() = _itemLiveData

    private fun loadContact(id: Long) {
        viewModelScope.launch {
            try {
                contactsRepository.loadContactAsFlow(id).collect {
                    _itemLiveData.value = it
                }
            } catch (e: Exception) {
                Timber.w(e)
            }
        }
    }

    fun setId(id: Long) {
        if (idLiveData.value != id) {
            idLiveData.value = id
        }
    }
}