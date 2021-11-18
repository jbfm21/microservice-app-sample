package br.com.example.microservice.user.infraestructure.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.example.microservice.user.domain.CardDetails;
import br.com.example.microservice.user.domain.exception.BusinessExceptions.CardDetailNotFoundException;

@Repository
public interface CardDetailsRepository extends JpaRepository<CardDetails, UUID> 
{
	default CardDetails findByIdOrNotFoundException(UUID userId) 
	{
		return findById(userId).orElseThrow(CardDetailNotFoundException::new);
    }
	
}