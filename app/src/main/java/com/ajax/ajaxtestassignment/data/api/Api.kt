package com.ajax.ajaxtestassignment.data.api

import com.ajax.ajaxtestassignment.data.api.model.ContactsResponse
import retrofit2.http.*

interface Api {
    @GET("/api/")
    suspend fun loadContacts(@Query("results") count: Int): ContactsResponse
}