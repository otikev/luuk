package models

import com.google.gson.annotations.SerializedName

data class Item(

    @field:SerializedName("price") //cents
    var price: Long? = null,

    @field:SerializedName("imageUrl")
    var imageUrl: String? = null,

    @field:SerializedName("sizeInternational")
    var sizeInternational: String? = null,

    @field:SerializedName("description")
    var description: String? = null,

    @field:SerializedName("id")
    var id: Long? = null,

    @field:SerializedName("sizeNumber")
    var sizeNumber: Int? = null,

    @field:SerializedName("target")
    var target: String? = null
)

fun Item.isSet(): Boolean {
    return this.price != null && !this.imageUrl.isNullOrEmpty() && !this.sizeInternational.isNullOrEmpty() &&
            !this.description.isNullOrBlank() && this.description.isNullOrBlank() && this.sizeNumber != null
}
