package com.suanfa.str;

/**
 * 字符串反转
 */
public class StringReverse {

    public static void main(String[] args) {
        System.out.println(reverse("123456789"));
    }

    public static String reverse(String str){
        char[] charArray = str.toCharArray();
        int i = 0;
        int j = charArray.length -1;
        while (i<j){
            char temp = charArray[i];
            charArray[i] = charArray[j];
            charArray[j] = temp;
            i++;
            j--;
        }
        return new String(charArray);
    }
}
