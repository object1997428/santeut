package com.santeut.gateway.authorize;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
@Component
public class AuthorizationToken {

            @Autowired
            private Environment env;
            public boolean validateToken(String token){
                String jwtSecret = env.getProperty("jwt.secretKey");
                try {
                    SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
                    Jwts.parser()
                        .verifyWith(key)
                        .build()
                        .parseSignedClaims(token)
                    .getPayload();

             return true;
        }catch (Exception e){
            return false;

        }

    }
}
