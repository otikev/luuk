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

    @field:SerializedName("measurements")
    var bodyMeasurements: BodyMeasurements? = null

    @field:SerializedName("staff")
    var isStaff: Boolean = false

    @field:SerializedName("s3AccessKeyId")
    var s3AccessKeyId: String? = null

    @field:SerializedName("s3SecretKeyId")
    var s3SecretKeyId: String? = null
}