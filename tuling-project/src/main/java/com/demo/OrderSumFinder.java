package com.demo;

import java.util.*;

/**
 * @Description: 数据对比工具类
 * @Author: xiewu
 * @Date:   2024-10-19
 * @Version: V1.0
 */
public class OrderSumFinder {
    public static void main(String[] args) {
        // 订单数据 (order_id, total)
        Map<String, Double> orders = new LinkedHashMap<>();
        orders.put("1541414617388656721226", 49.0);
        orders.put("2531865317389521074756", 49.0);
        orders.put("1755166217388657632576", 49.0);
        orders.put("1289488217388657650158", 49.0);
        orders.put("2559945217388658036656", 19.0);
        orders.put("2564377817388660878408", 99.0);
        orders.put("2501225817388661552322", 49.0);
        orders.put("2249173417388665696748", 49.0);
        orders.put("2383092517388666479372", 19.0);
        orders.put("1755091617388666744223", 49.0);
        orders.put("2499931517388666840073", 49.0);
        orders.put("2689820117389547295591", 49.0);
        orders.put("2441853917389746399996", 59.0);
        orders.put("2692078217389769251967", 49.0);
        orders.put("2728296817389826461181", 169.0);
        orders.put("1912302117389845661136", 49.0);
        orders.put("2772438217389859300101", 99.0);
        orders.put("2772771717389890259088", 19.0);
        orders.put("2664092017389919480285", 19.0);
        orders.put("2601990817389950627176", 19.0);
        orders.put("2774250217390050887071", 19.0);
        orders.put("1366801817390126606724", 59.0);
        orders.put("2773849817390142518596", 19.0);
        orders.put("2497737317389318756564", 49.0);
        orders.put("2707359217390185969085", 19.0);
        orders.put("2499166817389326629673", 49.0);
        orders.put("2498938317389326922255", 49.0);

        double targetSum = 1176.0;
        List<String> selectedOrders = findOrdersWithTargetSum(orders, targetSum, 24);

        System.out.println("订单ID 组合，总和为 " + targetSum + "：");
        System.out.println(selectedOrders);

        // 找到不在选中组合中的 3 个订单
        Set<String> remainingOrders = new HashSet<>(orders.keySet());
        remainingOrders.removeAll(selectedOrders);
        System.out.println("不在组合中的 3 个订单ID：");
        System.out.println(new ArrayList<>(remainingOrders));
    }

    public static List<String> findOrdersWithTargetSum(Map<String, Double> orders, double targetSum, int count) {
        List<Map.Entry<String, Double>> orderList = new ArrayList<>(orders.entrySet());
        List<String> result = new ArrayList<>();

        if (findCombination(orderList, targetSum, 0, new ArrayList<>(), result, count)) {
            return result;
        }
        return Collections.emptyList();
    }

    private static boolean findCombination(List<Map.Entry<String, Double>> orders, double targetSum, int start,
                                           List<String> current, List<String> result, int count) {
        if (current.size() == count && targetSum == 0) {
            result.addAll(current);
            return true;
        }
        if (targetSum < 0 || current.size() >= count) {
            return false;
        }

        for (int i = start; i < orders.size(); i++) {
            Map.Entry<String, Double> entry = orders.get(i);
            current.add(entry.getKey());
            if (findCombination(orders, targetSum - entry.getValue(), i + 1, current, result, count)) {
                return true;
            }
            current.remove(current.size() - 1);
        }
        return false;
    }
}
