package com.suanfa.list;

/**
 * @Description: 单向循环链表
 * @Author: xiewu
 * @Date: 2022/2/14
 * @Time: 20:52
 */
public class MyLinkedLoopList {

    public MyNode head;
    public int size;

    public void insertHead(int data) {
        MyNode cur = new MyNode(data);
        if (head == null) {
            head = cur;
            head.next = head;
        } else {
            MyNode tailNode = head;
            for (int i = 1; i < size; i++) {
                tailNode = tailNode.next;
            }
            cur.next = head;
            head = cur;
            tailNode.next = head;
        }
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

    public void delete(int data) {
        MyNode cur = head;
        MyNode curPre = null;
        for (int i = 0; i < size; i++) {
            if (cur.value == data) {
                break;
            }
            curPre = cur;
            cur = cur.next;
        }
        if (cur.value != data) {
            return;
        }
        if (cur == head) {
            MyNode tailNode = head;
            for (int i = 1; i < size; i++) {
                tailNode = tailNode.next;
            }
            //删除头结点
            head = cur.next;
            tailNode.next = head;
        } else if (cur.next == head) {
            //删除尾结点
            curPre.next = head;
        } else {
            //删除中间结点
            curPre.next = curPre.next.next;
        }
        size--;
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
        for (int i = 0; i < size; i++) {
            System.out.println(positionNode.value);
            positionNode = positionNode.next;
        }
    }

    public static void main(String[] args) {
        MyLinkedLoopList myLinkedList = new MyLinkedLoopList();
        myLinkedList.insertHead(6);
        myLinkedList.insertHead(5);
        myLinkedList.insertHead(4);
        myLinkedList.insertHead(3);
        myLinkedList.insertHead(2);
        myLinkedList.insertHead(1);
//        myLinkedList.insertMiddle(2, 2);
//        myLinkedList.update(3, 4);
//        myLinkedList.delete(5);
//        myLinkedList.delete(4);
        myLinkedList.delete(6);
//        myLinkedList.delete(2);
//        myLinkedList.delete(3);
//        myLinkedList.delete(1);

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


