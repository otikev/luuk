package models

import com.google.gson.annotations.SerializedName

data class BodyMeasurements(
    @field:SerializedName("sizeInternational")
    var sizeInternational: String? = "L",
    @field:SerializedName("sizeNumber")
    var sizeNumber: Int = 0,
    @field:SerializedName("chest")
    var chest: Int = 0,
    @field:SerializedName("waist")
    var waist: Int = 0,
    @field:SerializedName("hips")
    var hips: Int = 0,
)
