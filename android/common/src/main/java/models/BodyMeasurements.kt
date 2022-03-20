package models

import com.google.gson.annotations.SerializedName

data class BodyMeasurements(
    @field:SerializedName("sizeInternational")
    var sizeInternational: String? = "L",
    @field:SerializedName("sizeNumber")
    var sizeNumber: Int = 12
)
