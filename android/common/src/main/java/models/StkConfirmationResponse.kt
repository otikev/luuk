package models

import com.google.gson.annotations.SerializedName

data class StkConfirmationResponse (
    @SerializedName("merchantRequestID")
    var merchantRequestID: String
)