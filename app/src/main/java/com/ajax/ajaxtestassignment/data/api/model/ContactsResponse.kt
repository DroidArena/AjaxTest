package com.ajax.ajaxtestassignment.data.api.model


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ContactsResponse(
    @Json(name = "info")
    val info: Info,
    @Json(name = "results")
    val results: List<Result>
)