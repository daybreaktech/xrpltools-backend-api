package com.daybreaktech.xrpltools.backendapi.security.jwt;

import com.daybreaktech.xrpltools.backendapi.dto.XrplUserDetails;
import com.daybreaktech.xrpltools.backendapi.exceptions.XrplToolsException;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtService {

    @Value("${app.xrpl-tools.jwtSecret}")
    private String jwtSecret;

    @Value("${app.xrpl-tools.jwtExpirationMs}")
    private int jwtExpirationMs;

    public String generateJwtToken(Authentication authentication) {

        XrplUserDetails userPrincipal = (XrplUserDetails) authentication.getPrincipal();

        return Jwts.builder()
                .setSubject((userPrincipal.getUsername()))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateJwtToken(String authToken) throws Exception {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            throw new Exception("Invalid JWT Signature");
        } catch (MalformedJwtException e) {
            throw new Exception("Invalid JWT Token");
        } catch (ExpiredJwtException e) {
            throw new Exception("JWT Token Expired");
        } catch (UnsupportedJwtException e) {
            throw new Exception("Unsupported JWT Token");
        } catch (IllegalArgumentException e) {
            throw new Exception("JWT claims string is empty: " + e.getMessage());
        }
    }

}
