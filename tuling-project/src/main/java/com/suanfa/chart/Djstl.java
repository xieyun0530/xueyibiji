package com.suanfa.chart;

import java.util.*;

/**
 * 图结构的最短路径，有向图，思想是贪心加动态规划，
 * 假设：1-6的最短距离，
 * 建一个dis[]最短距离数组(下标从1开始)，先加入1，看1有多少条路，数组的下标就是表示1到那个点的距离是多少，只管直接距离，如果到不了就是无穷大；
 * 第一次，dis[]存储1这个点到其他数组路径的距离,下标：1:1->1 2:1->2 3:1-3 4:1-4 5:1:5 6:1-6，这个时候需要排除1，1已经走过了不能再走了排除，标记true
 * 第二次，找到上面排除之外的最短距离，那就是到3，然后加入3，找到3有几条路，找到1->3->4的距离小于dis数组第3个数组，则就进行替换，替换后标记true
 * 重复第二步操作，执行n步为止n，最后得到dis数组得到起始点到每个点的最短距离
 * 核心思想：排序得到最小值(需要排除已经加入的数字)，动态加入数字
 */
public class Djstl {

    public static void main(String[] args) {
        int n, m, x; // n表示点数，m表示边数，x表示我们要的起点
        Scanner cin = new Scanner(System.in);

        n = cin.nextInt();
        m = cin.nextInt();
        x = cin.nextInt();

        int value[][] = new int[n + 1][n + 1]; // 表示点到点的邻接矩阵
        int dis[] = new int[n + 1]; // 存最短路径的
        //找到邻接矩阵中
        for (int i = 1; i <= n; i++) {
            dis[i] = Integer.MAX_VALUE;
            for (int j = 1; j <= n; j++) {
                // 初始化我们的地图
                value[i][j] = -1; // 表示没有路的
                if (i == j) {
                    value[i][j] = 0;
                }
            }
        }

        //这里xx表示数字yy表示xx到yy的距离，v值存入value[xx][yy]中,
        for (int i = 0; i < m; i++) { // 表示要输入m个边
            int xx = cin.nextInt();
            int yy = cin.nextInt();
            int v = cin.nextInt(); // xx yy v表示从xx到yy有一条路 长度是v
            value[xx][yy] = v;
            // dis其实在第一个点时候可以在这里计算，找到dis数组里面离初始点最低的那个点
            if (xx == x) {
                dis[yy] = v;
            }
        }
        seach(x, dis, value, n);

    }

    public static void seach(int x, int dis[], int value[][], int n) {
        //已经动态加入过的数字
        boolean mark[] = new boolean[n + 1];
        mark[x] = true;
        dis[x] = 0;
        int count = 1;
        //记录最短路径走过的点
        Map<Integer, List<NodeDetail>> mapPath = new HashMap<>();
        //优先队列，存储动态规划拆分的时候，放入到某个点时到这个点的距离数据，不包括已经是最短距离的数据
        //min最小值的优化
        PriorityQueue<DisClass> priorityQueue = new PriorityQueue();
        for (int i = 1; i < dis.length; i++) {
            //起始点是0，自己到自己的距离是0
            if (i != x) {
                DisClass disClass = new DisClass(i, dis[i]);
                priorityQueue.add(disClass);
                //初始化起点边到i点的信息，边值和数字
                List<NodeDetail> list = new ArrayList<>();
                list.add(new NodeDetail(x,i,dis[i]));
                mapPath.put(i, list);
            }
        }

        while (count <= n) {    //O（n^2）
            int loc = 0; // 表示新加的点
//            int min = Integer.MAX_VALUE;
//            for (int i = 1; i <= n; i++) { // 求dis里面的最小值 优先队列,堆 logn
//            //!mark[i]表示没有加入过的数字
//                if (!mark[i] && dis[i] < min) {
//                    min = dis[i];
//                    loc = i;
//                }
//            }
            DisClass disClassMin = priorityQueue.poll();
            if (null == disClassMin) {
                break;
            }
            //没有替换成最小值
            if (!mark[disClassMin.i]) {
                loc = disClassMin.i;
            }
            if (loc == 0)
                break; // 表示没有可以加的点了
            mark[loc] = true;
            //只需要关注 我们加进来的点 能有哪些路径就可以了，不用扫描所有的dis 最好的情况应该可以达到o(nlogn),最坏的情况才是O(n^2)
            for (int i = 1; i <= n; i++) {
                //当前动态数组(去除)
                //1.dis[loc]起始点到loc(目前当前数字)的距离
                //2.value[loc][i]，loc(目前当前数字)到i数字的距离
                //3.dis[i]初始值到当前数字的距离
                //4.dis[loc]+value[loc][i]示例：(1->3->4)
                //5.(dis[loc] + value[loc][i] < dis[i])起始点到loc(目前当前数字)的距离+loc(目前当前数字)到i数字的距离 小于 dis数组初始值到当前数字的距离
                //6.value[loc][i] != -1 当前最小值到i数字有边可以走
                if (value[loc][i] != -1 && (dis[loc] + value[loc][i] < dis[i])) {
                    dis[i] = dis[loc] + value[loc][i];
                    // mapPath.get(i) = mapPath.get(loc) + NodeDetail(loc,i,value[loc][i])
                    // mapPath.get(i) 起始值到达i数字的路径信息
                    // mapPath.get(loc) 起始点到loc(目前当前数字)的距离的路径信息
                    //NodeDetail(loc,i,value[loc][i]) loc(目前当前数字)到i数字的路径信息
                    List<NodeDetail> locDetails = mapPath.get(loc);
                    List<NodeDetail> nodeDetails = mapPath.get(i);
                    nodeDetails.clear();
                    nodeDetails.addAll(locDetails);
                    nodeDetails.add(new NodeDetail(loc,i,value[loc][i]));
                }
            }
            count++;
        }
        for (int i = 1; i <= n; i++) {
            System.out.println(x + "到 " + i + "的最短路径为 ：" + dis[i]);
        }
        System.out.println("路径 ：" );
        for (Map.Entry<Integer, List<NodeDetail>> integerListEntry : mapPath.entrySet()) {
            System.out.println();
            System.out.println(x + "到 " + integerListEntry.getKey() + "的最短路径走过的点为 ：");
            for (NodeDetail nodeDetail : integerListEntry.getValue()) {
                System.out.println("起始值"+nodeDetail.start+"\t"+"边"+nodeDetail.edge+"结束值"+"\t"+nodeDetail.end);
            }
        }

    }

    static class DisClass implements Comparable<DisClass> {
        public int i;
        public int x;

        public DisClass(int i, int x) {
            this.i = i;
            this.x = x;
        }

        @Override
        public int compareTo(DisClass o) {
            return this.x - o.x;
        }
    }

    static class NodeDetail{
        //起始节点值
        public int start;
        //结束节点值
        public int end;
        //边值
        public int edge;

        public NodeDetail(int start, int end, int edge) {
            this.start = start;
            this.end = end;
            this.edge = edge;
        }
    }
}
/**
 * 6:点数，8:边数，1起点，下面1 6这种的是点与点之间的距离
  6
  8
  1
  1 6 100
  1 5 30
  1 3 10
  2 3 5
  3 4 50
  4 6 10
  5 4 20
  5 6 60
 */
