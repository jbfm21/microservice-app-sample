package br.com.example.microservice.infraestructure.security;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;


public class KeycloakRealmRoleConverter  implements Converter<Jwt, Collection<GrantedAuthority>> {
	@Override
    public Collection<GrantedAuthority> convert(Jwt jwt) 
	{
        final Map<String, Object> realmAccess = (Map<String, Object>) jwt.getClaims().get("realm_access");
        return ((List<?>)realmAccess.get("roles")).stream()
                .map(roleName -> "ROLE_" + roleName) //prefixando a role para mapear com o Spring Security 
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
	
	public static Converter<Jwt, AbstractAuthenticationToken> jwtAuthenticationConverter() 
	{
        JwtAuthenticationConverter jwtConverter = new JwtAuthenticationConverter();
        jwtConverter.setJwtGrantedAuthoritiesConverter(new KeycloakRealmRoleConverter());
        return jwtConverter;
    }

}
