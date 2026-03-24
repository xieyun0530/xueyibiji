package com.suanfa.list;

/**
 * @Description: 双向链表
 * @Author: xiewu
 * @Date: 2022/2/14
 * @Time: 22:46
 */
public class DoubleLinkedList {
    public MyNode head;
    public MyNode tail;
    public int size;

    public void insertHead(int data) {
        MyNode cur = new MyNode(data);
        if (null == head) {
            tail = cur;
        } else {
            cur.next = head;
            head.pre = cur;
        }
        head = cur;
        size++;
    }

    public void insertMiddle(int data, int position) {
        MyNode cur = new MyNode(data);
        if (null == head) {
            head = cur;
            tail = cur;
        } else {
            MyNode positionNode = null;
            for (int i = 0; i < position; i++) {
                positionNode = head.next;
            }
            cur.next = positionNode.next;
            cur.pre = positionNode.pre;
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
        //这里是先把当前的下一个节点的上一个节点指向当前节点的上一个节点
//        if (null != positionNode.next.next) {
//            positionNode.next.next.pre = positionNode.next;
//        }
        positionNode.next = positionNode.next.next;
        //第二种方式，当前节点的上一个节点是positionNode
        positionNode.next.pre = positionNode;
    }

    public void deleteKey(int data) {
        MyNode cur = head;
        while (null != cur) {
            if (data == cur.value) {
                break;
            }
            cur = cur.next;
        }
        if (cur == head) {
            head = head.next;
            head.pre = null;
        } else if (cur == tail) {
            tail = cur.pre;
            tail.next = null;
        } else {
            //当前节点的上一个节点的下一个节点指向当前的下一个节点，改变上一个节点的next指针
            cur.pre.next = cur.next;
            //当前节点的下一个节点的上一个节点指向当前的上一个节点，改变当前下一个节点的pre指针
            cur.next.pre = cur.pre;
        }

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
            System.out.println("pre:" + (positionNode.pre == null ? "" : positionNode.pre.value)
                    + "value:" + positionNode.value
                    + "next:" + (positionNode.next == null ? "" : positionNode.next.value));
            positionNode = positionNode.next;
        }
    }

    public static void main(String[] args) {
        DoubleLinkedList doubleLinkedList = new DoubleLinkedList();
        doubleLinkedList.insertHead(3);
        doubleLinkedList.insertHead(2);
        doubleLinkedList.insertHead(1);
        doubleLinkedList.insertHead(0);
//        doubleLinkedList.insertMiddle(2, 2);
//        doubleLinkedList.update(3, 4);
        doubleLinkedList.deleteKey(3);
//        doubleLinkedList.deleteKey(2);

        doubleLinkedList.print();
    }

    class MyNode {
        public int value;
        public MyNode pre;
        public MyNode next;

        public MyNode(int value) {
            this.value = value;
        }
    }

}


