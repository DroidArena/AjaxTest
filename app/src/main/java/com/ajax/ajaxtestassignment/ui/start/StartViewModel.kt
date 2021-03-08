package com.ajax.ajaxtestassignment.ui.start

import androidx.lifecycle.*
import com.ajax.ajaxtestassignment.data.ContactsRepository
import com.ajax.ajaxtestassignment.data.model.Contact
import com.ajax.ajaxtestassignment.data.preferences.UserPreferences
import com.ajax.ajaxtestassignment.ui.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class StartViewModel @Inject constructor(
    private val contactsRepository: ContactsRepository,
    private val userPreferences: UserPreferences): ViewModel() {

    private val _errorLiveEvent = SingleLiveEvent<Exception>()
    val errorLiveData: LiveData<Exception>
        get() = _errorLiveEvent

    private val _loadingLiveData = MutableLiveData<Boolean>()
    val loadingLiveData: LiveData<Boolean>
        get() = _loadingLiveData

    private val _contactsLiveData = MutableLiveData<List<Contact>>()
    val contactsLiveData: LiveData<List<Contact>>
        get() = _contactsLiveData

    init {
        viewModelScope.launch {
            try {
                contactsRepository.loadContacts()
                    .collect {
                        _contactsLiveData.value = it
                    }
            } catch (e: Exception) {
                Timber.e(e)

                _errorLiveEvent.value = e
            }
        }
        if (!userPreferences.isDataLoaded) {
            refresh()
        }
    }

    fun refresh() {
        _loadingLiveData.value = true
        viewModelScope.launch {
            try {
                contactsRepository.refreshContacts(30)
                userPreferences.isDataLoaded = true
            } catch (e: Exception) {
                Timber.w(e)

                _errorLiveEvent.value = e
            } finally {
                _loadingLiveData.value = false
            }
        }
    }

    fun delete(id: Long) {
        _loadingLiveData.value = true
        viewModelScope.launch {
            try {
                contactsRepository.deleteContact(id)
            } catch (e: Exception) {
                Timber.w(e)

                _errorLiveEvent.value = e
            } finally {
                _loadingLiveData.value = false
            }
        }
    }
}