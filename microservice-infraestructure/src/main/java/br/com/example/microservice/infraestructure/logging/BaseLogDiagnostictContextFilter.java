package br.com.example.microservice.infraestructure.logging;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.MDC;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.util.StringUtils;


public class BaseLogDiagnostictContextFilter implements javax.servlet.Filter 
{
	private static final String JWT_PREFERRED_USERNAME = "preferred_username";
	private static final String[] HEADERS_TO_TRY = {
            "X-Forwarded-For",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_X_FORWARDED_FOR",
            "HTTP_X_FORWARDED",
            "HTTP_X_CLUSTER_CLIENT_IP",
            "HTTP_CLIENT_IP",
            "HTTP_FORWARDED_FOR",
            "HTTP_FORWARDED",
            "HTTP_VIA",
            "REMOTE_ADDR" };
	
	@Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // do nothing
    }
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException 
    {
    	try
		{
    		HttpServletRequest httpRequest= (HttpServletRequest) request;
			
			String url = httpRequest.getRequestURL().toString();
			MDC.put("user.loggedIn", getLogin());
			MDC.put("user.ip", getClientIpAddress(httpRequest));
			chain.doFilter(request,response);
		} finally
		{
			MDC.clear();
		}
    }
    
    private String getLogin()
    {
		SecurityContext context = SecurityContextHolder.getContext();
		
		if (context == null || context.getAuthentication() == null) 
		{
			return "anonymous";
		}
		
        Authentication authentication = context.getAuthentication();
        if (authentication instanceof JwtAuthenticationToken)
        {
        	JwtAuthenticationToken jwtAut = (JwtAuthenticationToken) authentication;
        	Jwt jwt = (Jwt)jwtAut.getPrincipal();
        	if (StringUtils.hasText(jwt.getClaimAsString(JWT_PREFERRED_USERNAME)))
        	{
        		return jwt.getClaimAsString(JWT_PREFERRED_USERNAME); 
        	}
        }
		return authentication.getName();
    }

    private String getClientIpAddress(HttpServletRequest request) 
    {
        for (String header : HEADERS_TO_TRY) {
            String ip = request.getHeader(header);
            if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
                return ip;
            }
        }

        return request.getRemoteAddr();
    }
    
    
	@Override
    public void destroy() 
    {
        // do nothing
    }
}