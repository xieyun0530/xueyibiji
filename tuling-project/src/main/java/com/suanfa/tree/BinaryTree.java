package com.suanfa.tree;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

/**
 * 二叉树遍历
 */
public class BinaryTree {

    public static void main(String[] args) {
        Node H = new Node('H', null, null);
        Node K = new Node('K', null, null);
        Node D = new Node('D', null, null);
        Node G = new Node('G', H, K);
        Node C = new Node('C', D, null);
        Node F = new Node('F', G, null);
        Node E = new Node('E', null, F);
        Node B = new Node('B', null, C);
        Node A = new Node('A', B, E);
        beforeTree(A);
        System.out.println();
        inTree(A);
        System.out.println();
        afterTree(A);
        System.out.println();
        storeyTree(A);
    }

    /**
     * 前序遍历
     *
     * @param node
     */
    public static void beforeTree(Node node) {
        if (node == null) {
            return;
        }
        System.out.print(node.data);
        beforeTree(node.left);
        beforeTree(node.right);
    }

    /**
     * 中序遍历
     *
     * @param node
     */
    public static void inTree(Node node) {
        if (node == null) {
            return;
        }
        inTree(node.left);
        System.out.print(node.data);
        inTree(node.right);
    }

    /**
     * 后序遍历
     *
     * @param node
     */
    public static void afterTree(Node node) {
        if (node == null) {
            return;
        }
        afterTree(node.left);
        afterTree(node.right);
        System.out.print(node.data);
    }

    /**
     * 层次遍历
     *
     * @param node
     */
    public static void storeyTree(Node node) {
        List list = new ArrayList();
        ArrayDeque<Node> arrayDeque = new ArrayDeque();
        arrayDeque.add(node);
        while (!arrayDeque.isEmpty()) {
            Node nodeQue = arrayDeque.poll();
            if (null != nodeQue.left) {
                arrayDeque.add(node.left);
            }
            if (null != nodeQue.right) {
                arrayDeque.add(node.right);
            }
            list.add(nodeQue.data);
        }
        System.out.println(list.toString());
    }

}

class Node {
    public char data;
    public Node left;
    public Node right;

    public Node(char data, Node left, Node right) {
        this.data = data;
        this.left = left;
        this.right = right;
    }
}
