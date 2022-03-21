package models

import com.google.gson.annotations.SerializedName

data class Item(

	@field:SerializedName("price")
	val price: Long? = null,

	@field:SerializedName("imageUrl")
	val imageUrl: String? = null,

	@field:SerializedName("sizeInternational")
	val sizeInternational: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("id")
	val id: Long? = null,

	@field:SerializedName("sizeNumber")
	val sizeNumber: Int? = null
)
