package com.twilio.jwt;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.nio.charset.Charset;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT builder for Twilio auth tokens.
 */
public abstract class Jwt {

    private static final Charset UTF_8 = Charset.forName("UTF-8");

    private final SignatureAlgorithm algorithm;
    private final String secret;
    private final String issuer;
    private final Date expiration;

    /**
     * Create a new JWT.
     *
     * @param algorithm algorithm to use
     * @param secret secret key
     * @param issuer JWT issuer
     * @param expiration expiration Date
     */
    public Jwt(
        SignatureAlgorithm algorithm,
        String secret,
        String issuer,
        Date expiration
    ) {
        this.algorithm = algorithm;
        this.secret = secret;
        this.issuer = issuer;
        this.expiration = expiration;
    }

    /**
     * Encode a JWT.
     *
     * @return encoded JWT
     */
    public String toJwt() {
        Map<String, Object> headers = new HashMap<>();
        headers.put("typ", "JWT");
        headers.putAll(this.getHeaders());

        JwtBuilder builder =
            Jwts.builder()
                .signWith(this.algorithm, this.secret.getBytes(UTF_8))
                .setHeaderParams(headers)
                .setIssuer(this.issuer)
                .setExpiration(expiration)
                .setClaims(this.getClaims());

        if (this.getId() != null) {
            builder.setId(this.getId());
        }

        if (this.getSubject() != null) {
            builder.setSubject(this.getSubject());
        }

        if (this.getNbf() != null) {
            builder.setNotBefore(this.getNbf());
        }

        return builder.compact();
    }

    public String getId() {
        return null;
    }

    public String getSubject() {
        return null;
    }

    public Date getNbf() {
        return null;
    }

    public abstract Map<String, Object> getHeaders();

    public abstract Map<String, Object> getClaims();
}