package com.ajax.ajaxtestassignment.data

import com.ajax.ajaxtestassignment.data.api.Api
import com.ajax.ajaxtestassignment.data.database.ContactsDao
import com.ajax.ajaxtestassignment.data.model.Contact
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class ContactsRepositoryImpl(
    private val api: Api,
    private val contactsDao: ContactsDao,
    private val defaultDispatcher: CoroutineDispatcher
) : ContactsRepository {

    override suspend fun deleteContact(id: Long) {
        contactsDao.deleteContactById(id)
    }

    override suspend fun updateContact(contact: Contact) {
        contactsDao.updateContact(contact)
    }

    override suspend fun loadContact(id: Long): Contact {
        return contactsDao.getContact(id)
    }

    override fun loadContactAsFlow(id: Long): Flow<Contact> {
        return contactsDao.getContactAsFlow(id)
    }

    override fun loadContacts(): Flow<List<Contact>> {
        return contactsDao.getContacts()
    }

    override suspend fun refreshContacts(count: Int) {
        require(count > 0) { "count must be greater than 0" }

        val contacts = api.loadContacts(count)
        val dbContacts = withContext(defaultDispatcher) {
            contacts.results.map { result ->
                Contact(
                    0,
                    result.login.uuid,
                    result.name.first,
                    result.name.last,
                    result.picture?.large,
                    result.picture?.medium,
                    result.picture?.thumbnail,
                    result.email
                )
            }
        }
        contactsDao.replaceContacts(dbContacts)
    }
}