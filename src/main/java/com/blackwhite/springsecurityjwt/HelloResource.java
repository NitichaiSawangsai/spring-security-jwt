package com.blackwhite.springsecurityjwt;

import com.blackwhite.springsecurityjwt.models.AuthenticationRequest;
import com.blackwhite.springsecurityjwt.models.AuthenticationResponse;
import com.blackwhite.springsecurityjwt.services.MyUserService;
import com.blackwhite.springsecurityjwt.util.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloResource {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private MyUserService userDetailsService;

    @Autowired
    private JwtUtil jwtTokenUtil;
    
    
    @RequestMapping( "/hello" )
    public String hello() {
        return "Hello World Aom";
    }

    @RequestMapping(value= "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new Exception("Incorrect username or password =>> ",e);
        }

        final UserDetails userDetails = userDetailsService
        .loadUserByUsername(authenticationRequest.getUsername());

        final String jwt = jwtTokenUtil.generateTokeno(userDetails);

        return ResponseEntity.ok(new AuthenticationResponse(jwt));

    }
}
