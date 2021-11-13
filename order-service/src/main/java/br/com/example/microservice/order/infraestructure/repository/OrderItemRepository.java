package br.com.example.microservice.order.infraestructure.repository;

import java.util.UUID;

import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.example.microservice.order.domain.exceptions.BusinessException;
import br.com.example.microservice.order.infraestructure.entity.OrderEntity;
import br.com.example.microservice.order.infraestructure.entity.OrderItemEntity;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItemEntity, UUID> {
	
	default OrderItemEntity findByOrderIdAndProductIdOrNotFoundException(UUID orderId, UUID productId) 
	{
        OrderEntity order = OrderEntity.builder().orderId(orderId).build(); 
		
		Example<OrderItemEntity> example = Example.of(OrderItemEntity.builder().order(order).productId(productId).build());
        return findOne(example).orElseThrow(()->new BusinessException.OrderItemNotFoundException(orderId, productId));
    }
}