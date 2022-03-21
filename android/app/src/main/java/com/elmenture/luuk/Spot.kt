package com.elmenture.luuk

data class Spot(
    val id: Long = counter++,
    val url: String,
    val priceCents: Long,
    val sizeInternational: String,
    val description: String,
    val sizeNumber: Int?,
    val itemId: Long
) {
    companion object {
        private var counter = 0L
    }
}
