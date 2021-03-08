package com.ajax.ajaxtestassignment.data.api

import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*
import ua.medstar.idis.data.db.model.*
import ua.medstar.idis.data.model.ConsumablesRequest
import ua.medstar.idis.data.model.MediaModel
import ua.medstar.idis.data.api.model.ObservationModel
import ua.medstar.idis.data.model.UpdateResponse

interface Api {
    @FormUrlEncoded
    @POST("numbers/")
    suspend fun getNumbers(@Field("quantity") quantity: Int): List<String>

    @GET("kit/")
    suspend fun getKit(): Kit

    @POST("kit/")
    suspend fun postKit(@Body kit: Kit): Kit

    @POST("result/")
    suspend fun postExamination(@Body observationModel: ObservationModel): Response<Unit>

    @Multipart
    @POST("result-media/")
    suspend fun uploadMedia(@Part file: MultipartBody.Part): MediaModel

    @POST("ticket/")
    suspend fun postConsumablesRequest(@Body consumablesRequest: ConsumablesRequest)

    @GET("v1/mobile-application/")
    suspend fun getNewVersion(
        @Query("version_code") versionCode: Int,
        @Query("type") deviceType: String
    ): UpdateResponse

}