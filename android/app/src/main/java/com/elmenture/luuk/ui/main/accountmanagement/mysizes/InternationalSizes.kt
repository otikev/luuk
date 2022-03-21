package com.elmenture.luuk.ui.main.accountmanagement.mysizes

enum class InternationalSizes(var sizeName: String, var sizeMeasurement: SizeMeasurements) {
    XXS("XXS", SizeMeasurements(32, 26, 36)),
    XS("XS", SizeMeasurements(34, 28, 38)),
    S("S", SizeMeasurements(36, 30, 40)),
    M("M", SizeMeasurements(38, 32, 42)),
    L("L", SizeMeasurements(40, 34, 44)),
    XL("XL", SizeMeasurements(42, 36, 46)),
    XXL("XXL", SizeMeasurements(44, 38, 48)),
    _3XL("3XL", SizeMeasurements(46, 40, 50)),
    _4XL("4XL", SizeMeasurements(48, 42, 52)),
    _5XL("5XL", SizeMeasurements(50, 44, 54)),
    _6XL("6XL", SizeMeasurements(52, 46, 56)),
    _7XL("7XL", SizeMeasurements(54, 48, 58));


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
