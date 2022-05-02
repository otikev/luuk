package network.service

import models.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface EndPoints {
    @POST("user/measurements/update")
    fun postUserBodyMeasurements(@Body request: ActualMeasurements): Call<ResponseBody>

    @GET("user/measurements")
    fun getUserBodyMeasurements(): Call<BodyMeasurements>

    @POST("auth/facebooksignin")
    fun facebookSignIn(@FieldMap params: Map<String, String>): Call<SignInResponse>

    @FormUrlEncoded
    @POST("auth/googlesignin")
    fun googleSignIn(@FieldMap params: Map<String, String>): Call<SignInResponse>

    @GET("items/paginated")
    fun fetchItemsPaginated(@Query("page") page: Int, @Query("size") size: Int): Call<ItemResponse>

    @POST("items/new")
    fun createNewItem(@Body request: Item): Call<ResponseBody>


    @GET("items/all")
    fun fetchAllItems(): Call<List<Item>>

    @POST("user/update")
    fun updateUserDetails(@Body request: UpdateUserDetailsRequest): Call<ResponseBody>

    @POST("items/update")
    fun updateItem(@Body request: Item): Call<ResponseBody>


    @POST("order/validate")
    fun validateCartItems(@Body cartItems: ArrayList<Long>): Call<StkConfirmationResponse>

    @GET("order/confirm")
    fun confirmOrder(@Query("merchant_request_id") merchantRequestID: String): Call<OrderConfirmationResponse>
}