package com.challenge.ecommercebackend.modules.user.web.controller;

import com.challenge.ecommercebackend.modules.user.service.IAuthenticationService;
import com.challenge.ecommercebackend.modules.user.web.dto.request.AuthRequest;
import com.challenge.ecommercebackend.modules.user.web.dto.request.VerificateCode;
import com.challenge.ecommercebackend.modules.user.web.dto.response.AuthenticationResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationRest {

    private final IAuthenticationService authenticationService;

    public AuthenticationRest(IAuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(@RequestBody @Valid AuthRequest request) {
        Optional<AuthenticationResponse> response = this.authenticationService.authenticate(request);
        return response.isPresent()
                ? ResponseEntity.ok(response.get())
                : ResponseEntity.badRequest().body("Usuario o contraseña incorrectos");
    }

    @PostMapping("/recover-password")
    public ResponseEntity<String> recoverPassword(@RequestParam String email, final HttpServletRequest request) {
        return ResponseEntity.ok(authenticationService.recoverPassword(email, request));
    }

    @PostMapping("/verificate-code-recovery")
    public ResponseEntity<String> verifyCodeRecovery(@RequestBody @Valid VerificateCode code) {
        return this.authenticationService.verificateRecoveryCode(code.getCode(), code.getId()).isPresent() ?
                ResponseEntity.ok("Código verificado")
                : ResponseEntity.badRequest().body("El código no coincide o ya expiró");
    }

    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestBody @Valid VerificateCode code) {
        return this.authenticationService.changePassword(code.getCode(), code.getId(), code.getPassword()).isPresent() ?
                ResponseEntity.ok("Contraseña actualizada")
                : ResponseEntity.badRequest().body("No se pudo hacer el cambio de contraseña por favor intente de nuevo mas tarde");
    }
}
