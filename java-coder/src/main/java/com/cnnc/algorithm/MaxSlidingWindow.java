package com.cnnc.algorithm;

import java.util.LinkedList;

public class MaxSlidingWindow {

    // nums = [1,3,-1,-3,5,3,6,7], 和 k = 3

    private LinkedList<Integer> list = new LinkedList<>();

    public int[] maxSlidingWindow(int[] nums, int k) {
        int[] res = new int[nums.length - k + 1];
        int index = 0;
        for (int i=0; i < nums.length; i++) {

            while (!list.isEmpty() && list.peekLast() <= nums[i]) {
                list.pollLast();
            }
            list.addLast(nums[i]);
            if (list.size() >= k) {
                res[index++] = list.pollFirst();
            }
        }
        return res;
    }

    public static void main(String[] args) {
        int[] nums = new int[]{1,3,-1,-3,5,3,6,7};
        MaxSlidingWindow maxSlidingWindow = new MaxSlidingWindow();
        int[] res = maxSlidingWindow.maxSlidingWindow(nums, 3);
        for (int re : res) {
            System.out.println(re);
        }
    }
}
