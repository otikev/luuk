package com.luuk.common.network.service

import com.luuk.common.models.BodyMeasurements
import com.luuk.common.models.SignInResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface EndPoints {
    @POST("user/update-measurements")
    fun postUserBodyMeasurements(@Body request: BodyMeasurements): Call<ResponseBody>

    @POST("auth/facebooksignin")
    fun facebookSignIn(@FieldMap params: Map<String, String>): Call<SignInResponse>

    @FormUrlEncoded
    @POST("auth/googlesignin")
    fun googleSignIn(@FieldMap params: Map<String, String>): Call<SignInResponse>
}