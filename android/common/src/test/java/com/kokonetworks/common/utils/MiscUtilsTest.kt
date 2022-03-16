package com.kokonetworks.common.utils

import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class MiscUtilsTest {
    @Before
    fun setUp() {
        // Do nothing
    }

    @Test
    fun testGetUserNameInitials() {
        var result = MiscUtils.getUserNameInitials("")
        Assert.assertEquals("", result)
        result = MiscUtils.getUserNameInitials(" ")
        Assert.assertEquals("", result)
        result = MiscUtils.getUserNameInitials("JohnDoe")
        Assert.assertEquals("J", result)
        result = MiscUtils.getUserNameInitials("John Doe")
        Assert.assertEquals("JD", result)
        result = MiscUtils.getUserNameInitials("John  Doe")
        Assert.assertEquals("JD", result)
        result = MiscUtils.getUserNameInitials("  John Doe")
        Assert.assertEquals("JD", result)
        result = MiscUtils.getUserNameInitials("John Doe ")
        Assert.assertEquals("JD", result)
        result = MiscUtils.getUserNameInitials(" John Doe ")
        Assert.assertEquals("JD", result)
        result = MiscUtils.getUserNameInitials("John Doe Lorem")
        Assert.assertEquals("JD", result)
    }

    @Test
    fun getCapitalizedText(){
        Assert.assertEquals("Qas Koko", MiscUtils.getAutoCapitalizedText("QAS   KOKO"))
    }

    @Test
    fun getFormattedAmount(){
    Assert.assertEquals("KES 3,233.98",MiscUtils.getFormattedAmount(3233.98, "KES"))
    Assert.assertEquals("KES 3,233,900.98",MiscUtils.getFormattedAmount(3233900.98, "KES"))
    Assert.assertEquals("KES 3,233",MiscUtils.getFormattedAmount(3233.00, "KES"))
    }

    @Test
    fun getTwoNames(){
        Assert.assertEquals("Qas", MiscUtils.getTwoNames("QAS"))
        Assert.assertEquals("Qas Koko", MiscUtils.getTwoNames("QAS koko"))
        Assert.assertEquals("Qas Koko", MiscUtils.getTwoNames(" QAS koko"))
        Assert.assertEquals("Qas Koko", MiscUtils.getTwoNames("    QAS koko   "))
    }
}