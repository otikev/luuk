package models

data class Spot(
//    val id: Long = counter++,
    val url: String,
    val priceCents: Long,
    val description: String,
    val sizeInternational: String?,
    val sizeNumber: Int?,
    val sizeType: String,
    val itemId: Long,
    var tagProperties: List<Long>? = null
) {
    companion object {
        private var counter = 0L
    }
}