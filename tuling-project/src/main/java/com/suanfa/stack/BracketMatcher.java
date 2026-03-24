package com.suanfa.stack;

import java.util.Stack;

/**
 * 手写括号匹配算法，使用栈结构解决
 */
public class BracketMatcher {

    public static boolean isValid(String s){
        Stack<Character> stack = new Stack<>();
        char[] charArray = s.toCharArray();
        for (char c : charArray) {
            if (c == '(' || c == '{' || c == '[') {
                stack.push(c);
            }else if (c == ')' || c == '}' || c == ']'){
                if (stack.isEmpty() || !matcher(stack.pop(), c)) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean matcher(char left, char right){
        return (left == '(' && right == ')')
                || (left == '{' && right == '}')
                || (left == '[' && right == ']');
    }

    public static void main(String[] args) {
        String s = "([{}])";
        System.out.println(isValid(s));
    }
}
