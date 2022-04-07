package models

import com.google.gson.annotations.SerializedName

class SignInResponse {
    @field:SerializedName("success")
    var isSuccessful: Boolean = false

    @field:SerializedName("isNewAccount")
    var isNewAccount: Boolean = false

    @field:SerializedName("sessionKey")
    var sessionKey: String? = null

    @field:SerializedName("email")
    var email: String? = null

    @field:SerializedName("s3AccessKeyId")
    var s3AccessKeyId: String? = null

    @field:SerializedName("s3SecretKeyId")
    var s3SecretKeyId: String? = null

    @field:SerializedName("userMeasurements")
    var userMeasurements: UserMeasurements? = null
}