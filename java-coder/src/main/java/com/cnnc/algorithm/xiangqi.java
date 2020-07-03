package com.cnnc.algorithm;

public class xiangqi {

    public static int func(int x, int y, int k) {
        if (k == 0) {
            return x == 0 && y == 0 ? 1 : 0;
        }
        if (x < 0 || x > 9 || y < 0 || y > 8) {
            return 0;
        }
        return func(x + 2, y - 1, k - 1)
                + func(x + 2, y + 1, k - 1)
                + func(x + 1, y + 2, k - 1)
                + func(x - 1, y + 2, k - 1)
                + func(x - 2, y + 1, k - 1)
                + func(x - 2, y - 1, k - 1)
                + func(x - 1, y - 2, k - 1)
                + func(x + 1, y - 2, k - 1);
    }

    public static void main(String[] args) {
        System.out.println(func(3, 3, 10));
    }

}
