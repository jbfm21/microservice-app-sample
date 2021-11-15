package br.com.example.microservice.user.infraestructure.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.example.microservice.user.domain.User;
import br.com.example.microservice.user.domain.exception.BusinessExceptions;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> 
{
	default User findByIdOrNotFoundException(UUID userId) 
	{
		return findById(userId).orElseThrow(() -> new BusinessExceptions.UserNotFoundException());
    }
	
}