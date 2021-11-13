package br.com.example.microservice.order.infraestructure.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

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
    private String id;
	
	@ManyToOne
	@JoinColumn(name="orderId")
    private OrderEntity order;
	
    private String productId;
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
