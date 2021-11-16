# microservice-app-sample
Microservice application sample with SprintBoot


# Environment

## Build Pipeline

### jib-maven-plugin (https://github.com/GoogleContainerTools/jib/tree/master/jib-maven-plugin)

Jib is a Maven plugin for building Docker and OCI images for your Java applications.

## External Services/Servers

### MySQL

Use in product-service, product-review-service, order-service

- How to start:  docker run -d -p 3306:3306 -e MYSQL_ROOT_PASSWORD=pass -e MYSQL_DATABASE=microservice_db -e MYSQL_USER=sa -e MYSQL_PASSWORD=pass --name mysql-db mysql:latest
- Exposed port: 3306

- Add priveleges to user SA

mysql -uroot -ppass 
use microservice_db
CREATE DATABASE order_service;
CREATE DATABASE product_service;
CREATE DATABASE product_review_service;
GRANT ALL PRIVILEGES ON order_service.* TO sa;
GRANT ALL PRIVILEGES ON product_service.* TO sa;
GRANT ALL PRIVILEGES ON product_review_service.* TO sa;


### Keycloack (https://www.keycloak.org/)

Open Source Identity and Access Management: Add authentication to applications and secure services 

- How to start: docker run -d --name keycloak -p 8888:8080 -e KEYCLOAK_USER=spring -e KEYCLOAK_PASSWORD=spring123 jboss/keycloak
- Exposed port: 8888
- Endpoint:  http://localhost:8888/auth/
- Credential: 
   - user: spring  
   - password: spring123
- Initial Configuration:
   - 

### Vault (https://www.vaultproject.io)

 Identity-based secrets and encryption management system. A secret is anything that you want to tightly control access to, such as API encryption keys, passwords, or certificates. Vault provides encryption services that are gated by authentication and authorization methods. Using Vault’s UI, CLI, or HTTP API, access to secrets and other sensitive data can be securely stored and managed, tightly controlled (restricted), and auditable.

- How to start: start: docker run --name vault --cap-add=IPC_LOCK -e 'VAULT_DEV_ROOT_TOKEN_ID=myroot' -e 'VAULT_DEV_LISTEN_ADDRESS=0.0.0.0:8200' vault
- Exposed port: 8200
- Endpoint: http://localhost:8200/ui/vault/auth
- Credential: need to get the token created when start the container

__How to add secret values__

Inside container execute the commands belowe

- Export vault address and token:
```
export VAULT_ADDR='http://127.0.0.1:8200'
export VAULT_TOKEN="s.MroNtKUJTX2drcxqhHZePtJx"
```
- Add sample properties
```
a) Create a product-service-example.vault.json file with the example bellow and save in \\wsl$\docker-desktop-data\version-pack-data\community\docker\volumes\<volume> (if you use docker desktop)
{
  "spring.datasource.username": "root",
  "spring.datasource.password": "mongodbpwd"
}

b) Execute this command inside the path where product-service-example.vault.json was created to create the properties in vault:  vault kv put secret/catalog-product-service @product-service-example.vault.json

c) Execute this command to check if the property was created correctly:  vault kv get secret/catalog-product-service

d) Create a application-dev.vault.json file with this example bellow and save in \\wsl$\docker-desktop-data\version-pack-data\community\docker\volumes\<volume>
{
	"spring.rabbitmq.username": "guest",
	"spring.rabbitmq.password": "guest"
}

e) Execute this command inside the path where application-dev.vault.json was created to create the properties in vault: vault kv put secret/application,dev @application-dev.vault.json

f) Execute the command to check if the property was created correctly: vault kv get secret/application,dev
```

### RabbitMq (https://www.rabbitmq.com/)

RabbitMQ is a messaging broker - an intermediary for messaging. It gives your applications a common platform to send and receive messages, and your messages a safe place to live until received.

- How to start: docker run -d --hostname my-rabbit --name rabbit13 -p 15672:15672 -p 5672:5672 -p 25676:25676 rabbitmq:3-management
- Endpoint: http://localhost:15672/
- Credential: 
   - user: guest  
   - password: guest
- In this application is use with axon and is use with actuactor to notify the services that de configuration properties was changed.

### Zipkin (https://zipkin.io/)

Zipkin is a distributed tracing system. It helps gather timing data needed to troubleshoot latency problems in service architectures. Applications need to be “instrumented” to report trace data to Zipkin. ... This usually means configuration of a tracer or instrumentation library.

- How to start: docker run --name zipkin -d -p 9411:9411 openzipkin/zipkin
- Exposed port: 9411
- Endpoint: http://localhost:9411/zipkin/

### Axon Framework and Axon Server (https://axoniq.io/)

The open source Axon Framework provides a clean, elegant Java API for writing DDD, CQRS and Event Sourcing applications. It provides basic building blocks for writing aggregates, commands, queries, events, sagas, command handlers, event handlers, query handlers, repositories, communication buses and so on.

- How to start: docker run -d --name axon-server -p 8024:8024 -p 8124:8124 axoniq/axonserver
- Exposedport: 8024 and 8124
- Endpoint: http://localhost:8024/


### Eureka Discovery Server (https://github.com/Netflix/eureka)

Netflix Eureka is a REST based middleware designed for discovery and load balancing of web applications. For those who already have a Netflix Eureka app, this article explains the configurations required to get a Netflix Eureka based app running correctly in App Service

- Exposed port: 8761
- Endpoint: http://localhost:8761/



## Spring / SpringBoot Stack

### Resillience - Resilience4j (https://resilience4j.readme.io/docs)

Resilience4j is a lightweight, easy-to-use fault tolerance library inspired by
Netflix Hystrix, but designed for Java 8 and functional programming. Lightweight, because the library only uses Vavr, which does not have any other external library dependencies. Netflix Hystrix, in contrast, has a compile dependency to Archaius which has many more external library dependencies such as Guava and Apache Commons Configuration.

Resilience4j provides higher-order functions (decorators) to enhance any functional interface, lambda expression or method reference with a Circuit Breaker, Rate Limiter, Retry or Bulkhead. You can stack more than one decorator on any functional interface, lambda expression or method reference. The advantage is that you have the choice to select the decorators you need and nothing else.

### Monitoring - Spring Boot Actuator (https://docs.spring.io/spring-boot/docs/current/reference/html/actuator.html)

Spring Boot includes a number of additional features to help you monitor and manage your application when you push it to production. You can choose to manage and monitor your application by using HTTP endpoints or with JMX. Auditing, health, and metrics gathering can also be automatically applied to your application.

### Api Gateway - Spring Cloud Gateway (https://spring.io/projects/spring-cloud-gateway)

This project provides a library for building an API Gateway on top of Spring WebFlux. Spring Cloud Gateway aims to provide a simple, yet effective way to route to APIs and provide cross cutting concerns to them such as: security, monitoring/metrics, and resiliency.

### Distribution Tracing - Spring Cloud Sleuth (https://spring.io/projects/spring-cloud-sleuth)

Spring Cloud Sleuth provides Spring Boot auto-configuration for distributed tracing.

### Documentation - Spring Open API (https://springdoc.org/) and Swagger (https://swagger.io/)

Spring Open API:  java library helps automating the generation of API documentation using spring boot projects. springdoc-openapi works by examining an application at runtime to infer API semantics based on spring configurations, class structure and various annotations. Automatically generates documentation in JSON/YAML and HTML format APIs. This documentation can be completed by comments using swagger-api annotations.

Swagger: Simplify API development for users, teams, and enterprises with the Swagger open source and professional toolset. Find out how Swagger can help you design and document your APIs at scale.

### External Configuration - Spring Cloud Config (https://spring.io/projects/spring-cloud-config)

Spring Cloud Config provides server and client-side support for externalized configuration in a distributed system. With the Config Server you have a central place to manage external properties for applications across all environments. The concepts on both client and server map identically to the Spring Environment and PropertySource abstractions, so they fit very well with Spring applications, but can be used with any application running in any language. As an application moves through the deployment pipeline from dev to test and into production you can manage the configuration between those environments and be certain that applications have everything they need to run when they migrate. The default implementation of the server storage backend uses git so it easily supports labelled versions of configuration environments, as well as being accessible to a wide range of tooling for managing the content. It is easy to add alternative implementations and plug them in with Spring configuration.

### MessageBroker - Spring Cloud Bus (https://cloud.spring.io/spring-cloud-bus/reference/html/)

Spring Cloud Bus links the nodes of a distributed system with a lightweight message broker. This broker can then be used to broadcast state changes (such as configuration changes) or other management instructions. A key idea is that the bus is like a distributed actuator for a Spring Boot application that is scaled out. However, it can also be used as a communication channel between apps. This project provides starters for either an AMQP broker or Kafka as the transport.

Is used to send notification to services to refresh de configuration

### Data Repository - Spring Data JPA (hhttps://spring.io/projects/spring-data-jpa)

Spring Data JPA, part of the larger Spring Data family, makes it easy to easily implement JPA based repositories. This module deals with enhanced support for JPA based data access layers. It makes it easier to build Spring-powered applications that use data access technologies.

Implementing a data access layer of an application has been cumbersome for quite a while. Too much boilerplate code has to be written to execute simple queries as well as perform pagination, and auditing. Spring Data JPA aims to significantly improve the implementation of data access layers by reducing the effort to the amount that’s actually needed. As a developer you write your repository interfaces, including custom finder methods, and Spring will provide the implementation automatically

### Authentication - Spring Security (https://spring.io/projects/spring-security)

Spring Security is a powerful and highly customizable authentication and access-control framework. It is the de-facto standard for securing Spring-based applications.

Spring Security is a framework that focuses on providing both authentication and authorization to Java applications. Like all Spring projects, the real power of Spring Security is found in how easily it can be extended to meet custom requirements

### Web Application/Rest - Spring Web flow (https://spring.io/projects/spring-webflow)

Spring Web Flow builds on Spring MVC and allows implementing the "flows" of a web application. A flow encapsulates a sequence of steps that guide a user through the execution of some business task. It spans multiple HTTP requests, has state, deals with transactional data, is reusable, and may be dynamic and long-running in nature.

### Messaging Solution - Spring AMQP (https://spring.io/projects/spring-amqp)

The Spring AMQP project applies core Spring concepts to the development of AMQP-based messaging solutions. It provides a "template" as a high-level abstraction for sending and receiving messages. It also provides support for Message-driven POJOs with a "listener container". These libraries facilitate management of AMQP resources while promoting the use of dependency injection and declarative configuration. In all of these cases, you will see similarities to the JMS support in the Spring Framework.

The project consists of two parts; spring-amqp is the base abstraction, and spring-rabbit is the RabbitMQ implementation.

## Libraries

## Lombok (https://projectlombok.org/)

Project Lombok is a java library that automatically plugs into your editor and build tools, spicing up your java.
Never write another getter or equals method again, with one annotation your class has a fully featured builder, Automate your logging variables, and much more.

## Model Mapper (http://modelmapper.org/)

Applications often consist of similar but different object models, where the data in two models may be similar but the structure and concerns of the models are different. Object mapping makes it easy to convert one model to another, allowing separate models to remain segregated.

## Security Concepts

### OAth2 (https://auth0.com/intro-to-iam/what-is-oauth-2/)

OAuth 2.0, which stands for “Open Authorization”, is a standard designed to allow a website or application to access resources hosted by other web apps on behalf of a user. It replaced OAuth 1.0 in 2012 and is now the de facto industry standard for online authorization.

### JWE (https://openid.net/specs/draft-jones-json-web-encryption-02.html)

JSON Web Encryption (JWE) is a means of representing encrypted content using JSON data structures. Related signature capabilities are described in the separate JSON Web Signature (JWS) specificati

# Services

## Recreate environments

docker run -d --hostname my-rabbit --name rabbit13 -p 15672:15672 -p 5672:5672 -p 25676:25676 rabbitmq:3-management
docker run -d --name axon-server -p 8024:8024 -p 8124:8124 axoniq/axonserver

## Pre-configuration

### MySQL

Create services databases. Inside the container, execute this commands:
``` 
mysql -uroot -ppass 
use microservice_db
CREATE DATABASE product_service;
CREATE DATABASE product_review_service;
CREATE DATABASE order_service;
CREATE DATABASE payment_service;
CREATE DATABASE shipping_service;
CREATE DATABASE user_service;

GRANT ALL PRIVILEGES ON product_service.* TO sa;
GRANT ALL PRIVILEGES ON product_review_service.* TO sa;
GRANT ALL PRIVILEGES ON order_service.* TO sa;
GRANT ALL PRIVILEGES ON payment_service.* TO sa;
GRANT ALL PRIVILEGES ON shipping_service.* TO sa;
GRANT ALL PRIVILEGES ON user_service.* TO sa;

``` 
### KeyCloack

- Create SpringBootKeycloak Realm with user spring (password spring123) in http://localhost:8888/auth/
- Import data from microservice-app-sample\docs\realm-export.json
- Create users and assing to roles

## Start Order
- service-registry
- config-server (Before start remember to check de vault token access in application.yml)
- notification-service
- product-service
- product-review-service
- order-service
- payment-service
- shipping-service
- user-service
- api-gateway 

## api-gateway

- Exposed port: 9191
- How to generate docker image: mvnw clean package
- Endpoints:
  - Authentication: http://localhost:9191 
  - Get Session Id: http://localhost:9191/token
  - Get Token: http://localhost:9191/token
- Endpoint examples:
  - product-review-service
     - create: POST  http://localhost:9191/product-reviews {"authorName": "Marcio", "review": "Produto de excelente qualidade","productId": 1}
	 - delete: DELETE  http://localhost:9191/product-reviews/1
	 - update: PUT http://localhost:9191/product-reviews/1 {"authorName": "Marcio", "review": "Produto de excelente qualidade","productId": 1}
	 - find all: GET http://localhost:9191/product-reviews
	 - get one: GET http://localhost:9191/product-reviews/1
  - product-service
     - create: POST  http://localhost:9191/products {"productName": "Protoboard","shortDescription": "Placa de protoboard","longDescription": "Placa de protoboard para fazer experiências","inventoryId": "1"}
	 - delete: DELETE  http://localhost:9191/products/1
	 - update: PUT http://localhost:9191/products/1 {"productName": "Protoboard","shortDescription": "Placa de protoboard","longDescription": "Placa de protoboard para fazer experiências","inventoryId": "1"}
	 - find all: GET http://localhost:9191/products
	 - get one: GET http://localhost:9191/products/1

## config-server

Config Server used by the others services to load the properties from github and vault

- Attention: before start you need to set the vault token, created after start the vault container, in the application.yml
- Exposed port: 9296
- How to generate docker image: mvnw clean package
- Endpoints sample:
  - Get dev product-service profile config : http://localhost:9296/catalog-product-service/dev
  - Get dev product-review-service profile config: http://localhost:9296/product-review-service/dev

## disccovery-service

Is based on eureka server

- Exposed port: 8761
- How to generate docker image: mvnw clean package
- Endpoint: http://localhost:8761/


## product-service

Implement a basic crud pattern

- Exposed port: 9080
- How to generate docker image: mvnw clean package
- Endpoint examples:
   - h2 console: http://localhost:9080/h2
   - Refresh Config: POST http://localhost:9080/actuator/busrefresh
   - Get refreshed config value: GET http://localhost:9080/products/testeRefresh
   - create: POST  http://localhost:9080/products {"productName": "Protoboard","shortDescription": "Placa de protoboard","longDescription": "Placa de protoboard para fazer experiências","inventoryId": "1"}
   - delete: DELETE  http://localhost:9080/products/1
   - update: PUT http://localhost:9080/products/1 {"productName": "Protoboard","shortDescription": "Placa de protoboard","longDescription": "Placa de protoboard para fazer experiências","inventoryId": "1"}
   - find all: GET http://localhost:9080/products
   - actuator: http://localhost:9080/actuator   
   - get one: GET http://localhost:9080/products/1
   - open api: http://localhost:9080/v3/api-docs/
   - swagger:  http://localhost:9080/swagger-ui/index.html?configUrl=/v3/api-docs/swagger-config

## product-review-service

Implement a basic crud pattern with resillient4j whe getting information from product-service. Additional, use RabbitMQ to send notification to notification-service

- Exposed port: 9081
- How to generate docker image: mvnw clean package
- Endpoint examples:
   - create: POST  http://localhost:9081/product-reviews {"authorName": "Marcio", "review": "Produto de excelente qualidade","productId": 1}
   - delete: DELETE  http://localhost:9081/product-reviews/1
   - update: PUT http://localhost:9081/product-reviews/1 {"authorName": "Marcio", "review": "Produto de excelente qualidade","productId": 1}
   - find all: GET http://localhost:9081/product-reviews
   - get one: GET http://localhost:9081/product-reviews/1
   - actuator: http://localhost:9081/actuator   
   - open api: http://localhost:9081/v3/api-docs/
   - swagger:  http://localhost:9081/swagger-ui/index.html?configUrl=/v3/api-docs/swagger-config

## notification-service

- Exposed port: 9083
- How to generate docker image: mvnw clean package

## user-service

- Exposed port: 9087
- How to generate docker image: mvnw clean package
- Endpoint examples:
   - add credit-detail information: POST http://localhost:9087/users/card-details
     - If user not exists, this endpoin will create an user based in jwt information (id, givenName, firstName, email)
   - get authenticated user info: POST http://localhost:9087/users/info
	 
```
Example to create 	 
{
  "name": "Visa",
  "cardNumber":"1234567890",
  "validUntilMonth": 5,
  "validUntilYear": 21,
  "cvv": 123
}
```

## order-service

Implements the CQRS pattern with Axon and RabbitMQ

- Exposed port: 9082
- How to generate docker image: mvnw clean package
- Endpoint examples:
   - create order: POST http://localhost:9082/orders
   - add product: POST http://localhost:9082/orders/{id}/product/{product-id}
   - increment product: PUT http://localhost:9082/orders/{id}/product/{product-id}/increment
   - decrement product: PUT http://localhost:9082/orders/{id}/product/{product-id}/decrement
   - remove product: DELETE http://localhost:9082/orders/{id}/product/{product-id}
   - confirm order: PUT http://localhost:9082/orders/{id}/confirm
   - ship order: PUT http://localhost:9082/orders/{id}/ship
   - get all events: GET http://localhost:9082/orders/events/{id}
   - list all order: GET http://localhost:9082/orders/all-orders

## payment-service

Implements the CQRS pattern with Axon and RabbitMQ

- Exposed port: 9085
- How to generate docker image: mvnw clean package
- Endpoint examples:
   - list all shippings: POST http://localhost:9086/payments/all-payments


## shipping-service

Implements the CQRS pattern with Axon and RabbitMQ

- Exposed port: 9086
- How to generate docker image: mvnw clean package
- Endpoint examples:
   - list all shippings: POST http://localhost:9086/shippings/all-shippings




## Common Errors

Problem: Cannot construct instance of `br.com.example.microservice.shopdomain.command.CancelOrderCommand` (no Creators, like default constructor, exist): cannot deserialize from Object value (no delegate- or property-based Creator)
Solution: Add @Jacksonized lombok annotation

## Refernces

- https://www.cloudamqp.com/blog/part4-rabbitmq-for-beginners-exchanges-routing-keys-bindings.html
