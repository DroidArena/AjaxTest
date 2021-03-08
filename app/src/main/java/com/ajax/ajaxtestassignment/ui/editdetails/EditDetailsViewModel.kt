package com.ajax.ajaxtestassignment.ui.editdetails

import androidx.lifecycle.*
import com.ajax.ajaxtestassignment.data.ContactsRepository
import com.ajax.ajaxtestassignment.ui.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class EditDetailsViewModel @Inject constructor(private val contactsRepository: ContactsRepository) :
    ViewModel() {
    private val _loadingLiveData = MutableLiveData<Boolean>()
    val loadingLiveData: LiveData<Boolean>
        get() = _loadingLiveData

    private val _errorLiveEvent = SingleLiveEvent<Exception>()
    val errorLiveData: LiveData<Exception>
        get() = _errorLiveEvent

    private val _saveLiveEvent = SingleLiveEvent<Exception?>()
    val saveLiveData: LiveData<Exception?>
        get() = _saveLiveEvent

    private val idLiveData = MutableLiveData<Long>()

    val itemLiveData = idLiveData.switchMap { id ->
        liveData(viewModelScope.coroutineContext) {
            try {
                val contact = contactsRepository.loadContact(id)
                emit(contact)
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }

    fun setId(id: Long) {
        if (idLiveData.value != id) {
            idLiveData.value = id
        }
    }

    fun save(firstname: String, lastname: String, email: String) {
        _loadingLiveData.value = true
        viewModelScope.launch {
            try {
                val contact = itemLiveData.value
                checkNotNull(contact) { "contact is not loaded yet" }

                contactsRepository.updateContact(
                    contact.copy(
                        firstname = firstname,
                        lastname = lastname,
                        email = email
                    )
                )

                _saveLiveEvent.value = null

            } catch (e: Exception) {
                Timber.w(e)

                _saveLiveEvent.value = e
            }
        }
    }
}