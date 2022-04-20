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

    @field:SerializedName("staff")
    var isStaff: Boolean = false

    @field:SerializedName("s3AccessKeyId")
    var s3AccessKeyId: String? = null

    @field:SerializedName("s3SecretKeyId")
    var s3SecretKeyId: String? = null

    @field:SerializedName("actualMeasurements")
    var actualMeasurements: ActualMeasurements? = null

    @field:SerializedName("femaleSize")
    var femaleSize: FemaleSize? = null

    @field:SerializedName("contactPhoneNumber")
    var contactPhoneNumber: String? = null

    @field:SerializedName("clothingRecommendations")
    var clothingRecommendations: String? = null

    @field:SerializedName("physicalAddress")
    var physicalAddress: String? = null

    @field:SerializedName("name")
    var name: String? = null
}