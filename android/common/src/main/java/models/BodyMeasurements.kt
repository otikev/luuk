package models

import com.google.gson.annotations.SerializedName

data class ActualMeasurements(
    @field:SerializedName("bodyMeasurements")
    var bodyMeasurements: BodyMeasurements? = null,
    @field:SerializedName("clothingSizes")
    var clothingSizes: ClothingSizes? = null
)

data class BodyMeasurements(
    @field:SerializedName("chest_cm")
    var chest: Int? = null,
    @field:SerializedName("waist_cm")
    var waist: Int? = null,
    @field:SerializedName("hips_cm")
    var hips: Int? = null,
)

data class ClothingSizes(
    @field:SerializedName("international")
    var international: String? = null,
    @field:SerializedName("us")
    var us: Int? = null,
    @field:SerializedName("uk")
    var uk: Int? = null,
    @field:SerializedName("eu")
    var eu: Int? = null,
)
