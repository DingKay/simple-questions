package com.dk.gcd;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author dkay
 * @version 1.0
 */
public class EuclideanTest {

    private final Euclidean euclidean = new Euclidean();

    @Test
    public void gcdTest() {
        Assert.assertEquals(euclidean.gcd(10, 20), 10);
        Assert.assertEquals(euclidean.gcd(12, 18), 6);
        Assert.assertEquals(euclidean.gcd(36, 8), 4);
    }
}
