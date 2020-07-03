package com.cnnc.algorithm;

import java.util.Random;

public class Utils {

    // 合并有序链表
    public static ListNode mergeTwoLists(ListNode l1, ListNode l2) {
        ListNode h = new ListNode(0);
        ListNode cur = h;
        while (l1 != null && l2 != null) {
            h.next = new ListNode(l1.val <= l2.val ? l1.val : l2.val);
            h = h.next;
            if (l1.val <= l2.val) {
                l1 = l1.next;
            } else {
                l2 = l2.next;
            }
        }
        while (l1 != null) {
            h.next = new ListNode(l1.val);
            h = h.next;
            l1 = l1.next;
        }
        while (l2 != null) {
            h.next = new ListNode(l2.val);
            h = h.next;
            l2 = l2.next;
        }
        return cur.next;
    }

    public static void swap(ListNode n1, ListNode n2) {
        n1.val = n1.val ^ n2.val;
        n2.val = n1.val ^ n2.val;
        n1.val = n1.val ^ n2.val;
    }

    // 生产随机链表
    public static ListNode generateRandomLinkedList(int len, int value) {
        int size = len;
        if (size == 0) {
            return null;
        }
        size--;
        ListNode head = new ListNode((int) (new Random().nextInt(9)));
        ListNode pre = head;
        while (size != 0) {
            ListNode cur = new ListNode((int) (new Random().nextInt(9)));
            pre.next = cur;
            pre = cur;
            size--;
        }
        return head;
    }

    public static void print(ListNode head) {
        while (head != null) {
            System.out.print(head.val + "--->");
            head = head.next;
        }
        System.out.println("null");
    }

}
