package models

import com.google.gson.annotations.SerializedName

data class Order(

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("state")
	val state: String? = null,

	@field:SerializedName("merchantRequestID")
	val merchantRequestID: String? = null,

	@field:SerializedName("orderItems")
	val orderItems: List<OrderItem?>? = null
)

data class OrderItem(

	@field:SerializedName("item")
	val item: Item? = null,

	@field:SerializedName("id")
	val id: Int? = null
)
