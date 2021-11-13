package br.com.example.microservice.order.infraestructure.repository;

import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.example.microservice.order.domain.exceptions.OrderItemNotFoundException;
import br.com.example.microservice.order.infraestructure.entity.OrderEntity;
import br.com.example.microservice.order.infraestructure.entity.OrderItemEntity;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItemEntity, String> {
	
	default OrderItemEntity findByOrderIdAndProductIdOrNotFoundException(String orderId, String productId) 
	{
        OrderEntity order = OrderEntity.builder().orderId(orderId).build(); 
		
		Example<OrderItemEntity> example = Example.of(OrderItemEntity.builder().order(order).productId(productId).build());
        return findOne(example).orElseThrow(()->new OrderItemNotFoundException(orderId, productId));
    }
}