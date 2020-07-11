package com.example.user.config.security.filters;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.user.config.security.JWTSecurityConstants;
import com.example.user.config.security.SecurityMember;
import com.example.user.entity.Account;
import com.example.user.entity.Member;
import com.example.user.exception.InvalidPayloadException;
import com.example.user.exception.SignUpFailedException;
import com.example.user.payload.SignUpUser;
import com.example.user.service.AccountService;
import com.example.user.service.MemberService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.Date;

public class JwtAuthenticationSignUpFilter extends AbstractAuthenticationProcessingFilter {
    @Autowired
    private MemberService memberService;

    @Autowired
    private AccountService accountService;

    @Autowired
    public JwtAuthenticationSignUpFilter(AuthenticationManager authenticationManager) {
        super(new AntPathRequestMatcher("/signup"));
        setAuthenticationManager(authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req,
                                                HttpServletResponse res) throws AuthenticationException, IOException {
        SignUpUser model = null;

        try {
            model = new ObjectMapper().readValue(req.getInputStream(), SignUpUser.class);
        } catch(JsonParseException | JsonMappingException e) {
            throw new SignUpFailedException("Invalid signup payload data.", e);
        }

        if(!model.validateNullCheck()) {
            throw new SignUpFailedException("Invalid signup payload data (null or empty string delivered.)", null);
        }

        // save Member DB
        try {
            Member member = memberService.signUp(model);
            accountService.save(Account.builder().id(member.getId()).build());
        } catch(InvalidPayloadException e) {
            throw new SignUpFailedException(e.getLocalizedMessage(), e);
        }

        // Authenticate user
        return getAuthenticationManager().authenticate(
                // Create login token
                new UsernamePasswordAuthenticationToken(
                        model.getEmail(),
                        model.getPassword(),
                        Collections.emptyList()
                )
        );
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req,
                                            HttpServletResponse res,
                                            FilterChain chain,
                                            Authentication auth) throws IOException, ServletException {
        // setting JWT at response header
        SecurityMember member = (SecurityMember)auth.getPrincipal();

        long current = System.currentTimeMillis();
        Date issuedAt = new Date(current);
        Date expiredAt = new Date(current + JWTSecurityConstants.EXPIRATION_TIME);

        String token = JWT.create()
                .withIssuer("MyCoupon")
                .withAudience(Long.toString(member.getId()))
                .withSubject(member.getUsername())
                .withIssuedAt(issuedAt)
                .withExpiresAt(expiredAt)
                .sign(Algorithm.HMAC512(JWTSecurityConstants.SECRET.getBytes()));

        res.addHeader(JWTSecurityConstants.HEADER_STRING, JWTSecurityConstants.TOKEN_PREFIX + token);
        res.setStatus(HttpStatus.CREATED.value());
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
                                              HttpServletResponse response, AuthenticationException failed)
            throws IOException, ServletException {

        int statusCode;
        if(failed instanceof SignUpFailedException) {
            statusCode = HttpStatus.BAD_REQUEST.value();
        } else {
            statusCode = HttpStatus.UNAUTHORIZED.value();
        }

        response.setStatus(statusCode);
        PrintWriter writer = response.getWriter();
        writer.println(failed.getLocalizedMessage());
    }
}
