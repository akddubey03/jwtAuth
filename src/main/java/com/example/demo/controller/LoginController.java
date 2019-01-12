package com.example.demo.controller;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;

import java.util.ArrayList;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.JWT;
import com.example.demo.model.Response;
import com.example.demo.repo.ApplicationUserRepository;
import com.example.demo.security.SecurityConstants;

@RestController
@RequestMapping("/auth")
public class LoginController {
	
	    private AuthenticationManager authenticationManager;
	    private ApplicationUserRepository applicationUserRepository;
	    
	    public LoginController(AuthenticationManager authenticationManager,ApplicationUserRepository applicationUserRepository){
	    	this.authenticationManager=authenticationManager;
	    	this.applicationUserRepository=applicationUserRepository;
	    	
	    }
	
	  @PostMapping("/login")
	    public @ResponseBody Response login(@RequestBody com.example.demo.model.User user,HttpServletResponse res) {
		  System.out.println("login controller called ");
		  Authentication auth=authenticationManager.authenticate(
                  new UsernamePasswordAuthenticationToken(
                		  user.getUsername(),
                		  user.getPassword(),
                          new ArrayList<>()));
		  if(auth.isAuthenticated()){
			  String token = JWT.create()
		                .withSubject(((User) auth.getPrincipal()).getUsername())
		                .withExpiresAt(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
		                .sign(HMAC512(SecurityConstants.SECRET.getBytes()));
		        res.addHeader(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + token);
		        com.example.demo.model.User u = applicationUserRepository.findByUsername(((User) auth.getPrincipal()).getUsername());
		        
		        com.example.demo.model.User u1=new com.example.demo.model.User();
		        u1.setUsername(u.getUsername());
		        u1.setGender(u.getGender());
		        u1.setId(u.getId());
		        Response r =new Response();
		        r.setUser(u1);
		        r.setToken(SecurityConstants.TOKEN_PREFIX + token);
		        return r;
		  }else{
			  throw new BadCredentialsException("Invalid username/password supplied");
		  }
	      	    }

}
