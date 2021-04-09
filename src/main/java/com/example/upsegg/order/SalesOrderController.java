package com.example.upsegg.order;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.upsegg.order.entity.SalesOrder;
import com.example.upsegg.order.repository.SalesOrderRepository;

@RestController
public class SalesOrderController {
	private SalesOrderRepository salesOrderRepo;
	private SalesOrderService service;

	@Autowired
	public SalesOrderController(SalesOrderRepository purchaseRepo, SalesOrderService service) {
		this.salesOrderRepo = purchaseRepo;
		this.service = service;
	}

	// 주문 목록 조회
	@RequestMapping(value = "/sales-orders", method = RequestMethod.GET)
	public List<SalesOrder> getSalesOrders() {
		List<SalesOrder> list = salesOrderRepo.findAll();

		return list;
	}

	// 주문 1건 추가(임시)
	@RequestMapping(value = "/sales-orders", method = RequestMethod.POST)
	public SalesOrder addPurchaseOrders(@RequestBody SalesOrder salesOrder) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c1 = Calendar.getInstance();
		String Today = sdf.format(c1.getTime());
		salesOrder.setOrderDate(Today);

		salesOrder.setOrderState("00");
		salesOrderRepo.save(salesOrder);
		System.out.println(salesOrder);
		return salesOrder;
	}

	// 주문 상태 수정
	@RequestMapping(value = "/sales-orders/{id}", method = RequestMethod.PUT)
	public SalesOrder modiSalesOrders(@RequestParam("id") long id, HttpServletResponse res) {
		SalesOrder salesOrder = salesOrderRepo.findById(id).orElse(null);

		if (salesOrder == null) {
			res.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return null;
		}

		salesOrder.setId(id);

		salesOrderRepo.save(salesOrder);

		return salesOrder;
	}

	// 주문 선택 삭제
	@RequestMapping(value = "/sales-orders/{id}", method = RequestMethod.DELETE)
	public boolean removeSalesOrders(@PathVariable("id") long id, HttpServletResponse res) {
		SalesOrder salesOrder = salesOrderRepo.findById(id).orElse(null);

		if (salesOrder == null) {
			res.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return false;
		}

		salesOrderRepo.deleteById(id);

		return true;
	}
}
