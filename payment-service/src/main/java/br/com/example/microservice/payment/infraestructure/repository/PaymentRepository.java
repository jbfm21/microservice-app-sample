package br.com.example.microservice.payment.infraestructure.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.example.microservice.payment.domain.exception.BusinessException;
import br.com.example.microservice.payment.infraestructure.entity.PaymentEntity;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentEntity, UUID> 
{
	default PaymentEntity findByIdOrNotFoundException(UUID paymentId) 
	{
		return findById(paymentId).orElseThrow(() -> new BusinessException.PaymentNotFoundException(paymentId));
    }
	
}