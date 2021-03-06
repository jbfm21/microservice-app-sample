package br.com.example.microservice.payment.infraestructure.entity;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import br.com.example.microservice.payment.domain.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="payments")
@Builder @Data @NoArgsConstructor @AllArgsConstructor
public class PaymentEntity 
{
    @Id
    @Column(name = "payment_id", updatable = false, nullable = false, columnDefinition = "VARCHAR(36)")
    @Type(type = "uuid-char")    
    private UUID paymentId;

    @Column(name = "order_id", updatable = false, nullable = false, columnDefinition = "VARCHAR(36)")
    @Type(type = "uuid-char")    
    private UUID orderId;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

}

