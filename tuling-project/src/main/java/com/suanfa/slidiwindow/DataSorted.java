package com.suanfa.slidiwindow;

import java.util.*;

/**
 * 滑动窗口
 */
public class DataSorted {
    static double u = 0.5;
    public static void main(String[] args) {
//        List<Item> ls = new ArrayList<>();
//        ls.add(new Item("1","A",11.0));
//        ls.add(new Item("2","A",10.0));
//        ls.add(new Item("3","B",10.1));
//        ls.add(new Item("4","A",4.0));
//        ls.add(new Item("4","C",9.0));
//        ls.add(new Item("4","C",11.0));
//        ls.add(new Item("4","B",11.0));
//        Collections.sort(ls, new Comparator<Item>() {
//            @Override
//            public int compare(Item o1, Item o2) {
//                Double diff = o1.getScore() - o2.getScore();
//                if(diff>0){
//                    return -1;
//                }else{
//                    return 1;
//                }
//            }
//        });
//        System.out.println(ls);
//        System.out.println(scoreScatter(ls));
//        System.out.println(bucketScatter(ls));
//        System.out.println(windowsScatter(ls, 2));

        List<Item> ls = new ArrayList<>();
        ls.add(new Item("1","A",11.0));
        ls.add(new Item("2","A",10.0));
        ls.add(new Item("3","A",10.1));
        ls.add(new Item("4","A",4.0));
        ls.add(new Item("5","C",9.0));
        ls.add(new Item("6","C",11.0));
        ls.add(new Item("7","B",11.0));
        ls.add(new Item("8","B",11.0));
        ls.add(new Item("9","B",11.0));

        System.out.println(windowsScatterAll(ls, 3));

        List<Integer> array = Arrays.asList(1, 2, 1, 4, 4, 6, 2, 3, 5, 5, 5);
        int lastIndex = findLastConsecutiveDuplicateIndex(array);
        if (lastIndex != -1) {
            System.out.println("数组末尾元素连续重复的下标为：" + lastIndex);
            array = array.subList(0, lastIndex + 1);
            System.out.println("去除连续重复的数字：" + array);
        } else {
            System.out.println("数组末尾没有连续重复的元素。");
        }
    }

    public static int findLastConsecutiveDuplicateIndex(List<Integer> array) {
        int n = array.size();
        int i = n - 1; // 从数组末尾开始遍历
        boolean flag = true;
        while (i > 0) {
            if (array.get(i) == array.get(i - 1)) {
                // 找到连续重复的元素
                flag = false;
            }else if (array.get(i) != array.get(i - 1) && !flag) {
                // 连续重复的元素已经结束
                return i;
            } else if (flag) {
                //末尾没有连续重复的元素
                return -1;
            }

            i--;

        }
        //末尾找到连续重复的元素
        return -1;
    }

    /**
     * 通过设置滑动窗口，对窗口内元素一定程度打散，按窗口排列成不通类型的数据，这里的的元素位置不算交换，直接插入到比较元素前面，且所有元素往后移动一位
     * @author guoyanchao@eqxiu.com
     * @param numbers
     * @param length
     * @return
     */
    public static List<Item> windowsScatterAll(List<Item> numbers, Integer length){
        if(length == null || length > groupByType(numbers).size()){
            length = groupByType(numbers).size();
        }
        int size = numbers.size();
        for(int i=0; i<size; i++){
            //窗口大小
            List<String> keys = new ArrayList<>();
            //当前要比较的元素
            int itemIndex = i +1;
            keys.add(numbers.get(i).getType());
            for(int m=itemIndex; m<size; m++){
                Item itemParam = numbers.get(m);
                //如果它的下一个和它不相同则直接加入
                if (!keys.contains(itemParam.getType()) && m == i+1) {
                    keys.add(itemParam.getType());
                    if (m == size-1) {
                        break;
                    }
                    itemIndex ++;
                }else if(!keys.contains(itemParam.getType()) && !numbers.get(itemIndex).getType().equals(itemParam.getType())){
                    //删除当前与其比较的元素，因为当前元素需要放入到比较的元素前面
                    numbers.remove(m);
                    //放入到当前比较的元素的前面
                    ListIterator<Item> listIterator = numbers.listIterator(itemIndex);
                    //将当前元素放入到比较元素的前面
                    listIterator.add(itemParam);


                    //不是下一个元素，且寻找到的元素和当前元素不相同，则交换
//                    numbers.set(itemIndex, itemParam);
//                    numbers.set(m, numbers.get(itemIndex));
                    keys.add(itemParam.getType());
                    if (m == numbers.size()-1) {
                        break;
                    }
                    itemIndex ++;
                }
                //如果窗口大小等于length，则跳出循环
                if (keys.size() == length) {
                    break;
                }
            }
        }
        return numbers;
    }

    /**
     * 通过设置滑动窗口，对窗口内元素一定程度打散
     * @author guoyanchao@eqxiu.com
     * @param numbers
     * @param length
     * @return
     */
    public static List<Item> windowsScatter(List<Item> numbers, Integer length){
//        List<Item> ls = new ArrayList<>();
        if(length == null || length > groupByType(numbers).size()){
            length = groupByType(numbers).size();
        }
        for(int i=0; i<numbers.size()-length; i++){
            List<Item> subls = numbers.subList(i, i+length);
            List<String> keys = new ArrayList<>();
            int j = length+i;
            for(int m=0; m<length; m++){
                Item item = subls.get(m);
                if(keys.contains(item.getType())){
                    numbers.set(i+m, numbers.get(j));
                    numbers.set(j, item);
                    keys.add(item.getType());
                    j+=1;
                }else{
                    keys.add(item.getType());
                }
            }
        }
        return numbers;
    }
    /**
     * 通过分桶对全局数据打散
     * @author guoyanchao@eqxiu.com
     * @param numbers
     * @return
     */
    public static List<Item> bucketScatter(List<Item> numbers){
        Map<String, List<Item>> map = groupByType(numbers);
        List<Item> ls = new ArrayList<>();
        int maxSize = 0;
        for(String key : map.keySet()) {
            if(map.get(key).size()>maxSize){
                maxSize = map.get(key).size();
            }
        }
        for(int i=0; i<maxSize; i++){
            List<Item> tmp = new ArrayList<>();
            for(String k: map.keySet()){
                List<Item> gls = map.get(k);
                if(i<gls.size()){
                    tmp.add(gls.get(i));
                }
            }
            Collections.sort(tmp, new Comparator<Item>() {
                @Override
                public int compare(Item o1, Item o2) {
                    Double diff = o1.getScore() - o2.getScore();
                    if(diff>0){
                        return -1;
                    }else{
                        return 1;
                    }
                }
            });
            ls.addAll(tmp);
        }
        return ls;
    }
    /**
     * 通过重新计算得分，使用新的排名进行打散
     * @author guoyanchao@eqxiu.com
     * @param numbers
     * @return
     */
    public static List<Item> scoreScatter(List<Item> numbers) {
        List<Item> ls = new ArrayList<>();
        Map<String, List<Item>> map = groupByType(numbers);
//        System.out.println(map);
        for(String key : map.keySet()) {
            numbers = map.get(key);
            numbers = cumulativeSum(numbers);
            for (int i = 0; i < numbers.size(); i++) {
                Item item = numbers.get(i);
                if (i < numbers.size() - 1) {
                    item.setNewScore(Math.pow(Math.pow(item.getNewScore(), 1 / u) - Math.pow(numbers.get(i + 1).getNewScore(), 1 / u), u));
                } else {
                    item.setNewScore(Math.pow(Math.pow(item.getNewScore(), 1 / u), u));
                }
            }
            ls.addAll(numbers);
        }
        Collections.sort(ls, new Comparator<Item>() {
            @Override
            public int compare(Item o1, Item o2) {
                Double diff = o1.getNewScore() - o2.getNewScore();
                if(diff>0){
                    return -1;
                }else{
                    return 1;
                }
            }
        });
        return ls;
    }

    public static Map<String, List<Item>> groupByType(List<Item> numbers){
        Map<String, List<Item>> map = new HashMap<>();

        for(Item item : numbers){
            if(map.containsKey(item.getType())){
                map.get(item.getType()).add(item);
            }else{
                List<Item> ls = new ArrayList<>();
                ls.add(item);
                map.put(item.getType(), ls);
            }
        }
        return map;
    }

    private static List<Item> cumulativeSum(List<Item> numbers) {

        double sum = 0;
        for (int i = numbers.size()-1; i >= 0; i--) {
            Item item = numbers.get(i);
            sum += item.getScore(); // find sum
            item.setNewScore(sum);
//            numbers.set(i, item); // replace
        }

        return numbers;
    }
    static class Item{
        String pid = null;
        String type = null;
        Double score = 0.0;
        Double newScore = null;
        public Item(String pid, String type, Double score) {
            this.pid = pid;
            this.score = score;
            this.type = type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public void setScore(Double score) {
            this.score = score;
        }

        public void setNewScore(Double newScore) {
            this.newScore = newScore;
        }

        public String getType() {
            return type;
        }

        public Double getScore() {
            return score;
        }

        public Double getNewScore() {
            return newScore;
        }

        public void setPid(String pid) {
            this.pid = pid;
        }

        public String getPid() {
            return pid;
        }

        @Override
        public String toString() {
            return "Item{" +
                    "pid='" + pid + '\'' +
                    ", type='" + type + '\'' +
                    ", score=" + score +
                    ", newScore=" + newScore +
                    '}';
        }
    }
}