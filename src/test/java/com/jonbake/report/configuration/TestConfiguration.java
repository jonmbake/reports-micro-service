/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jonbake.report.configuration;

import com.jonbake.report.filters.AuthFilter;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;
import java.security.Key;
import java.util.Optional;

/**
 * Configuration implementation provided for tests.  Private key is generated and method is added to get user token.
 *
 * @author jonmbake
 */
public class TestConfiguration implements Configuration {
    public Key PRIVATE_KEY = MacProvider.generateKey();

    @Override
    public String getReportsDirectory() {
        return this.getClass().getClassLoader().getResource("reports/").getPath();
    }

    @Override
    public String getJWTSecret() {
        return AuthFilter.DECODER.encodeAsString(PRIVATE_KEY.getEncoded());
    }

    @Override
    public Optional<String> getJWTAudience() {
        return Optional.empty();
    }

    @Override
    public Optional<String> getJWTIssuer() {
        return Optional.empty();
    }

    @Override
    public Optional<String> getJDBCConnectionString() {
        return Optional.empty();
    }

    public String getTestUserToken () {
        return Jwts.builder().setSubject("Test User").signWith(SignatureAlgorithm.HS512, PRIVATE_KEY).compact();
    }
}
