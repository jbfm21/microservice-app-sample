package br.com.example.microservice.order.infraestructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.example.microservice.order.domain.exceptions.OrderNotFoundException;
import br.com.example.microservice.order.infraestructure.entity.OrderEntity;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, String> 
{
	default OrderEntity findByIdOrNotFoundException(String orderId) 
	{
		return findById(orderId).orElseThrow(() -> new OrderNotFoundException(orderId));
    }
	
}