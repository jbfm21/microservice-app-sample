package br.com.example.microservice.order.domain.queries;

import java.util.List;
import java.util.UUID;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

import lombok.Data;
import lombok.NoArgsConstructor;


public enum OrderDTO 
{;
    private interface OrderId { @Positive UUID getOrderId(); }
    private interface OrderStatus { @NotBlank String getOrderStatus(); }
    private interface OrderItems{ @NotBlank List<OrderItemDTO> getOrderItems(); }

    public enum Response{;
    	
    	@Data @NoArgsConstructor  
    	public static class Public implements OrderId, OrderStatus, OrderItems {
            UUID orderId;
            String orderStatus;
            List<OrderItemDTO> orderItems;
        }
    }
}