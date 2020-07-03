package com.cnnc.datastruct;

public class Lesson01 {

    public static void swap(int m, int n) {
        System.out.println("before swap: " + m + "\t" + n);
        m ^= n;
        n ^= m;
        m ^= n;
        System.out.println("after swap: " + m + "\t" + n);
    }

    public static void findOneOdd(int[] arr) {
        int eor = 0;
        int i = 0;
        while (i < arr.length) {
            eor ^= arr[i++];
        }
        System.out.println(eor);
    }

    public static void findTwoOdd(int[] arr) {
        int eor = 0;
        int i = 0;
        while (i < arr.length) {
            eor ^= arr[i++];
        }
        int rightOne = eor & (~eor + 1);
        int j = 0;
        int eor2 = 0;
        while (j < arr.length) {
            if ((arr[j] ^ rightOne) == 0) {
                eor2 ^= arr[j];
            }
            j++;
        }
        System.out.println(eor2 + "\t" + (eor2 ^ eor));
    }

    public static void main(String[] args) {
        swap(1, 2);
        findOneOdd(new int[]{2,3,3,2,3,4,4,1,3,4,2,2,1});
        findTwoOdd(new int[]{2,2,3,3,2,3,4,4,1,3,4,2,2,1,4,1});
    }
}
