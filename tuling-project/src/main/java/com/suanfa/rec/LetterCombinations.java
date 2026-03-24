package com.suanfa.rec;

/**
 * 26个字母a-z，找出组合a,b,ab，abc，a-z
 */
public class LetterCombinations {


    public static void main(String[] args) {
        generateCombinations("", 'a'); // 从空字符串开始，初始字母是 'a'
    }

    // 递归生成所有字母组合
    public static void generateCombinations(String prefix, char currentChar) {
        // 当当前字符超出 'z' 时结束递归
        if (currentChar > 'z') {
            return;
        }

        // 打印当前组合
        if (!prefix.isEmpty()) {
            System.out.println(prefix);
        }

        // 递归调用，加入当前字符并继续生成后续组合
        for (char c = currentChar; c <= 'z'; c++) {
            generateCombinations(prefix + c, (char) (c + 1));
        }
    }
}
