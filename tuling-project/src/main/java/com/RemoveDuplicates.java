package com;

import java.util.Arrays;

public class RemoveDuplicates {
    public static void main(String[] args) {
        int[] nums = {1,2,2,3,3,4,5,5,6};
        int[] result = removeDuplicates(nums);
        System.out.println(Arrays.toString(result));
    }

    public static int[] removeDuplicates(int[] nums) {
        if (nums == null || nums.length == 0) {
            return new int[0];
        }

        int slow = 0;  // 慢指针，表示当前存放唯一元素的位置
        for (int fast = 0; fast < nums.length; fast++) {
            if ((fast==0||nums[fast]!=nums[fast-1]) && (fast==nums.length-1||nums[fast]!=nums[fast+1])) {  // 如果快指针指向的值与慢指针的值不同
                nums[slow] = nums[fast];    // 将快指针的值赋给慢指针
                slow++;
            }
        }

        // 返回去重后的新数组，长度为 slow + 1
        return Arrays.copyOf(nums, slow);
    }
}
