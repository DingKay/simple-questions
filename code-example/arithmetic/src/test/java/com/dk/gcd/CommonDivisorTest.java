package com.dk.gcd;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author dkay
 * @version 1.0
 */
public class CommonDivisorTest {

    private final CommonDivisor commonDivisor = new CommonDivisor();

    @Test
    public void gcdTest() {
        Assert.assertEquals(commonDivisor.gcd(10, 20), 10);
        Assert.assertEquals(commonDivisor.gcd(12, 18), 6);
        Assert.assertEquals(commonDivisor.gcd(36, 8), 4);
    }
}
