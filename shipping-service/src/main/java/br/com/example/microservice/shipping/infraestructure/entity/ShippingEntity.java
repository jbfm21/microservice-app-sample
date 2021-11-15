package br.com.example.microservice.shipping.infraestructure.entity;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import br.com.example.microservice.shipping.domain.ShippingStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name="shippings")
@Builder @Data @NoArgsConstructor @AllArgsConstructor @ToString @EqualsAndHashCode
public class ShippingEntity 
{
    @Id
    @Column(name = "shipping_id", updatable = false, nullable = false, columnDefinition = "VARCHAR(36)")
    @Type(type = "uuid-char")    
    private UUID shippingId;
	
    @Column(name = "order_id", updatable = false, nullable = false, columnDefinition = "VARCHAR(36)")
    @Type(type = "uuid-char")    
    private UUID orderId;
    
    @Column(name = "payment_id", updatable = false, nullable = false, columnDefinition = "VARCHAR(36)")
    @Type(type = "uuid-char")    
    private UUID paymentId;
    
    @Enumerated(EnumType.STRING)
    private ShippingStatus shippingStatus;

    
}

