package com.cnnc.algorithm.pow;

public class SuperPow {


    public static int p(int a) {
        int res = 0;
        while (a != 0) {
            int mod = a % 10;
            if (res > Integer.MAX_VALUE/10 || (res == Integer.MAX_VALUE/10 && mod > 7)) {
                return 0;
            }
            if (res < Integer.MIN_VALUE/10 || (res == Integer.MIN_VALUE/10 && mod < -8)) {
                return 0;
            }
            res = res * 10 + mod;
            a /= 10;
        }
        return res;
    }

    public static void main(String[] args) {
        System.out.println(Integer.MAX_VALUE);
        System.out.println(Integer.MIN_VALUE);
        int a = -2147483648;
        int b = 123;
        int p = p(a);
        System.out.println(p);


    }

}
