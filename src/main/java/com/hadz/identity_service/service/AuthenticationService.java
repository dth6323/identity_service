package com.hadz.identity_service.service;

import com.hadz.identity_service.dto.request.AuthenticationRequest;
import com.hadz.identity_service.dto.request.IntrospectRequest;
import com.hadz.identity_service.dto.response.AuthenticationResponse;
import com.hadz.identity_service.dto.response.IntrospectResponse;
import com.hadz.identity_service.exception.AppException;
import com.hadz.identity_service.exception.ErrorCode;
import com.hadz.identity_service.repository.UserRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {
    UserRepository userRepository;
    @NonFinal
    @Value("${spring.jwt.signerKey}")
    String SIGNER_KEY ;
    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {
        var user = userRepository.findByUsername(authenticationRequest.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        boolean authentication = passwordEncoder.matches(authenticationRequest.getPassword(), user.getPassword());
        if (!authentication) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        var token = generateToken(authenticationRequest.getUsername());
        return AuthenticationResponse.builder()
                .token(token)
                .authenticated(true)
                .build();
    }
    public IntrospectResponse introspect(IntrospectRequest r) throws JOSEException {
        var token = r.getToken();
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            var verified = signedJWT.verify(verifier);
            Date expiration = signedJWT.getJWTClaimsSet().getExpirationTime();
            return IntrospectResponse.builder()
                    .valid(verified && expiration.after(new Date()))
                    .build();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
    private String generateToken(String username){
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
        JWTClaimsSet jwtClamsets = new JWTClaimsSet.Builder()
                .subject(username)
                .issuer("hadz.com")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()
                ))
                .claim("customClaim","Custom")
                .build();
        Payload payload = new Payload(jwtClamsets.toJSONObject());
        JWSObject jwsObject = new JWSObject(header, payload);
        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("cannot create token");
            throw new RuntimeException(e);
        }
    }
}
