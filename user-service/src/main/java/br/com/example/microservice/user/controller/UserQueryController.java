package br.com.example.microservice.user.controller;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.axonframework.queryhandling.QueryGateway;
import org.modelmapper.ModelMapper;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import br.com.example.microservice.infraestructure.services.JwtTokenService;
import br.com.example.microservice.user.domain.CardDetails;
import br.com.example.microservice.user.domain.User;
import br.com.example.microservice.user.dto.CardDetailsDTO;
import br.com.example.microservice.user.dto.UserDTO;
import br.com.example.microservice.user.infraestructure.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.log4j.Log4j2;

@RestController
@Log4j2
@RequestMapping("/users")
@RestControllerAdvice
@RefreshScope 
public class UserQueryController {

    private final UserRepository repository;
    private ModelMapper modelMapper;
    private JwtTokenService jwtTokenService;

    public UserQueryController(QueryGateway queryGateway, UserRepository repository, ModelMapper modelMapper, JwtTokenService jwtTokenService) {
        this.repository = repository;
        this.modelMapper = modelMapper;
        this.jwtTokenService = jwtTokenService;
    }
    

    //TODO: @PreAuthorize("hasRole('PRF_USER_FINDALL')")
    @GetMapping("/all-users")
    @Operation(summary = "List all users")
    @ApiResponses(value = { 
      @ApiResponse(responseCode = "200", description = "Found at least one user", content = { @Content(mediaType = "application/json",  schema = @Schema(implementation = UserDTO.Response.Public.class))}),
      @ApiResponse(responseCode = "404", description = "User not found", content = @Content) 
	})    
	public  ResponseEntity<List<UserDTO.Response.Public>> listAll()
	{
    	Iterable<User> result = repository.findAll();
    	List<UserDTO.Response.Public> resultDTO = StreamSupport.stream(result.spliterator(), false).map(user -> modelMapper.map(user, UserDTO.Response.Public.class)).collect(Collectors.toList());
    	return new ResponseEntity<>(resultDTO, HttpStatus.OK);
	}
    
    //TODO: @PreAuthorize("hasRole('PRF_USER_GETPAYMENT_DETAILS')")
    @Operation(summary = "Get user  by its id")
    @ApiResponses(value = { 
      @ApiResponse(responseCode = "200", description = "Found the user", content = { @Content(mediaType = "application/json",  schema = @Schema(implementation = UserDTO.Response.Public.class)) }),
      @ApiResponse(responseCode = "400", description = "Invalid id supplied",  content = @Content), 
      @ApiResponse(responseCode = "404", description = "User not found", content = @Content) 
    })
    @GetMapping(value = "/{id}")
    @PreAuthorize("hasRole('PRF_PRODUCT_GET')")
    public ResponseEntity<UserDTO.Response.Public> get(@PathVariable UUID id) 
    {
        log.info("Finding  user: {}", id);
    	User user = repository.findByIdOrNotFoundException(id); 
        return new ResponseEntity<>(modelMapper.map(user,  UserDTO.Response.Public.class), HttpStatus.OK);
    }
    

    //TODO: @PreAuthorize("hasRole('PRF_USER_ADD_CARD_DETAIL')")
    @Operation(summary = "Add user´s  Card Details")
    @ApiResponses(value = { 
      @ApiResponse(responseCode = "201", description = "Card details added", content = { @Content(mediaType = "application/json",  schema = @Schema(implementation = UserDTO.Response.Public.class)) })
    })
    @PostMapping("card-details")
    public ResponseEntity<Object> create(@RequestBody CardDetailsDTO.Request.Create cardDetailDTO) 
    {
    	log.info("Add user card detail: {}", cardDetailDTO);
    	UUID userId = jwtTokenService.getUserId();
    	CardDetails cardDetails = modelMapper.map(cardDetailDTO, CardDetails.class);
    	
    	User user = repository.findById(userId).orElse(User.builder().userId(userId).build());
    	user.setCardDetails(cardDetails);
    	cardDetails.setUser(user);
    	
    	//TODO:user.validate(userValidator);
    	
    	User savedEntity = repository.save(user);
        return new ResponseEntity<>(savedEntity, HttpStatus.CREATED);
    }    
   
}