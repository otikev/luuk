package com.luuk.common.models

import com.google.gson.annotations.SerializedName

data class BodyMeasurements(
    @field:SerializedName("neck")
    var neck: Int = 0,
    @field:SerializedName("shoulder")
    var shoulder: Int = 0,
    @field:SerializedName("chest")
    var chest: Int = 0,
    @field:SerializedName("waist")
    var waist: Int = 0,
    @field:SerializedName("thigh")
    var thigh: Int = 0,
    @field:SerializedName("leg")
    var leg: Int = 0,
)
