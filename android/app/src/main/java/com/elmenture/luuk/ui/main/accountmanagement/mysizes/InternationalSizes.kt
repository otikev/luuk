package com.elmenture.luuk.ui.main.accountmanagement.mysizes

enum class InternationalSizes(var sizeName: String) {
    XXS("XXS"),
    XS("XS"),
    S("S"),
    M("M"),
    L("L"),
    XL("XL"),
    XXL("XXL"),
    _3XL("3XL"),
    _4XL("4XL"),
    _5XL("5XL"),
    _6XL("6XL"),
    _7XL("7XL");


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
