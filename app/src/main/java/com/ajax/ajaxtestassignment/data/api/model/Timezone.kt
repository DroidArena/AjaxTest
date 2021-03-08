package com.ajax.ajaxtestassignment.data.api.model


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Timezone(
    @Json(name = "description")
    val description: String,
    @Json(name = "offset")
    val offset: String
)