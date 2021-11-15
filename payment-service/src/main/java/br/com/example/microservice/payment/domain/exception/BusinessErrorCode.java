package br.com.example.microservice.payment.domain.exception;

public enum BusinessErrorCode 
{
    PAYMENT_NOT_FOUND(1),
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