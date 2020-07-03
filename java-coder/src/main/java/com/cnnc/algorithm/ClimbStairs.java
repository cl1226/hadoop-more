package com.cnnc.algorithm;

public class ClimbStairs {

    // 计算 10^12次方，将12转成二进制 1100，零位置只乘以自己，1位置乘到res中
    public static long power(long x, int y) {
        long res = 1;
        char[] chars = Integer.toBinaryString(y).toCharArray();
        for (int i = chars.length - 1; i >= 0; i--) {
            if (chars[i] == '1') {
                res *= x;
            }
            x *= x;
        }
        return res;
    }

    public static long[][] matrixPower(long[][] x, long[][] y) {
        long a = x[0][0] * y[0][0] + x[0][1] * y[1][0];
        long b = x[0][0] * y[0][1] + x[0][1] * y[1][1];
        long c = x[1][0] * y[0][0] + x[1][1] * y[1][0];
        long d = x[1][0] * y[0][1] + x[1][1] * y[1][1];
        return new long[][]{new long[]{a, b}, new long[]{c, d}};
    }

    public static void printMatrix(int[][] x) {
        for (int i = 0; i < x.length; i++) {
            for (int j = 0; j < x[i].length; j++) {
                System.out.print(x[i][j]);
            }
        }
    }

    public static long[][] power2(long[][] x, int y) {
        long[][] res = new long[2][2];
        res[0][0] = 1;
        res[1][1] = 1;
        char[] chars = Integer.toBinaryString(y).toCharArray();
        for (int i = chars.length - 1; i >= 0; i--) {
            if (chars[i] == '1') {
                res = matrixPower(x, res);
            }
            x = matrixPower(x, x);
        }
        return res;
    }

    public static void main(String[] args) {
//        long power = power(10, 15);
//        System.out.println(power);
        int n = 45;
        if (n == 1) {
            System.out.println(1);
        } else if (n == 2) {
            System.out.println(2);
        } else {
            long[][] x = new long[][]{new long[]{1, 1}, new long[]{1, 0}};
            long[][] temp = power2(x, n - 2);

            System.out.println();
            long res = 2 * temp[0][0] + temp[1][0];
            System.out.println(res);
            int r = (int) res;
            System.out.println(r);
        }
    }

}
