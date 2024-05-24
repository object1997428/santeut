package com.santeut.gateway.authorize;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.security.Key;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
@Component
public class AuthorizationToken {

    @Value("${jwt.secretKey}")
    private String jwtSecretKey;

            public boolean validateToken(String token){
                try {
                    Jwts.parser()
                        .verifyWith((SecretKey) getKey())
                        .build()
                        .parseSignedClaims(token)
                    .getPayload();
             return true;
        }catch (Exception e){
            return false;
        }
    }

    private Key getKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecretKey));
    }

    public Claims extractAllClaims(String token){

        return Jwts.parser()
                .verifyWith((SecretKey) getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

}
