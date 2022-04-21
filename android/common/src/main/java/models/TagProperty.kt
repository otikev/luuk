package models

import com.google.gson.annotations.SerializedName

class TagProperty {
    @field:SerializedName("value")
    var value: String? = null

    @field:SerializedName("id")
    var id: Long? = null
}