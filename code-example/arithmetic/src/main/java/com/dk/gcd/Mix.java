package com.dk.gcd;

/**
 * @author dkay
 * @version 1.0
 */
public class Mix {

    /**
     * 都是奇数 采用更相相减法
     * @see CommonDivisor
     * 采用
     */
    public int gcd(int a, int b) {
        if (a == b) {
            return a;
        }

        if (a == 0 || b == 0) {
            return a + b;
        }

        if (a < b) {
            return gcd(b, a);
        }


        if ((a & 1) == 1 && (b & 1) == 1) {
            return gcd(b, a - b);
        } else if ((a & 1) == 0 && (b & 1) == 1) {
            return gcd(a >> 1, b);
        } else if ((a & 1) == 1 && (b & 1) == 0) {
            return gcd(a, b >> 1);
        }

        return gcd(a >> 1, b >> 1) << 1;
    }
}
