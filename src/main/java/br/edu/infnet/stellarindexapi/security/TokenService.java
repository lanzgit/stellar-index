package br.edu.infnet.stellarindexapi.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TokenService {

  @Value("${stellar.jwt.chave-secreta}")
  private String chaveSecreta;

  @Value("${stellar.jwt.tempo-expiracao}")
  private Long tempoExpiracao;

  public String criarToken(String username, String papel) {
    Date agora = new Date();
    Date expiracao = new Date(agora.getTime() + tempoExpiracao);

    return Jwts.builder()
        .subject(username)
        .claim("papel", papel)
        .issuedAt(agora)
        .expiration(expiracao)
        .signWith(this.obterChave())
        .compact();
  }

  public String extrairUsername(String token) {
    return Jwts.parser()
        .verifyWith((SecretKey) obterChave())
        .build()
        .parseSignedClaims(token)
        .getPayload()
        .getSubject();
  }

  public String extrairPapel(String token) {
    return Jwts.parser()
        .verifyWith((SecretKey) obterChave())
        .build()
        .parseSignedClaims(token)
        .getPayload()
        .get("papel", String.class);
  }

  public boolean validarToken(String token) {
    try {
      Jwts.parser().verifyWith((SecretKey) obterChave()).build().parseSignedClaims(token);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  public Key obterChave() {
    return Keys.hmacShaKeyFor(chaveSecreta.getBytes(StandardCharsets.UTF_8));
  }
}
