package com.suanfa.stack;

import java.util.Scanner;

/**
 * @Description: 使用栈实现加减乘除计算，不包括带括号
 * @Author: xiewu
 * @Date: 2022/2/16
 * @Time: 13:06
 */
public class ArrayStack<Item> {

    private Item data[];
    public int top;

    public ArrayStack(int size) {
        data = (Item[]) new Object[size];
    }

    public void push(Item value) {
        if (top >= data.length - 1) {
            resise(data.length * 2);
        }
        data[top++] = value;
    }

    public Item pop() {
        if (isEmpty()) {
            return null;
        }
        Item value = data[--top];
        data[top] = null;
        int scale = 2;
        if (top > 0 & top <= (data.length / scale)) {
            resise(data.length / scale);
        }
        return value;
    }

    public void resise(int size) {
        Item dataRe[] = (Item[]) new Object[size];
        for (int i = 0; i < top; i++) {
            dataRe[i] = data[i];
        }
        data = dataRe;
    }

    public void print() {
        for (int i = 0; i < data.length; i++) {
            System.out.println(data[i]);
        }
    }

    public boolean isEmpty() {
        return top == 0;
    }

    public boolean isOk(String s) {
        ArrayStack<Character> arrayStack = new ArrayStack<>(20);
        char[] chars = s.toCharArray();
        Character pop;
        for (char aChar : chars) {
            switch (aChar) {
                case '{':
                case '[':
                case '(':
                    arrayStack.push(aChar);
                    break;
                case '}':
                    pop = arrayStack.pop();
                    if (pop == null) {
                        break;
                    } else if (pop == '{') {
                        return true;
                    }
                    break;
                case ']':
                    pop = arrayStack.pop();
                    if (pop == null) {
                        break;
                    } else if (pop == '[') {
                        return true;
                    }
                    break;
                case ')':
                    pop = arrayStack.pop();
                    if (pop == null) {
                        break;
                    } else if (pop == '(') {
                        return true;
                    }
                    break;
            }
        }
        return arrayStack.isEmpty();
    }

    public Integer compute(String computeValue) {
        ArrayStack<String> fuHaoStack = new ArrayStack<>(20);
        ArrayStack<Integer> shuZiStack = new ArrayStack<>(20);
        char[] array = computeValue.toCharArray();
        int startIndex = 0;
        for (int i = 0; i < array.length; i++) {
            String va = String.valueOf(array[i]);
            switch (va) {
                case "*":
                case "/":
                case "+":
                case "-":
                    String top = fuHaoStack.pop();
                    if (top != null) {
                        int levelTop = isLevel(top);
                        int levelCur = isLevel(va);
                        if (levelCur > levelTop) {
                            fuHaoStack.push(top);
                        } else {
                            Integer pop1 = shuZiStack.pop();
                            Integer pop2 = shuZiStack.pop();
                            Integer result = computeValue(pop1, pop2, top);
                            shuZiStack.push(result);
                        }
                    }
                    fuHaoStack.push(va);
                    break;
                default:
                    if (startIndex == 0 && i > 0 && i < array.length - 1 && !String.valueOf(array[i + 1]).matches("[\\+\\-\\*\\/]")) {
                        startIndex = i;
                        break;
                    }
                    if (i < array.length - 1 && !String.valueOf(array[i + 1]).matches("[\\+\\-\\*\\/]")) {
                        break;
                    }
                    //多位数
                    if (startIndex > 0) {
                        va = "";
                        for (int k = startIndex; k <= i; k++) {
                            va += String.valueOf(array[k]);
                        }
                    }
                    shuZiStack.push(Integer.valueOf(va));
                    startIndex = 0;
                    break;
            }
        }
        if (shuZiStack.top > 1) {

        }
        int stackValue = jisuanStack(fuHaoStack, shuZiStack);
        return stackValue;
    }

    public int jisuanStack(ArrayStack<String> fuHaoStack, ArrayStack<Integer> shuZiStack) {
        if (shuZiStack.top == 1) {
            return shuZiStack.pop();
        }
        Integer b = shuZiStack.pop();
        Integer a = shuZiStack.pop();
        int value = computeValue(a, b, fuHaoStack.pop());
        shuZiStack.push(value);
        return jisuanStack(fuHaoStack, shuZiStack);
    }

    public int isLevel(String aChar) {
        switch (aChar) {
            case "*":
            case "/":
                return 1;
            case "+":
            case "-":
                return 0;
            default:
                throw new RuntimeException("符号不存在！");
        }
    }

    public Integer computeValue(Integer a, Integer b, String fuHao) {
        switch (fuHao) {
            case "*":
                return a * b;
            case "/":
                return a / b;
            case "+":
                return a + b;
            case "-":
                return a - b;
            default:
                throw new RuntimeException("符号不存在！");
        }
    }

    public static void main(String[] args) {
        System.out.println(3 + 11 * 2 + 8 - 15 / 5);
        ArrayStack<Character> arrayStack = new ArrayStack<>(20);
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String s = scanner.next();
//            System.out.println("s的匹配结果:" + arrayStack.isOk(s));
            System.out.println("计算结果:" + arrayStack.compute(s));
        }
    }
}
