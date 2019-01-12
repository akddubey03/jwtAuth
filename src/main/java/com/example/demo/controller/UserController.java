package com.example.demo.controller;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.JWT;
import com.example.demo.model.Response;
import com.example.demo.model.User;
import com.example.demo.repo.ApplicationUserRepository;
import com.example.demo.security.SecurityConstants;
import com.google.gson.Gson;

@RestController
@RequestMapping("/users")
public class UserController {
	
	private static final AtomicInteger counter = new AtomicInteger(0);

	
    private ApplicationUserRepository applicationUserRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserController(ApplicationUserRepository applicationUserRepository,
                          BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.applicationUserRepository = applicationUserRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @PostMapping("/sign-up")
    public void signUp(@RequestBody User user, HttpServletResponse res) throws IOException{
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setId(counter.incrementAndGet());
        applicationUserRepository.save(user);
        
        String token = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
                .sign(HMAC512(SecurityConstants.SECRET.getBytes()));
        User u1=new User();
        u1.setUsername(user.getUsername());
        u1.setGender(user.getGender());
        u1.setId(user.getId());
        Response r =new Response();
        r.setUser(u1);
        r.setToken(SecurityConstants.TOKEN_PREFIX + token);
        String jsonString = new Gson().toJson(r);
        PrintWriter out = res.getWriter();
        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");
        out.print(jsonString);
        out.flush();
    }
    
    
    @GetMapping("/getUser")
    public @ResponseBody User getUser(@RequestParam("name")String username){
    	return applicationUserRepository.findByUsername(username);
    }
  
}
