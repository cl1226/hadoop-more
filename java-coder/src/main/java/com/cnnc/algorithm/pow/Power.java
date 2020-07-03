package com.cnnc.algorithm.pow;

public class Power {

    public static double p(double x, int y) {
        if (y < 0) {
            x = 1/x;
            y = -y;
        }
        char[] chars = Integer.toBinaryString(y).toCharArray();
        double temp = x;
        double res = 1;
        for (int i = chars.length - 1; i >= 0; i--) {
            if (chars[i] == '1') {
                res *= temp;
            }
            temp *= temp;
        }
        return res;
    }

    public static void main(String[] args) {
        double x = 2.0000;
        int y = -2;
        double res = p(x, y);
        System.out.println(res);

    }

}
