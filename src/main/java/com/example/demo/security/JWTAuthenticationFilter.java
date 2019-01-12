//package com.example.demo.security;
//
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.util.ArrayList;
//import java.util.Date;
//
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import static com.auth0.jwt.algorithms.Algorithm.HMAC512;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//import com.auth0.jwt.JWT;
//import com.example.demo.model.Response;
//import com.example.demo.repo.ApplicationUserRepository;
//import com.example.demo.service.UserDetailsServiceImpl;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.google.gson.Gson;
//
//public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
//    private AuthenticationManager authenticationManager;
//    private ApplicationUserRepository applicationUserRepository;
//
//    public JWTAuthenticationFilter(AuthenticationManager authenticationManager,ApplicationUserRepository applicationUserRepository) {
//        this.authenticationManager = authenticationManager;
//        this.applicationUserRepository=applicationUserRepository;
//  
//    }
//
//    @Override
//    public Authentication attemptAuthentication(HttpServletRequest req,
//                                                HttpServletResponse res) throws AuthenticationException {
//        try {
//        	com.example.demo.model.User creds = new ObjectMapper()
//                    .readValue(req.getInputStream(), com.example.demo.model.User.class);
//
//            return authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(
//                            creds.getUsername(),
//                            creds.getPassword(),
//                            new ArrayList<>())
//            );
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
//    
//    @Override
//    protected void successfulAuthentication(HttpServletRequest req,
//                                            HttpServletResponse res,
//                                            FilterChain chain,
//                                            Authentication auth) throws IOException, ServletException {
//             
//        String token = JWT.create()
//                .withSubject(((User) auth.getPrincipal()).getUsername())
//                .withExpiresAt(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
//                .sign(HMAC512(SecurityConstants.SECRET.getBytes()));
//        res.addHeader(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + token);
//        com.example.demo.model.User user = applicationUserRepository.findByUsername(((User) auth.getPrincipal()).getUsername());
//        
//        com.example.demo.model.User u1=new com.example.demo.model.User();
//        u1.setUsername(user.getUsername());
//        u1.setGender(user.getGender());
//        u1.setId(user.getId());
//        Response r =new Response();
//        r.setUser(u1);
//        r.setToken(SecurityConstants.TOKEN_PREFIX + token);
//        String jsonString = new Gson().toJson(r);
//        PrintWriter out = res.getWriter();
//        res.setContentType("application/json");
//        res.setCharacterEncoding("UTF-8");
//        out.print(jsonString);
//        out.flush();
//    }
//    
//}
//
