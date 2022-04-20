package models

import com.google.gson.annotations.SerializedName

class UpdateUserDetailsRequest {
    @SerializedName("name")
    var name: String? = null
    @SerializedName("email")
    var email: String? = null
    @SerializedName("contactPhoneNumber")
    var contactPhoneNumber: String? = null
    @SerializedName("mpesaPhoneNumber")
    var mpesaPhoneNumber: String? = null
    @SerializedName("targets")
    var targets: String? = null
    @SerializedName("physicalAddress")
    var physicalAddress: String? = null
}