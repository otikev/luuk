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
    var sizeNumber: Int? = null
)

fun Item.isSet(): Boolean {
    return price != null && !imageUrl.isNullOrEmpty() && !sizeInternational.isNullOrEmpty() &&
            !description.isNullOrBlank() && description.isNullOrBlank() && sizeNumber !=null
}
