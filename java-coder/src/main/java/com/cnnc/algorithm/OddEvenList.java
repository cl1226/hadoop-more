package com.cnnc.algorithm;

public class OddEvenList {

    public static ListNode oddEven(ListNode head) {

        if (head == null) {
            return head;
        }

        ListNode odd = head;
        ListNode even = odd.next;
        ListNode evenHead = even;

        while (even != null && even.next != null) {
            odd.next = even.next;
            odd = odd.next;
            even.next = odd.next;
            even = even.next;
        }
        odd.next = evenHead;
        return head;

    }


    public static void main(String[] args) {
        ListNode node = Utils.generateRandomLinkedList(10, 1);
        Utils.print(node);
        oddEven(node);
        Utils.print(node);

    }

}
