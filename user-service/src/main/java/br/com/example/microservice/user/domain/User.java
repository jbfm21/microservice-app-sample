package br.com.example.microservice.user.domain;

import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name="users")
@Builder @Data @NoArgsConstructor @AllArgsConstructor @ToString @EqualsAndHashCode
public class User 
{
    @Id
    @Column(name = "user_id", updatable = false, nullable = false, columnDefinition = "VARCHAR(36)")
    @Type(type = "uuid-char")    
    private UUID userId;
    
    String firstName;
    String lastName;
    String email;
    
    @OneToOne(mappedBy="user", fetch=FetchType.EAGER, cascade = CascadeType.ALL)
    CardDetails cardDetails;    
    
}

