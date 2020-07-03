package com.cnnc.algorithm;

public class GetIntersectionNode {

    public static ListNode getIntersectionNode(ListNode headA, ListNode headB) {
        ListNode a = headA;
        ListNode b = headB;
        while(a != b) {
            a = a == null ? headB : a.next;
            b = b == null ? headA : b.next;
        }
        return a;
    }

    public static void main(String[] args) {
        ListNode node = new ListNode(1);
        node.next = new ListNode(2);
        node.next.next = new ListNode(3);
        node.next.next.next = new ListNode(4);
        node.next.next.next.next = new ListNode(5);

        ListNode node1 = new ListNode(5);
        node1.next = new ListNode(6);
        node1.next.next = node.next.next.next;

        Utils.print(node);
        Utils.print(node1);

        ListNode intersectionNode = getIntersectionNode(node, node1);

        Utils.print(intersectionNode);
    }

}
