package com.suanfa.sort;

/**
 * 动态算法
 * <p>
 * 状态转移方程：Max(money[i]+res[i-1][w-weight[i]],res[i-1][w]);
 * 当前价格money[i]
 * res[i-1][w-weight[i]]当前剩余重量的价格=当前列重量-当前物品的重量
 * 同列的上一个价格res[i-1][w]
 */
public class ShoppingSort {

    public static void main(String[] args) {

//        packDpTest();
        cardTest();
    }

    /**
     * 使用动态规划实现，背包问题
     */
    private static void packDpTest() {
        //        int[] price = {6, 10, 12};//价格
//        int[] weight = {1, 2, 4};//重量kg
//        int p = 3;
//        int w = 5;//总共能装5kg

        int[] price = {19, 10, 12, 18, 6};//价格
        int[] weight = {5, 2, 4, 1, 4};//重量kg
        int p = 5;
        int w = 5;//总共能装5kg

        int[][] pw = new int[p + 1][w + 1];//这里加一是因为拆分的列和行都是从1开始
        for (int i = 1; i <= p; i++) {
            for (int j = 1; j <= w; j++) {
                //这里是如果拆分为位置的重量大于等于当前放入的物品
                //则需要把同一列上的上一个价格 与 当前价格+当前物品重量剩余空间能装下的价格相加进行比较(这里的价格取分割重量后的价格)，那个大则放入那个价格
                if (weight[i - 1] <= j) {
                    System.out.println(pw[i - 1][j] + "\t" + price[i - 1] + "\t" + pw[i - 1][j - weight[i - 1]]);
                    //pw[i - 1][j]同列的上一个价格(也就是表示不装当前物品的价格)
                    //当前价格price[i - 1]
                    // pw[i - 1][j - weight[i - 1]]当前剩余重量的价格=当前列所能装的重量-当前物品的重量
                    //当前剩余重量的价格对应上一行的列值(列值表示重量)
                    //这里的pw[i - 1][j - weight[i - 1]中的pw为什么要使用i-1去寻找剩余重量的价格，是因为当前物品已经装进去了，所以只能转以上的物品，所以需要-1
                    pw[i][j] = Math.max(pw[i - 1][j], price[i - 1] + pw[i - 1][j - weight[i - 1]]);
                } else {
                    pw[i][j] = pw[i - 1][j];
                }

            }
        }
        //最佳性价比物品的价格总和
        System.out.println("总价：" + pw[p][w]);
        System.out.println("重量：" + w);
        System.out.println("物品：" + p);
        //组合物品分别是哪些物品，这里需要倒着找，因为性价比最高的总价在最后，倒推过去得到有哪些物品的价格
//        for (int i = p; i >= 1; i--) {
//            //当前物品的重量要小于或者等于剩余重量，第一次进来是总重量
//            if (weight[i - 1] <= w) {
//                //如果当前重量的价格 小于或等于 当前价格+当前价格剩余重量价格，那说明这个物品就在总计中
//                if (pw[i - 1][w] <= price[i - 1] + pw[i - 1][w - weight[i - 1]]) {
//                    System.out.println(price[i - 1] + "\t" + weight[i - 1]);
//                    w -= weight[i - 1];
//                }
//            }
//        }

        System.out.println("=========第二种方式=======");
        for (int i = p; i >= 1; i--) {
            //如果当前物品的价格大于上一行物品的价格，说明这个物品在里面
            //也就是说当前价格 + 剩余重量的价格 > 上一个物品价格 + 剩余重量的价格，所以当前物品一定在其中
            if (pw[i][w] > pw[i - 1][w]) {
                System.out.println(price[i - 1]);
                w = w - weight[i - 1];
            }
        }
    }

    public static void cardTest() {
        int[] price = {2, 4, 3};
        //物品价格数量price的数量
        int p = 3;
        //最多能放入的价格
        int w = 5;
        int[][] pw = new int[p + 1][w + 1];
        for (int i = 1; i <= p; i++) {
            for (int j = 1; j <= w; j++) {
                if (price[i - 1] <= j) {
                    pw[i][j] = Math.max(pw[i - 1][j], price[i - 1] + pw[i - 1][j - price[i - 1]]);
                } else {
                    pw[i][j] = pw[i - 1][j];
                }
            }
        }
        System.out.println("总价：" + pw[p][w]);
//        for (int i = p; i >= 1; i--) {
//            if (price[i - 1] <= pw[i][w] && pw[i - 1][w] <= price[i - 1] + (w - price[i - 1])) {
//                System.out.println("价格：" + price[i - 1]);
//                w -= price[i - 1];
//            }
//        }

        System.out.println("=========第二种方式=======");
        for (int i = p; i >= 1; i--) {
            if (pw[i][w] > pw[i - 1][w]) {
                System.out.println("价格：" + price[i - 1]);
                w = w - price[i - 1];
            }
        }
    }

}
