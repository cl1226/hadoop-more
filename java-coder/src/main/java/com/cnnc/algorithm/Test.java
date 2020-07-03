package com.cnnc.algorithm;

import java.util.Stack;

public class Test {

	public static ListNode reserve(ListNode head) {
		ListNode pre = null;
		ListNode next = null;
		while (head != null) {
			next = head.next;
			head.next = pre;
			pre = head;
			head = next;
		}
		return pre;
	}

	// 使用n长度的stack
	public static boolean isPalindrome1(ListNode head) {
		Stack<ListNode> stack = new Stack<ListNode>();
		ListNode cur = head;
		while(cur != null) {
			stack.push(cur);
			cur = cur.next;
		}
		while(head != null) {
			if(head.val != stack.pop().val) {
				return false;
			}
			head = head.next;
		}
		return true;
	}

	// 使用n/2长度的stack
	public static boolean isPalindrome2(ListNode head) {
		ListNode slow = head;
		ListNode fast = head;
		while (fast.next != null && fast.next.next != null) {
			slow = slow.next;
			fast = fast.next.next;
		}
		Stack<ListNode> stack = new Stack<>();
		ListNode n = slow.next;
		while (n != null) {
			stack.push(n);
			n = n.next;
		}
		slow = head;
		while (slow != null && !stack.empty()) {
			if (slow.val != stack.pop().val) {
				return false;
			}
			slow = slow.next;
		}
		return true;
	}

	// 使用O(1)空间复杂度
	public static boolean isPalindrome3(ListNode head) {
		ListNode slow = head;
		ListNode fast = head;
		if (fast == null || fast.next == null) {
			return true;
		}
		if (fast.next.next == null && fast.val == fast.next.val) {
			return true;
		}
		if (fast.next.next == null && fast.val != fast.next.val) {
			return false;
		}
		if (fast == null || fast.next == null || fast.next.next == null) {
			return false;
		}
		while (fast.next != null && fast.next.next != null) {
			slow = slow.next;
			fast = fast.next.next;
		}
		ListNode pre = null;
		ListNode next = null;
		slow = slow.next;
		while (slow != null) {
			next = slow.next;
			slow.next = pre;
			pre = slow;
			slow = next;
		}
		ListNode t = pre;
		slow = head;
		boolean flag = true;
		while (slow != null && pre != null) {
			if (slow.val != pre.val) {
				flag = false;
				break;
			}
			slow = slow.next;
			pre = pre.next;
		}

		ListNode n1 = null;
		ListNode n2 = null;
		while (t != null) {
			n2 = t.next;
			t.next = n1;
			n1 = t;
			t = n2;
		}

		return flag;
	}

	public static void main(String[] args) {
		ListNode head = generateRandomLinkedList(10, 2);

		print(head);

//		System.out.println(isPalindrome1(head));
//		print(head);
//		System.out.println(isPalindrome2(head));
		System.out.println(isPalindrome3(head));
		print(head);

//		ListNode head = new ListNode(1);
//		head.next = new ListNode(2);
//		head.next.next = new ListNode(3);
//		head.next.next.next = new ListNode(4);
//		print(head);
//		print(reserve(head));

	}

	public static void print(ListNode head) {
		while (head != null) {
			System.out.print(head.val + "--->");
			head = head.next;
		}
		System.out.println("null");
	}

	public static ListNode generateRandomLinkedList(int len, int value) {
		int size = (int) (Math.random() * (len + 1));
		if (size == 0) {
			return null;
		}
		size--;
		ListNode head = new ListNode((int) (Math.random() * (value + 1)));
		ListNode pre = head;
		while (size != 0) {
			ListNode cur = new ListNode((int) (Math.random() * (value + 1)));
			pre.next = cur;
			pre = cur;
			size--;
		}
		return head;
	}

}
