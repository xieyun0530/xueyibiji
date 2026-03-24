package com.suanfa.tree;

import java.util.Arrays;
import java.util.List;

/**
 * 二叉搜索树
 */
public class MyBinarySearchTree {

    public static void main(String[] args) {
        MyBinarySearchTree myBinarySearchTree = new MyBinarySearchTree();
        SearchNode node = new SearchNode(5);
//        List<Integer> list = Arrays.asList(3,6,8,0,4);
//        List<Integer> list = Arrays.asList(3,7,6,10,8);//删除7时后继节点没有子节点情况
        List<Integer> list = Arrays.asList(3, 7, 6, 10, 8, 9);//删除7时后继节点有右子节点情况
        for (Integer value : list) {
            myBinarySearchTree.insert(value, node);
        }
//        SeachNode select = select(4, node);
//        System.out.println(select.data);
        myBinarySearchTree.show(node);
        System.out.println("=============");
        SearchNode searchNode = myBinarySearchTree.delete(7, node);
        myBinarySearchTree.show(searchNode);
        System.out.println();
    }

    public void insert(int data, SearchNode node) {
        if (data < node.data) {
            if (null == node.left) {
                SearchNode left = new SearchNode(data);
                node.left = left;
                left.parent = node;
            } else {
                insert(data, node.left);
            }
        } else {
            if (null == node.right) {
                SearchNode right = new SearchNode(data);
                node.right = right;
                right.parent = node;
            } else {
                insert(data, node.right);
            }
        }
    }

    public SearchNode delete(int data, SearchNode node) {
        SearchNode deleteNode = select(data, node);
        if (null == deleteNode) {
            return node;
        }
        //情况1：删除节点是叶子节点
        if (null == deleteNode.left && null == deleteNode.right) {
            if (deleteNode == node) {
                node = null;
            } else if (deleteNode.data < deleteNode.parent.data) {
                deleteNode.parent.left = null;
            } else {
                deleteNode.parent.right = null;
            }
            return node;
        }
        //情况2：删除的节点只有一个叶子节点
        if (null != deleteNode.left && null == deleteNode.right) {
            //删除的是root节点
            if (deleteNode == node) {
                node = deleteNode.left;
                return node;
            }
            deleteNode.left.parent = deleteNode.parent;
            if (deleteNode.data > deleteNode.parent.data) {
                deleteNode.parent.right = deleteNode.left;
            } else {
                deleteNode.parent.left = deleteNode.left;
            }
            return node;
        } else if (null != deleteNode.right && null == deleteNode.left) {
            //删除的是root节点
            if (deleteNode == node) {
                node = deleteNode.right;
                return node;
            }
            //删除的右节点的父节点指向删除节点的父节点
            deleteNode.right.parent = deleteNode.parent;
            //删除节点大于父节点，则把删除的父节点放入到删除父节点的右边，否则放入左边
            if (deleteNode.data > deleteNode.parent.data) {
                deleteNode.parent.right = deleteNode.right;
            } else {
                deleteNode.parent.left = deleteNode.right;
            }
            return node;
        }

        //情况3：删除的节点有两个叶子节点
        SearchNode afterNode = findAfterNode(deleteNode);
        //把要删除节点的左节点，放入到后继节点的左边
        afterNode.left = deleteNode.left;
        deleteNode.left.parent = afterNode;
        //当前后继节点没有右节点的情况，相当于是上面的情况一
        //这里需要注意一个条件时后继节点的父级不等于删除节点，因为如果等于表明是删除的root节点
        //下面的afterNode后继节点的操作是，把后继节点afterNode得到一颗以后继节点为root的树
        //最后如果后继节点大于要删除节点的父级节点则放入到它的右边，否则放入到它的左边
        if (afterNode.right == null && afterNode.parent != deleteNode) {
            //后继节点在左边，把指向它的父级节点置空，防止死循环
            afterNode.parent.left = null;
            //这里因为后继节点是没有右节点的，所以这里的操作是把后继节点的父级节点放入到后继节点的右边
            //后继节点的右节点等于后继节点的父级节点
            afterNode.right = afterNode.parent;
            //后继节点的右节点的父级节点等于后继节点
            afterNode.right.parent = afterNode;
        } else if (afterNode.parent != deleteNode) {
            //当前后继节点有右节点的确情况，相当于上面的情况二
            //这里因为后继节点有右子节点，所以这里有两步骤
            //第一把后继节点的右节点放入到后继父节点的左边
            afterNode.right.parent = afterNode.parent;
            afterNode.parent.left = afterNode.right;
            //第二把删除节点的右边也就是后继父节点放入到后继节点的右边
            afterNode.right = deleteNode.right;
            deleteNode.right.parent = afterNode;
        }
        //如果删除的节点等于root节点
        if (deleteNode == node) {
            afterNode.parent = null;
            node = afterNode;
            return node;
        }

        //把要删除节点的父节点的指针指向后继节点的父节点
        afterNode.parent = deleteNode.parent;
        //如果删除节点大于父节点则表示删除节点在右边否则在左边，把后继节点指向删除节点的右边或者左边
        if (deleteNode.data > deleteNode.parent.data) {
            deleteNode.parent.right = afterNode;
        } else {
            deleteNode.parent.left = afterNode;
        }
        return node;
    }

    //寻找后继节点
    public SearchNode findAfterNode(SearchNode node) {
        //如果右边为空，表示没有后继节点，因为后继节点比当前节点大，那一定是在右边，但是一般不会为空，因为删除的节点是包含左右子节点的
        if (node.right == null) {
            return node;
        }
        //寻找当前节点的右边，这样达到第一个条件大于当前节点
        SearchNode cur = node.right;
        SearchNode pre = node.right;
        while (cur != null) {
            pre = cur;
            //循环寻找当前节点的右边节点的左节点，这说明满足第二个条件，最小值
            cur = cur.left;
        }
        return pre;
    }

    public SearchNode select(int data, SearchNode node) {
        if (node.data == data) {
            return node;
        } else if (data < node.data) {
            return select(data, node.left);
        } else {
            return select(data, node.right);
        }
    }

    public void print(SearchNode node) {
        if (null != node.left) {
            print(node.left);
        }
        System.out.print(node.data);
        if (null != node.right) {
            print(node.right);
        }
    }

    public void show(SearchNode root) {
        if (root == null) {
            System.out.println("EMPTY!");
            return;
        }
        // 得到树的深度
        int treeDepth = getTreeDepth(root);

        // 最后一行的宽度为2的（n - 1）次方乘3，再加1
        // 作为整个二维数组的宽度
        int arrayHeight = treeDepth * 2 - 1;
        int arrayWidth = (2 << (treeDepth - 2)) * 3 + 1;
        // 用一个字符串数组来存储每个位置应显示的元素
        String[][] res = new String[arrayHeight][arrayWidth];
        // 对数组进行初始化，默认为一个空格
        for (int i = 0; i < arrayHeight; i++) {
            for (int j = 0; j < arrayWidth; j++) {
                res[i][j] = " ";
            }
        }

        // 从根节点开始，递归处理整个树
        writeArray(root, 0, arrayWidth / 2, res, treeDepth);

        // 此时，已经将所有需要显示的元素储存到了二维数组中，将其拼接并打印即可
        for (String[] line : res) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < line.length; i++) {
                sb.append(line[i]);
                if (line[i].length() > 1 && i <= line.length - 1) {
                    i += line[i].length() > 4 ? 2 : line[i].length() - 1;
                }
            }
            System.out.println(sb.toString());
        }
    }

    // 用于获得树的层数
    public int getTreeDepth(SearchNode root) {
        return root == null ? 0 : (1 + Math.max(getTreeDepth(root.left), getTreeDepth(root.right)));
    }

    private void writeArray(SearchNode currNode, int rowIndex, int columnIndex, String[][] res, int treeDepth) {
        // 保证输入的树不为空
        if (currNode == null) {
            return;
        }
        // 先将当前节点保存到二维数组中
        res[rowIndex][columnIndex] = String.valueOf(currNode.data);

        // 计算当前位于树的第几层
        int currLevel = ((rowIndex + 1) / 2);
        // 若到了最后一层，则返回
        if (currLevel == treeDepth) {
            return;
        }
        // 计算当前行到下一行，每个元素之间的间隔（下一行的列索引与当前元素的列索引之间的间隔）
        int gap = treeDepth - currLevel - 1;

        // 对左儿子进行判断，若有左儿子，则记录相应的"/"与左儿子的值
        if (currNode.left != null) {
            res[rowIndex + 1][columnIndex - gap] = "/";
            writeArray(currNode.left, rowIndex + 2, columnIndex - gap * 2, res, treeDepth);
        }

        // 对右儿子进行判断，若有右儿子，则记录相应的"\"与右儿子的值
        if (currNode.right != null) {
            res[rowIndex + 1][columnIndex + gap] = "\\";
            writeArray(currNode.right, rowIndex + 2, columnIndex + gap * 2, res, treeDepth);
        }
    }

}

class SearchNode {

    public int data;
    public SearchNode left;
    public SearchNode right;
    public SearchNode parent;

    public SearchNode(int data) {
        this.data = data;
    }
}
