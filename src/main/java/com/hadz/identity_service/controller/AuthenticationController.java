package com.hadz.identity_service.controller;

import com.hadz.identity_service.dto.request.ApiResponse;
import com.hadz.identity_service.dto.request.AuthenticationRequest;
import com.hadz.identity_service.dto.request.IntrospectRequest;
import com.hadz.identity_service.dto.response.AuthenticationResponse;
import com.hadz.identity_service.dto.response.IntrospectResponse;
import com.hadz.identity_service.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
    AuthenticationService authenticationService;
    @PostMapping("/token")
    ApiResponse<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request){
        var result = authenticationService.authenticate(request);
        return ApiResponse.<AuthenticationResponse>builder().
                result(result).
                build();
    }
    @PostMapping("/introspects")
    ApiResponse<IntrospectResponse> introspects(@RequestBody IntrospectRequest request) throws JOSEException {
        var result = authenticationService.introspect(request);
        return ApiResponse.<IntrospectResponse>builder().
                result(result).
                build();
    }
}
