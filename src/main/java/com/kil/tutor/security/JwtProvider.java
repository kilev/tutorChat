package com.kil.tutor.security;

import com.kil.tutor.TutorChatException;
import com.kil.tutor.entity.user.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.CertificateException;
import java.time.Instant;
import java.util.Date;

@Service
public class JwtProvider {
    private static final String KEY_STORE_PASSWORD = "secret";

    private KeyStore keyStore;

    @PostConstruct
    public void init(){
        try {
            keyStore = KeyStore.getInstance("JKS");
            InputStream resourceAsStream = getClass().getResourceAsStream("/springblog.jks");
            keyStore.load(resourceAsStream, KEY_STORE_PASSWORD.toCharArray());
        } catch (KeyStoreException | IOException | NoSuchAlgorithmException | CertificateException e) {
            throw new TutorChatException("Exception occurred loading keystore.", e);
        }
    }

    public String generateToken(Authentication authentication, Instant jwtExpirationTime) {
        User user = (User) authentication.getPrincipal();
        return generateTokenWithUserName(user, jwtExpirationTime);
    }

    public String generateTokenWithUserName(User user, Instant jwtExpirationTime){
        return Jwts.builder()
                .setSubject(user.getUsername())
                .setClaims(Jwts.claims().setId(user.getId().toString()))
                .setIssuedAt(Date.from(Instant.now()))
                .signWith(getPrivateKey())
                .setExpiration(Date.from(jwtExpirationTime))
                .compact();
    }

    public void validateJwt(String jwt) {
        Jwts.parserBuilder()
                .setSigningKey(getPublicKey())
                .build()
                .parseClaimsJws(jwt);
    }

    public String getUserName(String jwt) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getPublicKey())
                .build()
                .parseClaimsJws(jwt)
                .getBody();
        return claims.getSubject();
    }

    private PublicKey getPublicKey() {
        try {
            return keyStore.getCertificate("springblog").getPublicKey();
        } catch (KeyStoreException e) {
            throw new TutorChatException("Exception occurred while retrieving public key from keystore.", e);
        }
    }

    private PrivateKey getPrivateKey() {
        try {
            return (PrivateKey) keyStore.getKey("springblog", KEY_STORE_PASSWORD.toCharArray());
        } catch (KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException e) {
            throw new TutorChatException("Exception occurred while retrieving private key from keystore.", e);
        }
    }
}
