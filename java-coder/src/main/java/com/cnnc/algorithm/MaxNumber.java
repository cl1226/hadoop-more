package com.cnnc.algorithm;

public class MaxNumber {

    public static int getMaxNumber(int num) {
        char[] chars = String.valueOf(num).toCharArray();
        int index = 0;
        for (int i = 0; i < chars.length-1; i++) {
            if (chars[i] <= chars[i+1]) {
                index = i;
                break;
            }
        }
        chars[index] = 'x';
        String x = String.valueOf(chars).replace("x", "");
        return Integer.valueOf(x);
    }

    public static void main(String[] args) {
        int num = 47356;
        int m = 3;
        int res = num;
        for (int i = 1; i <= m; i++) {
            res = getMaxNumber(res);
            System.out.println(res);
        }
        System.out.println(res);
    }

}
