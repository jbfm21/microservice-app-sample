package br.com.example.microservice.payment.domain.query;

import java.io.Serializable;

import lombok.Data;
import lombok.NoArgsConstructor;

public enum Queries 
{;
	@Data @NoArgsConstructor
	public static class FindAllPaymentQuery implements Serializable
	{
		//Need to have some properties to avoid the exception org.axonframework.axonserver.connector.query.AxonServerQueryDispatchException: Unable to serialize object
		private static final long serialVersionUID = 307766558721563471L;
		private String queryName = "FindAllPaymentQuery";
	}
}