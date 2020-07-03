package com.cnnc.algorithm;

import java.util.PriorityQueue;

public class HeapSort {

    public static void swap(int[] nums, int m, int n) {
        int temp = nums[m];
        nums[m] = nums[n];
        nums[n] = temp;
    }

    public static void heapSort(int[] nums) {
        if (nums == null || nums.length < 2) {
            return;
        }
        for (int i = nums.length - 1; i >= 0; i--) {
            heapify(nums, i, nums.length);
        }
    }

    public static void heapify(int[] nums, int index, int heapSize) {
        int left = index * 2 + 1;
        while (left < heapSize) {
            int largest = ((left + 1) < heapSize) && (nums[left + 1] > nums[left]) ? left + 1 : left;

            largest = nums[largest] > nums[index] ? largest : index;
            if (largest == index) {
                break;
            }
            swap(nums, largest, index);
            index = largest;
            left = index * 2 + 1;
        }
    }


    public static void main(String[] args) {

        int[] nums = new int[]{6,1,3,2,5,9,8,7};

        heapSort(nums);

        for (int i = 0; i < nums.length; i++) {
            System.out.print(nums[i]);
        }
        System.out.println("");

        int heapSize = nums.length;
        swap(nums, 0, --heapSize);
        while (heapSize > 0) { // O(N)
            heapify(nums, 0, heapSize); // O(logN)
            swap(nums, 0, --heapSize); // O(1)
        }
        for (int i = 0; i < nums.length; i++) {
            System.out.print(nums[i]);
        }
        System.out.println("");

//        System.out.println((3 << 1) + 1);
    }

}
