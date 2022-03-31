package network.service

import models.BodyMeasurements
import models.Item
import models.ItemResponse
import models.SignInResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface EndPoints {
    @POST("user/measurements/update")
    fun postUserBodyMeasurements(@Body request: BodyMeasurements): Call<ResponseBody>

    @GET("user/measurements")
    fun getUserBodyMeasurements(): Call<BodyMeasurements>

    @POST("auth/facebooksignin")
    fun facebookSignIn(@FieldMap params: Map<String, String>): Call<SignInResponse>

    @FormUrlEncoded
    @POST("auth/googlesignin")
    fun googleSignIn(@FieldMap params: Map<String, String>): Call<SignInResponse>

    @GET("items/all")
    fun fetchItems(): Call<List<Item>>

    @GET("items/paginated")
    fun fetchItemsPaginated(@Query("page") page: Int, @Query("size") size: Int): Call<ItemResponse>

    @POST("items/new")
    fun createNewItem(@Body request: Item): Call<ResponseBody>
}