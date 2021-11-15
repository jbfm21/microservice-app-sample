package br.com.example.microservice.shipping.domain.exception;

public enum BusinessErrorCode 
{
    SHIPPING_NOT_FOUND(1),
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