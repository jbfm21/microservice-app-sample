package br.com.example.microservice.order.infraestructure.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.example.microservice.order.domain.exception.BusinessException;
import br.com.example.microservice.order.infraestructure.entity.OrderEntity;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, UUID> 
{
	default OrderEntity findByIdOrNotFoundException(UUID orderId) 
	{
		return findById(orderId).orElseThrow(() -> new BusinessException.OrderNotFoundException(orderId));
    }
	
}