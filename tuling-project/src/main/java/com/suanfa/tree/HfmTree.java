package com.suanfa.tree;

import com.alibaba.fastjson.JSON;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * 哈夫曼编码，思想是进行压缩，按照哈夫曼树建立密码规则，解压按照密码规则进行解压
 */
public class HfmTree {

    //根节点
    public HfmNode root;
    //编码对应权重，权重值表示一个字符对应的数字不能重复，用于加密
    public Map<String, Integer> weight;
    //所有的叶子节点
    public List<HfmNode> leafs;
    //key字符-value编码
    public Map<String, String> keyCharMap;
    //key编码-value字符
    public Map<String, String> keyCodeMap;

    public String code = "";

    public HfmTree(Map<String, Integer> weight) {
        this.weight = weight;
        leafs = new ArrayList<>();
    }

    /**
     * 创建哈夫曼树
     */
    public void createTree() {
        //优先队列，新增进去会自动排序（这个队列是一个完全二叉树，最小堆，权重低的在最上面）
        PriorityQueue<HfmNode> priorityQueue = new PriorityQueue();
        //组建叶子节点
        for (Map.Entry<String, Integer> map : weight.entrySet()) {
            HfmNode hfmNode = new HfmNode();
            hfmNode.fre = map.getValue();
            hfmNode.chars = map.getKey();
            leafs.add(hfmNode);
            priorityQueue.add(hfmNode);
            System.out.println(JSON.toJSON(priorityQueue));
        }

        //组建哈夫曼树，把叶子节点倒着组装成哈夫曼树，两两一组，所以从1开始，最后一个得到的是root不用组装所以-1
        for (int i = 1; i <= leafs.size() - 1; i++) {
            HfmNode hfmNodeMin = priorityQueue.poll();
            HfmNode hfmNodeMax = priorityQueue.poll();
            HfmNode hfmNode = new HfmNode();
            hfmNode.fre = hfmNodeMin.fre + hfmNodeMax.fre;
            hfmNode.chars = hfmNodeMin.chars + hfmNodeMax.chars;
            hfmNode.left = hfmNodeMin;
            hfmNode.right = hfmNodeMax;
            hfmNodeMin.parent = hfmNode;
            hfmNodeMax.parent = hfmNode;
            priorityQueue.add(hfmNode);
        }
        root = priorityQueue.poll();
    }

    /**
     * 获取编码表，遍历所有的叶子节点，因为叶子节点全部都是需要压缩的字符，从下往上寻找一直寻找到root节点
     * 当前是左节点表示编码是0，右节点表示编码是1
     *
     * @return
     */
    public Map<String, String> code() {
        keyCharMap = new HashMap<>();
        keyCodeMap = new HashMap<>();
        for (HfmNode leaf : leafs) {
            String code = "";
            HfmNode current = leaf;
            do {
                //左边等于0，右边等于1，注意：这里左小右大，但是他们都小于父节点，所以这里需要==去判断
                if (null != current.parent && current.fre == current.parent.left.fre) {
                    code = "0" + code;
                } else {
                    code = "1" + code;
                }
                current = current.parent;
            } while (null != current.parent);
            keyCharMap.put(leaf.chars, code);
            keyCodeMap.put(code, leaf.chars);
            this.code += code;
        }

        return keyCharMap;
    }

    /**
     * 编码
     *
     * @param s
     * @param keyCharMap
     */
    public String encode(String s, Map<String, String> keyCharMap) {
        char[] chars = s.toCharArray();
        String code = "";
        for (char aChar : chars) {
            String value = keyCharMap.get(String.valueOf(aChar));
            if (!StringUtils.isEmpty(value)) {
                code += value;
            }
        }
        return code;
    }

    /**
     * 解码
     *
     * @param code
     * @param keyCodeMap
     */
    public String decode(String code, Map<String, String> keyCodeMap) {
        char[] chars = code.toCharArray();
        String key = "";
        String valueAll = "";
        for (char aChar : chars) {
            key += aChar;
            String value = keyCodeMap.get(key);
            if (!StringUtils.isEmpty(value)) {
                valueAll += value;
                key = "";
            }
        }
        return valueAll;
    }

    public static void main(String[] args) {
        //// a:3 b:24 c:6 d:20 e:34 f:4 g:12
        String s = "abcdefg";
        Map<String, Integer> weight = new HashMap<>();
        weight.put("a", 3);
        weight.put("b", 24);
        weight.put("c", 6);
        weight.put("d", 20);
        weight.put("e", 34);
        weight.put("f", 4);
        weight.put("g", 12);
        HfmTree hfmTree = new HfmTree(weight);
        hfmTree.createTree();
        hfmTree.code();
        System.out.println("生成的哈夫曼编码表：" + hfmTree.keyCharMap);
        String encodeValue = hfmTree.encode(s, hfmTree.keyCharMap);
        System.out.println("生成的哈夫曼编码：" + encodeValue);
        String decodeValue = hfmTree.decode(encodeValue, hfmTree.keyCodeMap);
        System.out.println("解码：" + decodeValue);

    }
}

class HfmNode implements Comparable<HfmNode> {
    public String chars;
    //权重也就是出现频率的意思
    public int fre;
    public HfmNode left;
    public HfmNode right;
    public HfmNode parent;

    @Override
    public int compareTo(HfmNode o) {
        /**
         * 如果返回值为 负数（即 this.fre < o.fre），表示当前对象的频率小于传入对象的频率，排序时当前对象排在前面。
         * 如果返回值为 0（即 this.fre == o.fre），表示两个对象的频率相等。
         * 如果返回值为 正数（即 this.fre > o.fre），表示当前对象的频率大于传入对象的频率，排序时当前对象排在后面。
         */
        return this.fre - o.fre;
    }
}
