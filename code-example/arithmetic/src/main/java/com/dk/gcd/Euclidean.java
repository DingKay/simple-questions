package com.dk.gcd;

/**
 * @author dkay
 * @version 1.0
 */
public class Euclidean {
    public int gcd(int a, int b) {
        if (a == b) {
            return a;
        }

        if (a == 0 || b == 0) {
            return Math.max(a, b);
        }

        if (a > b) {
            a = a % b;
        } else {
            b = b % a;
        }

        return gcd(a, b);
    }
}
