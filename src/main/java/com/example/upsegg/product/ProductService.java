package com.example.upsegg.product;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.example.upsegg.product.entity.Product;
import com.google.gson.Gson;

@Service
public class ProductService {
	private RabbitTemplate rabbit;
	private KafkaTemplate<String, String> kafka;

	@Autowired
	public ProductService(RabbitTemplate rabbit, KafkaTemplate<String, String> kafka) {
		this.rabbit = rabbit;
		this.kafka = kafka;
	}

	public void sendProduct(Product product) {
		System.out.println("-- PRODUCT LOG --");
		System.err.println(product);

		// rabbit.convertAndSend("commerce.product", product);
		// kafka.send("commerce.product", new Gson().toJson(product));
	}
}
