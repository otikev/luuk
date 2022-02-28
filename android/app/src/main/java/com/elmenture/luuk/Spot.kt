package com.elmenture.luuk

data class Spot(
        val id: Long = counter++,
        val url: String,
        val price: Long
) {
    companion object {
        private var counter = 0L
    }
}
