package com.luuk.common.network.service

import com.luuk.common.models.BodyMeasurements
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface EndPoints {
    @POST("user/update-measurements")
    fun postUserBodyMeasurements(@Body request: BodyMeasurements): Call<ResponseBody>
}