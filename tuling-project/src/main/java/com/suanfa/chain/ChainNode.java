package com.suanfa.chain;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

/**
 * 链表反转
 * @Description
 * @Author: xiewu
 * @Date: 2022/5/15 19:35
 */
public class ChainNode {

    private int id;
    private String pid;
    private int tagKeyId;
    private String tagValue;

    public ChainNode(String pid, String tagValue) {
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
        for (int i = 2; i <= 4; i++) {
            node.next = new ListNode(i);
            node = node.next;
        }
        ListNode result = reverseList(head);
        while (result != null){
            System.out.println(result.val);
            result = result.next;
        }
    }

    @Data
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

        //反转的关键，把循环到的当前指针的下一个节点的指针指向它的前一个节点，这里不能使用result.next因为这个result节点是一个反转后的尾节点
        //这里就是result的最后一个节点，把result的最后一个节点指向head，得到反转结果（head其实就是result的最后一个节点的上一个节点）
        //1->2->3->4 变成 1->2->3->4->3
        head.next.next = head;
        //断开上一个节点的指针，解开环形指针，这个时候就变成了两个链表
        // head：3 result：4->3
        head.next = null;
        System.out.println("head："+JSONObject.toJSONString(head));
        System.out.println("result："+JSONObject.toJSONString(result));
        return result;
    }
}
