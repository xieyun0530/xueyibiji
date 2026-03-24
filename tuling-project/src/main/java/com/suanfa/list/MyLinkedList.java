package com.suanfa.list;

/**
 * @Description: 单向链表
 * @Author: xiewu
 * @Date: 2022/2/14
 * @Time: 20:52
 */
public class MyLinkedList {

    public MyNode head;
    public int size;

    public void insertHead(int data) {
        MyNode cur = new MyNode(data);
        cur.next = head;
        head = cur;
        size++;
    }

    public void insertMiddle(int data, int position) {
        MyNode cur = new MyNode(data);
        if (null == head) {
            head = cur;
        } else {
            MyNode positionNode = null;
            for (int i = 0; i < position; i++) {
                positionNode = head.next;
            }
            cur.next = positionNode.next;
            positionNode.next = cur;
        }
    }

    public void update(int position, int data) {
        if (position == 0) {
            head.value = data;
        }
        MyNode cur = head;
        for (int i = 0; i < position; i++) {
            cur = cur.next;
        }
        cur.value = data;
    }

    public void delete(int position) {
        if (position == 0) {
            head = head.next;
            return;
        }
        MyNode positionNode = head;
        //这里需要从1开始，如果删除的是下标为1时这里不会走循环，
        // 如果从0开始这找到的是要删除的节点本身，实际需要找到要删除的节点的上一个节点，所以从1开始少循环一次
        for (int i = 1; i < position; i++) {
            positionNode = positionNode.next;
        }
        positionNode.next = positionNode.next.next;
    }

    public MyNode find(int position) {
        if (position == 0) {
            return head;
        }
        MyNode cur = null;
        for (int i = 0; i < position; i++) {
            cur = head.next;
        }
        return cur;
    }

    public void print() {
        MyNode positionNode = head;
        while (null != positionNode) {
            System.out.println(positionNode.value);
            positionNode = positionNode.next;
        }
    }

    public static void main(String[] args) {
        MyLinkedList myLinkedList = new MyLinkedList();
        myLinkedList.insertHead(3);
        myLinkedList.insertHead(2);
        myLinkedList.insertHead(1);
        myLinkedList.insertHead(0);
//        myLinkedList.insertMiddle(2, 2);
//        myLinkedList.update(3, 4);
        myLinkedList.delete(2);

        myLinkedList.print();
    }
    class MyNode {
        public int value;
        public MyNode next;

        public MyNode(int value) {
            this.value = value;
        }
    }
}


