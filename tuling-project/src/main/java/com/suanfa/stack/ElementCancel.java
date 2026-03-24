package com.suanfa.stack;

import java.util.Arrays;
import java.util.List;
import java.util.Stack;

/**
 * a,b相反可以抵消，c,d相反可以抵消，给定一个数组a,b,c,d,a,a,预期结果是a,a使用代码实现；
 */
public class ElementCancel {

    public static void main(String[] args) {
        List<String> list = Arrays.asList("a", "b", "c", "d", "a", "a");
        Stack<String> stack = new Stack<>();
        for (String value : list) {
            if (!stack.isEmpty() && compareString(stack.peek(), value)) {
                stack.pop();
            }else {
                stack.push(value);
            }
        }
        System.out.println(stack);
    }


    public static boolean compareString(String a, String b){
        return (a.equals("a") && b.equals("b")) || (a.equals("c") && b.equals("d"));
    }
}
