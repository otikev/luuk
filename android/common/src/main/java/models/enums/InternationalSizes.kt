package models.enums

enum class InternationalSizes(var sizeName: String) {
    //modified to match backend size chart
    XS("XS"),
    S("S"),
    M("M"),
    L("L"),
    XL("XL"),
    XXL("XXL");

    companion object {
        fun fromString(value: String?): InternationalSizes? {
            return if (value != null) {
                if (value.toCharArray()[0].isDigit()) {
                    valueOf("_$value")
                } else {
                    valueOf(value)
                }
            } else {
                null
            }
        }
    }
}
