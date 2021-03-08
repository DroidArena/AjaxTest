package com.ajax.ajaxtestassignment.data

import com.ajax.ajaxtestassignment.data.model.Contact
import kotlinx.coroutines.flow.Flow

interface ContactsRepository {
    fun loadContacts(): Flow<List<Contact>>
    suspend fun refreshContacts(count: Int)
    suspend fun loadContact(id: Long): Contact
    fun loadContactAsFlow(id: Long): Flow<Contact>
    suspend fun deleteContact(id: Long)
    suspend fun updateContact(contact: Contact)
}
