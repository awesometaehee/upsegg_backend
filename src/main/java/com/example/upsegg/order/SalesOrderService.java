package com.example.upsegg.order;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.upsegg.order.entity.SalesOrder;
import com.example.upsegg.order.repository.SalesOrderRepository;

@Service
public class SalesOrderService {

	private SalesOrderRepository orderRepo;

	@Autowired
	public SalesOrderService(SalesOrderRepository orderRepo) {
		this.orderRepo = orderRepo;
	}

	@RabbitListener(queues = "commerce.purchaseorder")
	public void receiveOrder(SalesOrder order) {
		System.out.println("--- SALESORDER LOG ---");
		System.out.println(order);

		SalesOrder salesOrder = SalesOrder.builder().name(order.getName()).address(order.getAddress())
				.tel(order.getTel()).orderDate(order.getOrderDate()).note(order.getNote()).pay(order.getPay())
				.salesOrderDetail(order.getSalesOrderDetail()).build();

		System.out.println(salesOrder);
		orderRepo.save(salesOrder);
	}
}
