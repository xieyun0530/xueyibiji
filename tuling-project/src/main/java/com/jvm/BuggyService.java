//package com.jvm;
//
//import java.io.BufferedReader;
//import java.io.File;
//import java.io.FileReader;
//import java.io.IOException;
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.sql.Statement;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//import java.util.concurrent.TimeUnit;
////import lombok.Getter;
////import org.springframework.transaction.annotation.Transactional;
////import org.springframework.web.client.RestTemplate;
//
///**
// * 远程面试题，找代码的问题点
// */
//public class BuggyService {
//
//    private static BuggyService instance = null; //需要加volatile
//    private ExecutorService pool = Executors.newFixedThreadPool(4);
//    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-mm-dd");
//
////    private final RestTemplate restTemplate = new RestTemplate();
//
//    private BuggyService() {
//    }
//
//    public static BuggyService getInstance() {
//        if (instance == null) {
//            synchronized (BuggyService.class) {
//                if (instance == null) {
//                    instance = new BuggyService();
//                }
//            }
//        }
//        return instance;
//    }
//
//    private int counter = 0;
//
//    public void submitTasks(List<String> items) {
//        for (String it : items) {
//            pool.submit(() -> {
//                try {
//                    Date d = SDF.parse(it); //SimpleDateFormat这个是线程不安全的应该使用LocalDate
//                    System.out.println("Processing: " + d);
//                } catch (ParseException e) {
//                }
//                counter = counter + 1; //线程不安全
//            });
//        }
//    }
//
//    public List<String> readLines(String path) throws IOException {
//        List<String> lines = new ArrayList<>();
//        File f = new File(path);
//        BufferedReader br = new BufferedReader(new FileReader(f)); //流未关闭
//        String l;
//        while ((l = br.readLine()) != null) {
//            lines.add(l);
//        }
//        return lines;
//    }
//
//    /**
//     * 查找数组中的最大值的索引
//     *
//     * @param arr
//     * @return
//     */
//    public int indexOfMax(int[] arr) {
//        if (arr == null || arr.length == 0) {
//            return -1;
//        }
//        int idx = 0;
//        for (int i = 0; i < arr.length - 1; i++) {
//            if (arr[i] < arr[i + 1]) {
//                idx = i + 1;
//            }
//        }
//        return idx;
//    }
//
//    private Connection getConnection() throws SQLException {
//        return DriverManager.getConnection("jdbc:h2:mem:testdb");
//    }
//
////    @Transactional
//    public void insertUser(String name) throws SQLException {
//        Connection conn = getConnection();
//        Statement stmt = conn.createStatement();
//        String sql = "INSERT INTO users (name) VALUES ('" + name + "')";
//        stmt.executeUpdate(sql);
//    }
//
//    public List<Map<String, Object>> queryUsers(String nameFilter) throws SQLException {
//        Connection conn = getConnection();
//        Statement stmt = conn.createStatement();
//        String sql = "SELECT id, name FROM users WHERE name LIKE '%" + nameFilter + "%'";
//        ResultSet rs = stmt.executeQuery(sql);
//        List<Map<String, Object>> out = new ArrayList<>();
//        while (rs.next()) {
//            Map<String, Object> row = new HashMap<>();
//            row.put("id", rs.getInt("id"));
//            row.put("name", rs.getString("name"));
//            out.add(row);
//        }
//        if (out == null) {
//            insertUser(nameFilter);
//        }
//        return out;
//    }
//
////    public String fetchData() throws SQLException {
////        for (int i = 0; i < 100; i++) {
////            String str = restTemplate.getForObject("https://api.example.com/data/" + i, String.class);
////            insertUser(str);
////        }
////        return "done";
////    }
//
//    static class User {
//
//        String name;
//        int id;
//
//        User(String name, int id) {
//            this.name = name;
//            this.id = id;
//        }
//
//        @Override
//        public boolean equals(Object o) {
//            if (this == o) {
//                return true;
//            }
//            if (!(o instanceof User p)) {
//                return false;
//            }
//            return this.id == p.id;
//        }
//
//        @Override
//        public int hashCode() {
//            return name != null ? name.hashCode() : 0;
//        }
//    }
//
//    public void shutdownNow() {
//        pool.shutdown();
//        try {
//            pool.awaitTermination(1, TimeUnit.SECONDS);
//        } catch (InterruptedException e) {
//        }
//    }
//
//    public void doWorkSafely() {
//        try {
//            List<String> lines = readLines("nonexistent.txt");
//            System.out.println(lines.size());
//        } catch (Exception e) {
//
//        }
//    }
//
////    @Getter
//    private final List<String> items = Arrays.asList("a", "b", "c");
//
//    public List<String> getItems() {
//        return items;
//    }
//
//    // small main to run
//    public static void main(String[] args) throws Exception {
//        BuggyService s = BuggyService.getInstance();
//        s.submitTasks(Arrays.asList("2023-01-01", "2023-02-02", "bad-date"));
//        System.out.println("Index of max: " + s.indexOfMax(new int[]{10, 2, 5, 1}));//这里等于2
//        Set<User> set = new HashSet<>();
//        User p1 = new User("Alice", 1);
//        User p2 = new User("Bob", 1);
//        set.add(p1);
//        System.out.println("Contains p2? " + set.contains(p2));
//        set.add(p2);
//        System.out.println("Contains p2? " + set.contains(p2));
//
//        List<String> ext = s.getItems();
//        ext.get(0).replace("a", "d");
//        System.out.println("Items now: " + s.getItems());//[a, b, c] String 是不可变对象，这里是创建了一个新的字符串 "d"，但你没有把这个新字符串重新放回列表
//        ext.add("x"); //报错新增不了，因为返回的实际上是 java.util.Arrays$ArrayList（不是 java.util.ArrayList）
//        // 返回的实际上是 java.util.Arrays$ArrayList（不是 java.util.ArrayList）这个类的底层实现只是一个定长数组的视图，
//        // 它内部直接引用了传入的数组 a[]，允许修改已有元素（set），不允许增删元素（add / remove）
//        System.out.println("Items now: " + s.getItems());
//    }
//}
