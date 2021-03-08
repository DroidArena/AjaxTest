package com.ajax.ajaxtestassignment.data.database

import androidx.room.*
import com.ajax.ajaxtestassignment.data.model.Contact
import kotlinx.coroutines.flow.Flow

@Dao
abstract class ContactsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(contact: Contact): Long

    @Transaction
    open suspend fun replaceContacts(contacts: List<Contact>) {
        dropTable()
        insertContacts(contacts)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertContacts(contacts: List<Contact>)

    @Query("SELECT * FROM contacts")
    abstract fun getContacts(): Flow<List<Contact>>

    @Query("SELECT * FROM contacts WHERE id=:id")
    abstract fun getContactAsFlow(id: Long): Flow<Contact>

    @Query("SELECT * FROM contacts WHERE id=:id")
    abstract suspend fun getContact(id: Long): Contact

    @Query("DELETE FROM contacts WHERE id=:id")
    abstract suspend fun deleteContactById(id: Long)

    @Update
    abstract suspend fun updateContact(contact: Contact)

    @Delete
    abstract suspend fun deleteContact(contact: Contact)

    @Query("DELETE FROM contacts")
    abstract fun dropTable()
}