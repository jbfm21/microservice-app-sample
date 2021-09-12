package br.com.example.microservice.productreview;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import br.com.example.microservice.productreview.infraestructure.security.JwtTokenService;
import feign.RequestInterceptor;
import feign.RequestTemplate;

@SpringBootApplication
@ComponentScan
@EnableEurekaClient
@EnableOAuth2Client
@EnableFeignClients
public class ProductReviewApplication {

    @Autowired
    private JwtTokenService jwtTokenService;
	
	public static void main(String[] args) {
		SpringApplication.run(ProductReviewApplication.class, args);
	}
	
	
	//Nao esta sendo utilizado, esta usando o Feing mantido aqui para exemplo
	@Bean
	@LoadBalanced
	public RestTemplate restTemplate() 
	{
		final RestTemplate restTemplate = new RestTemplate();

        List<ClientHttpRequestInterceptor> interceptors = restTemplate.getInterceptors(); 
        if (CollectionUtils.isEmpty(interceptors)) 
        {
            interceptors = new ArrayList<>();
        }
        interceptors.add(new RestTemplateHeaderModifierInterceptor());
        restTemplate.setInterceptors(interceptors);
	 
	    return restTemplate;
	}
	
	//Usado pelo RestTemplate
	public class RestTemplateHeaderModifierInterceptor implements ClientHttpRequestInterceptor 
	{
		

	    @Override
	    public ClientHttpResponse intercept( HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException 
	    {
    		String jwtToken = jwtTokenService.getToken();
            if (StringUtils.hasText(jwtToken)) 
            {
            	request.getHeaders().add("Authorization", "Bearer "  + jwtToken);
            }
	    	ClientHttpResponse response = execution.execute(request, body);
	        return response;
	    }
	}
	
	//Usado pelo Feing
	@Bean
	public RequestInterceptor requestTokenBearerInterceptor() 
	{
		return requestTemplate -> 
		{
			String jwtToken = jwtTokenService.getToken();
			requestTemplate.header("Authorization", String.format("Bearer %s", jwtToken));
		};
	}
}
