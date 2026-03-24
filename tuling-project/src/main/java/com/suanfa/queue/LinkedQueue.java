package com.suanfa.queue;

public class LinkedQueue {

    MyNode head;
    MyNode tail;
    int size;

    public void enQueue(int data) {
        MyNode cur = new MyNode(data);
        if (head == null) {
            tail = cur;
            head = cur;
        } else {
            cur.pre = tail;
            tail.next = cur;
            tail = cur;
        }
        size++;
    }

    public MyNode deQueue() {
        if (head == null) {
            return null;
        }
        MyNode value = null;
        if (head == tail) {
            head = null;
            tail = null;
            return null;
        } else {
            value = head;
            MyNode next = head.next;
            head = next;
        }
        size--;
        return value;
    }

    public void print() {
        MyNode cur = head;
        while (cur != null) {
            System.out.println("队列值：" + cur.value);
            cur = cur.next;
        }
    }

    class MyNode {
        public int value;
        public MyNode next;
        public MyNode pre;

        public MyNode(int value) {
            this.value = value;
        }
    }

    public static void main(String[] args) {
        LinkedQueue linkedQueue = new LinkedQueue();
        linkedQueue.enQueue(1);
        linkedQueue.enQueue(2);
        linkedQueue.enQueue(3);
        linkedQueue.enQueue(4);
        linkedQueue.print();
        System.out.println();
        System.out.println(linkedQueue.deQueue().value);
        System.out.println(linkedQueue.deQueue().value);
        System.out.println(linkedQueue.deQueue().value);
        System.out.println();
        linkedQueue.print();
    }
}
