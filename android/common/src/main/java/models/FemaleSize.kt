package models

import com.google.gson.annotations.SerializedName

data class FemaleSize (
    @field:SerializedName("dress")
    var dress: Size? = null
)