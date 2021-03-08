package com.ajax.ajaxtestassignment.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "contacts",
    indices = [Index("uuid", unique = true)])
data class Contact(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long,

    @ColumnInfo(name = "uuid")
    val uuid: String,

    @ColumnInfo(name = "firstname")
    val firstname: String,

    @ColumnInfo(name = "lastname")
    val lastname: String,

    @ColumnInfo(name = "picture_large")
    val pictureLarge: String?,

    @ColumnInfo(name = "picture_medium")
    val pictureMedium: String?,

    @ColumnInfo(name = "picture_thumbnail")
    val pictureThumbnail: String?,

    @ColumnInfo(name = "email")
    val email: String
)
