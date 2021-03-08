package com.ajax.ajaxtestassignment.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ajax.ajaxtestassignment.data.model.Contact

@Database(
    entities = [
        Contact::class
    ],
    version = 1
)
abstract class AjaxDatabase : RoomDatabase() {
    abstract fun contactsDao(): ContactsDao
}