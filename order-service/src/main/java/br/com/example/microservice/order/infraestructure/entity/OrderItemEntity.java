package br.com.example.microservice.order.infraestructure.entity;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name="orderItems")
@Builder @Data @NoArgsConstructor @AllArgsConstructor @ToString @EqualsAndHashCode
public class OrderItemEntity
{
	@Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "order_item_id", updatable = false, nullable = false, columnDefinition = "VARCHAR(36)")
    @Type(type = "uuid-char")    
    private String orderItemId;	
	
	@ManyToOne
	@JoinColumn(name="orderId")
    private OrderEntity order;
	

    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "product_id", updatable = false, nullable = false, columnDefinition = "VARCHAR(36)")
    @Type(type = "uuid-char")    
    private UUID productId;
    
    private Long quantity;
    
    public void decrement()
    {
    	if (quantity > 0)
    	{
    		quantity--;
    	}
    }

    public void increment()
    {
  		quantity++;
    }

}
