package com.dk.gcd;

/**
 * @author dkay
 * @version 1.0
 */
public class CommonDivisor {

    /**
     * 更相相减法
     */
    public int gcd(int a, int b) {
        while (true) {
            if (a == b) {
                return a;
            }
            if (a > b) {
                a = a - b;
            } else {
                b = b - a;
            }
        }
    }
}
