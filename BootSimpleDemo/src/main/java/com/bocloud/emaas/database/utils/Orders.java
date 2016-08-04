package com.bocloud.emaas.database.utils;

import java.util.HashMap;
import java.util.Map;

import com.bocloud.emaas.database.core.meta.Order;

public class Orders {

	public static Map<String, Order> simpleOrder(String column, Order order) {
		Map<String, Order> orders = new HashMap<String, Order>();
		orders.put(column, order);
		return orders;
	}

	public static Map<String, Order> simpleCreateOrder(Order order) {
		Map<String, Order> orders = new HashMap<String, Order>();
		orders.put("gmt_create", order);
		return orders;
	}
}
