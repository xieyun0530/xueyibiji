package com.jdbc;

/**
 * @Description
 * @Author: xiewu
 * @Date: 2022/5/15 19:35
 */
public class TagStock {

    private int id;
    private String pid;
    private int tagKeyId;
    private String tagValue;

    public TagStock(String pid, String tagValue) {
        this.pid = pid;
        this.tagValue = tagValue;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getTagValue() {
        return tagValue;
    }

    public void setTagValue(String tagValue) {
        this.tagValue = tagValue;
    }

    public int getTagKeyId() {
        return tagKeyId;
    }

    public void setTagKeyId(int tagKeyId) {
        this.tagKeyId = tagKeyId;
    }

    public static void main(String[] args) {
        ListNode head = new ListNode(1);
        ListNode node = head;
        for (int i = 2; i <= 5; i++) {
            node.next = new ListNode(i);
            node = node.next;
        }
        ListNode result = reverseList(head);
        while (result != null){
            System.out.println(result.val);
            result = result.next;
        }
    }

    public static class ListNode {
        int val;
        ListNode next;

        ListNode() {
        }

        ListNode(int val) {
            this.val = val;
        }

        ListNode(int val, ListNode next) {
            this.val = val;
            this.next = next;
        }
    }

    public static ListNode reverseList(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }
        ListNode result = reverseList(head.next);

        head.next.next = head;
        head.next = null;
        return result;
    }
}
