package fr.training.spring.shop.batch.order.processor;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.training.spring.shop.application.order.OrderDTO;
import fr.training.spring.shop.application.order.OrderManagement;

@Component
public class OrderProcessor implements ItemProcessor<String, OrderDTO> {

	@Autowired
	private OrderManagement orderManagement;

	@Override
	public OrderDTO process(String orderID) throws Exception {

		return orderManagement.findOne(orderID);
	}

}
