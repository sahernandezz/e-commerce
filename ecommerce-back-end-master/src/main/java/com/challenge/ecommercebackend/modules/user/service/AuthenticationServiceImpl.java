package com.challenge.ecommercebackend.modules.user.service;

import com.challenge.ecommercebackend.config.redis.RedisTokenService;
import com.challenge.ecommercebackend.email.IEmailSenderService;
import com.challenge.ecommercebackend.jwt.JwtUtils;
import com.challenge.ecommercebackend.modules.user.persisten.entity.Role;
import com.challenge.ecommercebackend.modules.user.persisten.entity.User;
import com.challenge.ecommercebackend.modules.user.persisten.entity.UserStatus;
import com.challenge.ecommercebackend.modules.user.persisten.repository.command.IRoleCommandRepository;
import com.challenge.ecommercebackend.modules.user.persisten.repository.command.IUserCommandRepository;
import com.challenge.ecommercebackend.modules.user.web.dto.request.AuthRequest;
import com.challenge.ecommercebackend.modules.user.web.dto.request.RegisterInput;
import com.challenge.ecommercebackend.modules.user.web.dto.response.AuthenticationResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@Transactional
public class AuthenticationServiceImpl implements IAuthenticationService {

    private final IUserCommandRepository userCommandRepository;
    private final RedisTokenService redisTokenService;
    private final IRoleCommandRepository roleCommandRepository;
    private final JwtUtils jwtUtils;
    private final IEmailSenderService mailService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Value("${auth.user.code_length}")
    private Integer codeLength;

    @Value("${auth.user.time_recovery_code}")
    private Integer time;

    public AuthenticationServiceImpl(
            IUserCommandRepository userCommandRepository,
            RedisTokenService redisTokenService,
            IRoleCommandRepository roleCommandRepository,
            JwtUtils jwtUtils,
            IEmailSenderService mailService,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager) {
        this.userCommandRepository = userCommandRepository;
        this.redisTokenService = redisTokenService;
        this.roleCommandRepository = roleCommandRepository;
        this.jwtUtils = jwtUtils;
        this.mailService = mailService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Optional<AuthenticationResponse> authenticate(AuthRequest request) {
        Optional<AuthenticationResponse> response;
        try {
            Optional<User> user = userCommandRepository.findByEmail(request.getEmail());
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

            if (!authentication.isAuthenticated() || user.isEmpty()) {
                response = Optional.empty();
            } else {
                String token = jwtUtils.generateToken(user.get());
                response = Optional.of(AuthenticationResponse.builder()
                        .message("Autenticación exitosa")
                        .token(token)
                        .user(user.get())
                        .build());
                revokeAllUserTokens(user.get());
                saveUserToken(user.get(), token);
            }
        } catch (Exception e) {
            response = Optional.empty();
        }
        return response;
    }

    @Override
    public AuthenticationResponse register(RegisterInput request) {
        // Verificar si el email ya existe
        if (userCommandRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("El email ya está registrado");
        }

        // Obtener el rol USER por defecto
        Role userRole = roleCommandRepository.findByName(Role.USER)
                .orElseThrow(() -> new RuntimeException("Rol USER no encontrado"));

        // Crear el nuevo usuario con un solo rol
        User newUser = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .status(UserStatus.ACTIVE)
                .role(userRole)
                .build();

        User savedUser = userCommandRepository.save(newUser);

        // Generar token JWT
        String token = jwtUtils.generateToken(savedUser);
        saveUserToken(savedUser, token);

        return AuthenticationResponse.builder()
                .message("Registro exitoso")
                .token(token)
                .user(savedUser)
                .build();
    }

    public String recoverPassword(String correo, HttpServletRequest request) {
        Optional<User> user = userCommandRepository.findByEmail(correo);
        if (user.isPresent() && user.get().isEnabled()) {
            sendRecoveryCode(user.get(), request);
        }
        return "Se ha enviado un código de recuperación a su correo electrónico";
    }

    public Optional<User> verificateRecoveryCode(String recoveryCode, Long id) {
        Optional<User> result = Optional.empty();
        Optional<User> user = userCommandRepository.findById(id);
        if (user.isPresent() && user.get().isEnabled() && user.get().getRecoveryDate() != null
                && user.get().getRecoveryCode() != null) {
            if (passwordEncoder.matches(recoveryCode, user.get().getRecoveryCode())
                    && !isDateExpired(user.get().getRecoveryDate())) {
                result = user;
            }
            if (isDateExpired(user.get().getRecoveryDate())) {
                removeCodeRecovery(user.get());
                result = Optional.empty();
            }
        }
        return result;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public Optional<User> changePassword(String recoveryCode, Long id, String clave) {
        Optional<User> user = verificateRecoveryCode(recoveryCode, id);
        if (user.isPresent()) {
            user.get().setPassword(passwordEncoder.encode(clave));
            user.get().setRecoveryCode(null);
            user.get().setRecoveryDate(null);
            user = Optional.of(userCommandRepository.save(user.get()));
            user.ifPresent(this::revokeAllUserTokens);
        }
        user.ifPresent(usuario -> mailService.sendPasswordRecoveryConfirmation(usuario.getEmail()));
        return user;
    }

    private void sendRecoveryCode(User user, HttpServletRequest request) {
        if (time != null && time > 0) {
            String code = getRandomString(codeLength);
            user.setRecoveryCode(passwordEncoder.encode(code));
            user.setRecoveryDate(addMinutesCurrentDate(time));
            userCommandRepository.save(user);
            mailService.sendRecoveryCode(user.getEmail(), user.getId(), code, request, time);
        }
    }

    private void saveUserToken(User user, String jwtToken) {
        redisTokenService.saveToken(user, jwtToken);
    }

    private void removeCodeRecovery(User user) {
        user.setRecoveryCode(null);
        user.setRecoveryDate(null);
        userCommandRepository.save(user);
    }

    private void revokeAllUserTokens(User user) {
        redisTokenService.revokeAllUserTokens(user.getId());
    }

    private Date addMinutesCurrentDate(Integer minutes) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MINUTE, minutes);
        return calendar.getTime();
    }

    private Boolean isDateExpired(Date date) {
        return date == null || date.before(new Date());
    }

    private String getRandomString(Integer n) {
        String result = null;
        if (n != null && n > 0) {
            String uuid = UUID.randomUUID().toString();
            result = IntStream.range(0, n)
                    .map(i -> ThreadLocalRandom.current().nextInt(0, uuid.getBytes(StandardCharsets.UTF_8).length))
                    .mapToObj(index -> String.valueOf(uuid.charAt(index)))
                    .collect(Collectors.joining());
        }
        return result;
    }
}

