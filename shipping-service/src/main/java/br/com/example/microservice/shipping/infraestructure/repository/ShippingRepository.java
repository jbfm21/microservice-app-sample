package br.com.example.microservice.shipping.infraestructure.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.example.microservice.shipping.domain.exception.BusinessException;
import br.com.example.microservice.shipping.infraestructure.entity.ShippingEntity;

@Repository
public interface ShippingRepository extends JpaRepository<ShippingEntity, UUID> 
{
	default ShippingEntity findByIdOrNotFoundException(UUID shippingId) 
	{
		return findById(shippingId).orElseThrow(() -> new BusinessException.ShippingNotFoundException(shippingId));
    }
	
}