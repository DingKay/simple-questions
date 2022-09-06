package com.dk.gcd;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author dkay
 * @version 1.0
 */
public class MixTest {

    private Mix mix = new Mix();

    @Test
    public void gcdTest() {
        Assert.assertEquals(mix.gcd(10, 20), 10);
        Assert.assertEquals(mix.gcd(12, 18), 6);
        Assert.assertEquals(mix.gcd(36, 8), 4);
        Assert.assertEquals(mix.gcd(7, 7), 7);
        Assert.assertEquals(mix.gcd(15, 5), 5);
        Assert.assertEquals(mix.gcd(7, 21), 7);
        Assert.assertEquals(mix.gcd(44, 21), 1);
    }
}
