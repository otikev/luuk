package models;

import com.google.gson.annotations.SerializedName

class OrderStateUpdate {
    @SerializedName("orderId")
    var orderId: Long = 0
    @SerializedName("newState")
    lateinit  var  newState: String
}
