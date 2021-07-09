package com.shobhit.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.shobhit.entity.Order;
import com.shobhit.service.OrderService;

@RestController
@RequestMapping("/order")
public class OrderController {

	@Autowired
	public OrderService orderService;

	@RequestMapping (value = "/create", method = RequestMethod.POST)
	public String createOrder(@RequestBody Order order) {
		orderService.createOrder(order);

		return "Order Placed Successfully";
	}

	@RequestMapping (value = "/{id}", method = RequestMethod.GET)
	public Order getOrderById(@PathVariable int orderId) {
		return orderService.getOrderById(orderId);
	}

	@RequestMapping (value = "/all", method = RequestMethod.GET)
	public List<Order> getOrders() {
		return orderService.getOrders();
	}
}