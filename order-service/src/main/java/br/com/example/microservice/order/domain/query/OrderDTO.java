package br.com.example.microservice.order.domain.query;

import java.util.List;
import java.util.UUID;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

import lombok.Data;
import lombok.NoArgsConstructor;


public enum OrderDTO 
{;
    private interface OrderId { @Positive UUID getOrderId(); }
    private interface UserId { @Positive UUID getUserId(); }
    private interface OrderStatus { @NotBlank String getOrderStatus(); }
    private interface OrderItems{ @NotBlank List<OrderItemDTO.Response.Public> getOrderItems(); }

    public enum Response{;
    	
    	@Data @NoArgsConstructor  
    	public static class Public implements OrderId, UserId, OrderStatus, OrderItems {
            UUID orderId;
            UUID userId;
            String orderStatus;
            List<OrderItemDTO.Response.Public> orderItems;
        }
    }
}