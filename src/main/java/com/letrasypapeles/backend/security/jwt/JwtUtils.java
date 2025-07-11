package com.letrasypapeles.backend.security.jwt;

import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
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
      throw new AuthenticationCredentialsNotFoundException("El token JWT ya no es válido o ha expirado", e);
		}
  }

  //Extrar los claims de el token JWT
  public Claims getClaimsFromToken(String token) {
    return Jwts.parserBuilder()
      .setSigningKey(getSignatureKey())
      .build()
      .parseClaimsJws(token)
      .getBody();
  }

  //Obtener el nombre de usuario del token JWT
  public String getUsernameFromClaim(String token) {
    return getClaimsFromToken(token).getSubject();
  }




}