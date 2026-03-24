//package com.jdbc;
//
//import java.io.ByteArrayInputStream;
//import java.io.InputStream;
//import java.sql.*;
//
//import org.springframework.jdbc.core.JdbcTemplate;
//
//import javax.sql.DataSource;
//
///**
// * LOAD DATA
// * @author seven
// * @since 07.03.2013
// */
//public class BulkLoadData2MySQL {
//
//    public static InputStream getTestDataInputStream() {
//        StringBuilder builder = new StringBuilder();
//        for (int i = 1; i <= 10; i++) {
//            for (int j = 0; j <= 10000; j++) {
//                builder.append(4);
//                builder.append("\t");
//                builder.append(4 + 1);
//                builder.append("\t");
//                builder.append(4 + 2);
//                builder.append("\t");
//                builder.append(4 + 3);
//                builder.append("\t");
//                builder.append(4 + 4);
//                builder.append("\t");
//                builder.append(4 + 5);
//                builder.append("\n");
//            }
//        }
//        byte[] bytes = builder.toString().getBytes();
//        InputStream is = new ByteArrayInputStream(bytes);
//        return is;
//    }
//
//    /**
//     * load bulk data from InputStream to MySQL
//     */
//    public int bulkLoadFromInputStream(String loadDataSql,
//                                       InputStream dataStream) throws SQLException {
//        if (dataStream == null) {
//            System.out.println("InputStream is null ,No data is imported");
//            return 0;
//        }
//        Connection conn = getConnection();
//        com.mysql.jdbc.PreparedStatement mysqlStatement = null;
//        try {
//            PreparedStatement statement = conn.prepareStatement(loadDataSql);
//
//            int result = 0;
//
//            if (statement.isWrapperFor(com.mysql.jdbc.Statement.class)) {
//                mysqlStatement = statement
//                        .unwrap(com.mysql.jdbc.PreparedStatement.class);
//
//                mysqlStatement.setLocalInfileInputStream(dataStream);
//                result = mysqlStatement.executeUpdate();
//            }
//            return result;
//        } catch (SQLException throwables) {
//            throwables.printStackTrace();
//            return 0;
//        } finally {
//            release(null, mysqlStatement, conn);
//        }
//    }
//
//    public static Connection getConnection() {
//        //Q1-想想为什么先定义?
//        Connection conn = null;
//        Statement st = null;
//        ResultSet rs = null;
//
//        try {
//            //1.加载数据库驱动
//            Class.forName("com.mysql.jdbc.Driver");
//
//            //2.获取与数据库的链接
//            //端口号:3306  数据库名称:jsp_database
//            String url = "jdbc:mysql://192.168.220.132:3306/tag_db_4?serverTimezone=UTC";//链接地址
//            String username = "root";//用户名
//            String password = "123456";//密码
//            conn = DriverManager.getConnection(url, username, password);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return conn;
//    }
//
//    public static void release(ResultSet rs, Statement st, Connection conn) {
//        //6.关闭链接，释放资源
//        if (rs != null) {
//            try {
//                rs.close();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            rs = null;
//        }
//        if (st != null) {
//            try {
//                st.close();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        if (conn != null) {
//            try {
//                conn.close();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    public static void main(String[] args) {
//        String testSql = "LOAD DATA LOCAL INFILE 'sql.csv' IGNORE INTO TABLE test (a,b,c,d,e,f)";
//        InputStream dataStream = getTestDataInputStream();
//        BulkLoadData2MySQL dao = new BulkLoadData2MySQL();
//        try {
//            long beginTime = System.currentTimeMillis();
//            int rows = dao.bulkLoadFromInputStream(testSql, dataStream);
//            long endTime = System.currentTimeMillis();
//            System.out.println("importing " + rows + " rows data into mysql and cost " + (endTime - beginTime) + " ms!");
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        System.exit(1);
//    }
//
//}