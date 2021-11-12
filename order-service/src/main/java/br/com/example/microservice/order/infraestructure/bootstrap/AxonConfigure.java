package br.com.example.microservice.order.infraestructure.bootstrap;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EntityScan(" br.com.example.microservice.order.infraestructure.entity")
public class AxonConfigure {

}
