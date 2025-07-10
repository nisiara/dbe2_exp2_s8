package com.letrasypapeles.backend.security.jwt;

import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtils {

  @Value("${jwt.secret.key}")
  private String jwtSecretKey;

  @Value("${jwt.expiration.time}")
  private String jwtExpirationTime;


  //Generar token JWT
  public String generateAccessToken(String username) {
    return Jwts.builder()
      .setSubject(username)
      .setIssuedAt(new Date(System.currentTimeMillis()))  // Implementación para generar un token JWT
      .setExpiration(new Date(System.currentTimeMillis() + Long.parseLong(jwtExpirationTime)) )
      .signWith(getSignatureKey(), SignatureAlgorithm.HS256)
      .compact();
    }   

  //Obtener firma del token JWT
  public Key getSignatureKey() {
    byte[] keyBytes = Decoders.BASE64.decode(jwtSecretKey);
    return Keys.hmacShaKeyFor(keyBytes);
  }

  //Validar token JWT
  public boolean validateToken(String token) {
    try {
      Jwts.parserBuilder()
        .setSigningKey(getSignatureKey())
        .build()
        .parseClaimsJws(token);
      return true;
    } 
    catch (Exception e) {
      return false;
    }
  }
}