package com.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class JDBCdemo {

    public static void main(String[] args) {

        ExecutorService executorService = Executors.newFixedThreadPool(10);

        StringBuffer tagValue = new StringBuffer();
        for (int i = 1; i <= 2104; i++) {
            if (i == 2104){
                tagValue.append("1234");
            }else {
                tagValue.append("1234,");
            }
        }

        for (int i = 0; i <= 1000000; i++) {
            List<TagStock> list = new ArrayList<>();
            int max = i+100;
            for (int j = i; j < max; j++) {
//                TagStock tagStock = getTagStock1("123", j);
                TagStock tagStock = getTagStock2(tagValue, j);
                list.add(tagStock);
            }
            i = max;

            executorService.execute(new Runnable() {
                @Override
                public void run() {
//                    insert1(list);
                    insert2(list);
                }
            });
        }
//        Map<String, List<TagStock>> collect = list.stream().collect(Collectors.groupingBy(a -> a.getPid()));
//        for (Map.Entry<String, List<TagStock>> stringListEntry : collect.entrySet()) {
//            if (stringListEntry.getValue().size() > 1) {
//                System.out.println(stringListEntry.getValue().get(0).getId() + "\t" + stringListEntry.getKey());
//                System.out.println(stringListEntry.getValue().get(1).getId() + "\t" + stringListEntry.getKey());
//            }
//        }

    }

    private static TagStock getTagStock1(String tagValue, int j) {
        String number = String.valueOf(j);
        String pid = "qwertyudb2530ab0f01fe9851c8ed0f4a92f9d7c0b05828941626d25f6caa0d9".substring(number.length());
        pid = number + pid;
        TagStock tagStock = new TagStock(pid, tagValue);
        tagStock.setTagKeyId(j);
        return tagStock;
    }

    private static TagStock getTagStock2(StringBuffer tagValue, int j) {
        String number = String.valueOf(j);
        String pid = "qwertyudb2530ab0f01fe9851c8ed0f4a92f9d7c0b05828941626d25f6caa0d9".substring(number.length());
        pid = number + pid;
        TagStock tagStock = new TagStock(pid, tagValue.toString());
        return tagStock;
    }

    public static Connection getConnection() {
        //Q1-想想为什么先定义?
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;

        try {
            //1.加载数据库驱动
            Class.forName("com.mysql.jdbc.Driver");

            //2.获取与数据库的链接
            //端口号:3306  数据库名称:jsp_database
            String url = "jdbc:mysql://192.168.220.132:3306/tag_db_4?serverTimezone=UTC";//链接地址
            String username = "root";//用户名
            String password = "123456";//密码
            conn = DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }

    public static void release(ResultSet rs, Statement st, Connection conn) {
        //6.关闭链接，释放资源
        if (rs != null) {
            try {
                rs.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            rs = null;
        }
        if (st != null) {
            try {
                st.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (conn != null) {
            try {
                conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean insert1(List<TagStock> list) {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            //获取数据的连接
            conn = getConnection();
            //获取Statement对象
            stmt = conn.createStatement();

            StringBuilder sqlStringBuilder = new StringBuilder("insert into tag_stock_1(pid,tag_key_id,tag_value) values ");
            for (int i = 0; i < list.size(); i++) {
                TagStock tagStock = list.get(i);
                if (i == list.size() - 1) {
                    sqlStringBuilder.append("('"
                            + tagStock.getPid() + "','"
                            + tagStock.getTagKeyId() + "','"
                            + tagStock.getTagValue() + "')");
                } else {
                    sqlStringBuilder.append("('"
                            + tagStock.getPid() + "','"
                            + tagStock.getTagKeyId() + "','"
                            + tagStock.getTagValue() + "'),");
                }
            }
            int num = stmt.executeUpdate(sqlStringBuilder.toString());
            if (num > 0) {
                return true;
            }
            return false;

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            release(rs, stmt, conn);
        }
        return false;

    }

    public static boolean insert2(List<TagStock> list) {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            //获取数据的连接
            conn = getConnection();
            //获取Statement对象
            stmt = conn.createStatement();

            StringBuilder sqlStringBuilder = new StringBuilder("insert into tag_stock_2_2104(pid,tag_value) values ");
            for (int i = 0; i < list.size(); i++) {
                TagStock tagStock = list.get(i);
                if (i == list.size() - 1) {
                    sqlStringBuilder.append("('"
                            + tagStock.getPid() + "','"
                            + tagStock.getTagValue() + "')");
                } else {
                    sqlStringBuilder.append("('"
                            + tagStock.getPid() + "','"
                            + tagStock.getTagValue() + "'),");
                }
            }
            int num = stmt.executeUpdate(sqlStringBuilder.toString());
            if (num > 0) {
                return true;
            }
            return false;

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            release(rs, stmt, conn);
        }
        return false;

    }
}

