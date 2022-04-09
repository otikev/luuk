package models

data class ItemResponse (
    val content: ArrayList<Item>? = null,
    val pageNo: Int = 0,
    val pageSize: Int = 0,
    val totalElements: Long = 0,
    val totalPages: Int = 0,
    val last: Boolean = false,
)