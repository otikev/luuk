package models

import com.google.gson.annotations.SerializedName

data class Size (
    @field:SerializedName("international")
    var international: String,

    @field:SerializedName("us")
    var us: Int,

    @field:SerializedName("uk")
    var uk: Int,

    @field:SerializedName("eu")
    var eu: Int,

    @field:SerializedName("chest_cm")
    var chest: String,

    @field:SerializedName("waist_cm")
    var waist: String,

    @field:SerializedName("hips_cm")
    var hips: String,
)