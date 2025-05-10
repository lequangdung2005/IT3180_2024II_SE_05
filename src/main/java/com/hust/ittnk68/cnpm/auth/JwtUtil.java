package com.hust.ittnk68.cnpm.auth;

import java.security.*;
import io.jsonwebtoken.*;
import java.util.Date;
import org.springframework.stereotype.Component;
// import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.IOException;

import com.hust.ittnk68.cnpm.model.Account;

// import jakarta.annotation.*;

@Component
public class JwtUtil {

    private final KeyPair keyPair;
    private final ObjectMapper objectMapper;

    // @PostConstruct
    // public void init () {
    //     System.out.println ("JwtUtil borned!");
    // }

    public JwtUtil ()
    {
        keyPair = new RsaKeyUtil ().generateKeyPair ();
        objectMapper = new ObjectMapper ();
    }

    public <T> String generateToken (T object) throws Exception
    {
        try {
            String jsonString = objectMapper.writeValueAsString (object);         // convert to json
            System.out.printf ("debug: %s\n", jsonString);
            return Jwts.builder()
                .setSubject (jsonString) 
                .setIssuedAt (new Date ())
                .setExpiration (new Date (System.currentTimeMillis () + 1000*60*60))
                .signWith (getPrivateKey (), SignatureAlgorithm.RS256)
                .compact ();
        }
        catch (JsonProcessingException e) {
            throw new RuntimeException("Error serializing Account object", e);
        }
    }

    public <T> T extract (String token, Class<T> clazz)
    {
        try {
            String jsonString = Jwts.parserBuilder ()
                .setSigningKey (getPublicKey ())
                .build ()
                .parseClaimsJws (token)
                .getBody ()
                .getSubject ();
            return objectMapper.readValue (jsonString, clazz);
        }
        catch (ExpiredJwtException e) {
            throw new IllegalArgumentException ("Token has expired");
        }
        catch (JwtException | IllegalArgumentException e) {
            throw new IllegalArgumentException ("Invalid token");
        }
        catch (IOException e) {
            throw new RuntimeException("Error deserializing Account object", e);
        }
    }

    public <T> boolean isTokenValid (String token, Class<T> clazz)
    {
        try {
            extract (token, clazz);
            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }

    public PrivateKey getPrivateKey() {
        return keyPair.getPrivate();
    }

    public PublicKey getPublicKey() {
        return keyPair.getPublic();
    }

} 
