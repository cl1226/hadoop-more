package com.cnnc.algorithm;

public class SortList {


    public static ListNode sort(ListNode head) {
        ListNode slow = head;
        ListNode fast = head;
        if (fast == null || fast.next == null) {
            return fast;
        }
        if (fast.next.next == null) {
            if (fast.val <= fast.next.val) {
                return fast;
            } else {
                Utils.swap(fast, fast.next);
                return fast;
            }
        }
        while(slow != null && fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }
        sort(head);
        ListNode next = slow.next;
        slow.next = null;
        sort(next);
        return head;
    }

    public static void main(String[] args) {
        ListNode node = Utils.generateRandomLinkedList(5, 1);
        Utils.print(node);
        sort(node);
        Utils.print(node);
    }

}
