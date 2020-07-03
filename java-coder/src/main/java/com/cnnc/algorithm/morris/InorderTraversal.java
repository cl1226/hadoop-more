package com.cnnc.algorithm.morris;

import java.util.ArrayList;
import java.util.List;

public class InorderTraversal {

    public static void print(List<Integer> list, TreeNode root) {
        if (root == null) {
            return;
        }
        print(list, root.left);
        list.add(root.val);
        print(list, root.right);
    }
    public static List<Integer> inorderTraversal(TreeNode root) {
        List<Integer> list = new ArrayList<>();
        print(list, root);
        return list;
    }

    public static void main(String[] args) {
        TreeNode root = new TreeNode(1);
        root.right = new TreeNode(2);
        root.right.left = new TreeNode(3);
        List<Integer> list = inorderTraversal(root);
        System.out.println(list.toString());
    }

}
