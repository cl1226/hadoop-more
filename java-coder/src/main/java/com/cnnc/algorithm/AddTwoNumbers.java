package com.cnnc.algorithm;

public class AddTwoNumbers {

    public static ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        if (l1 == null || l2 == null) {
            return l1 != null ? l1 : l2;
        }
        ListNode head = new ListNode(0);
        ListNode cur = head;
        int m = 0;
        while (l1 != null && l2 != null) {
            int res = l1.val + l2.val;
            res += m;
            if (res >= 10) {
                m = res / 10;
            }
            cur.next = new ListNode(res >= 10 ? res % 10 : res);
            cur = cur.next;
            l1 = l1.next;
            l2 = l2.next;
        }
        while(l1 != null) {
            int res = l1.val + m;
            if (res >= 10) {
                m = res / 10;
            }
            cur.next = new ListNode(res >= 10 ? res % 10 : res);
            cur = cur.next;
            l1 = l1.next;
        }
        while(l2 != null) {
            int res = l2.val + m;
            if (res >= 10) {
                m = res / 10;
            }
            cur.next = new ListNode(res >= 10 ? res % 10 : res);
            cur = cur.next;
            l2 = l2.next;
        }
        return head.next;
    }

    public static void main(String[] args) {
        ListNode l1 = Utils.generateRandomLinkedList(3, 1);
        ListNode l2 = Utils.generateRandomLinkedList(3, 1);

        ListNode l3 = addTwoNumbers(l1, l2);

        Utils.print(l1);
        Utils.print(l2);
        Utils.print(l3);
    }

}
