package br.com.example.microservice.order.domain.exception;

public enum BusinessErrorCode 
{
    ORDER_ITEM_ALREADY_EXISTS(1),
    ORDER_ALREADY_CONFIRMED(2),
    ORDER_UNCONFIRMED(3),
    ORDER_ITEM_NOT_FOUND(4),
    ORDER_NOT_FOUND(5),
    PRODUCT_NOT_FOUND(6),
    CARD_DETAIL_NOT_FOUND(7),    
    UNKNOWN(99999);
    private final int code;
	
    private BusinessErrorCode(int code)
    {
        this.code = code;
    }

    public int getCode()
    {
        return code;
    }
  
}