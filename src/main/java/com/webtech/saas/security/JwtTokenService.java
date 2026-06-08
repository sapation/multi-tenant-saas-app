package com.webtech.saas.security;


import com.webtech.saas.exceptions.UnauthorizedException;
import com.webtech.saas.properties.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SecurityException;
import jakarta.annotation.PostConstruct;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtTokenService {

    private final JwtProperties jwtProperties;
    private PrivateKey privateKey;
    private PublicKey publicKey;

    @PostConstruct
    public void init() {
        try {
            this.privateKey = loadPrivateKey(this.jwtProperties.getPrivateKeyPath());
            this.publicKey = loadPublicKey(this.jwtProperties.getPublicKeyPath());

            log.debug("Private && public key loaded successfully");
        } catch (final Exception e) {
            log.error("Error loading private key", e);
            throw new RuntimeException("Error in loading private and public keys");
        }
    }

    public String getAccessToken(@NonNull final String tenantId, @NonNull final String userId, final String role) {
        final Date now = new Date();
        final Date expiration = new Date(System.currentTimeMillis() + this.jwtProperties.getAccessTokenExpiration());

        return Jwts.builder()
                .subject(userId)
                .claim("tenant_id", tenantId)
                .claim("role", role)
                .issuedAt(now)
                .expiration(expiration)
                .issuer("multi-tenant-saas-app")
                .signWith(this.privateKey, Jwts.SIG.RS256)
                .compact();
    }

    public boolean validateToken(String jwt) {
        try {
            Jwts.parser()
                    .verifyWith(this.publicKey)
                    .build()
                    .parseSignedClaims(jwt);
            return true;
        } catch (final UnsupportedOperationException e) {
            throw new UnauthorizedException("Token is not signed");
        } catch (final MalformedJwtException e) {
            throw new UnauthorizedException("Token is malformed");
        } catch (final SecurityException e) {
            throw new UnauthorizedException("Invalid Jwt signature");
        } catch (final IllegalArgumentException e) {
            throw new UnauthorizedException("Jwt claim string is empty");
        } catch (final Exception e) {
            throw new UnauthorizedException("Token has Expired");
        }
    }

    public String getUserIdFromToken(String token) {
        final Claims claims = getClaimsFromToken(token);
        return claims.getSubject();
    }

    public String getTenantIdFromToken(String token) {
        final Claims claims = getClaimsFromToken(token);
        return claims.get("tenant_id", String.class);
    }

    public String getRoleFromToken(String token) {
        final Claims claims = getClaimsFromToken(token);
        return claims.get("role", String.class);
    }

    private Claims getClaimsFromToken(String token ) {
        return Jwts.parser()
                .verifyWith(this.publicKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private PublicKey loadPublicKey(final String publicKeyPath) throws Exception {
        try (final InputStream inputStream = JwtTokenService.class.getClassLoader().getResourceAsStream(publicKeyPath)) {
            if (inputStream == null) {
                throw new RuntimeException("Private key not found");
            }

            final String key = new String(inputStream.readAllBytes());
            final String publicKeyPEM = key.replace("-----BEGIN PUBLIC KEY-----", "")
                    .replace("-----END PUBLIC KEY-----", "")
                    .replaceAll("\\s", "");
            final byte[] encoded = Base64.getDecoder().decode(publicKeyPEM);
            final X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encoded);
            return KeyFactory.getInstance("RSA").generatePublic(keySpec);
        }
    }

    private PrivateKey loadPrivateKey(final String privateKeyPath) throws Exception {
        try (final InputStream inputStream = JwtTokenService.class.getClassLoader().getResourceAsStream(privateKeyPath)) {
            if (inputStream == null) {
                throw new RuntimeException("Private key not found");
            }

            final String key = new String(inputStream.readAllBytes());
            final String privateKeyPEM = key
                    .replace("-----BEGIN PRIVATE KEY-----", "")
                    .replace("-----END PRIVATE KEY-----", "")
                    .replaceAll("\\s", "");
            final byte[] encoded = Base64.getDecoder().decode(privateKeyPEM);
            final PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encoded);
            return KeyFactory.getInstance("RSA").generatePrivate(keySpec);
        }
    }
}
