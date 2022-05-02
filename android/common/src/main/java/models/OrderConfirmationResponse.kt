package models

data class OrderConfirmationResponse(
    var id: Int,
    var state: String,
    var merchantRequestID: String
)