package com.example.ownablebackend.services.impl;

import com.example.ownablebackend.api.request.LoginRequest;
import com.example.ownablebackend.api.request.SignupRequest;
import com.example.ownablebackend.api.response.JwtResponse;
import com.example.ownablebackend.api.response.MessageResponse;
import com.example.ownablebackend.config.security.jwt.JwtUtils;

import com.example.ownablebackend.config.security.services.UserDetailsImpl;
import com.example.ownablebackend.constant.GlobalConstant;
import com.example.ownablebackend.domain.User;
import com.example.ownablebackend.domain.UserRole;
import com.example.ownablebackend.domain.enumeration.UserRoles;
import com.example.ownablebackend.dto.mailservice.EmailDetails;
import com.example.ownablebackend.exception.FormatterException;
import com.example.ownablebackend.exception.NotActivatedUserException;
import com.example.ownablebackend.exception.PasswordException;
import com.example.ownablebackend.repositories.RoleRepository;
import com.example.ownablebackend.repositories.UserRepository;
import com.example.ownablebackend.services.UserService;
import com.example.ownablebackend.services.email.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;


import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.HashSet;
import java.util.List;

import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private static final String REGEX_PATTERN = "^(.+)@(\\S+)$";

    private final AuthenticationManager authenticationManager;

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final EmailService emailService;

    private final PasswordEncoder encoder;

    private final JwtUtils jwtUtils;

    public UserServiceImpl(AuthenticationManager authenticationManager,
                            UserRepository userRepository,
                            RoleRepository roleRepository,
                            JwtUtils jwtUtils,
                           EmailService emailService,
                           PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.jwtUtils = jwtUtils;
        this.emailService = emailService;
        this.encoder = passwordEncoder;

    }

    @Override
    public MessageResponse saveUser(SignupRequest signupRequest) {

        this.patternMatches(signupRequest.getEmail());
        this.checksBothPasswordAndConfirmSame(signupRequest.getPassword(), signupRequest.getConfirmPassword());

        var userName = signupRequest.getEmail();

        boolean existResponse = userRepository.existsByUserName(userName);
        if (existResponse) {
            return new MessageResponse(GlobalConstant.EXIST_EMAIL);
        }

        // Create new user's account
        User user = new User(
                userName,
                encoder.encode(signupRequest.getPassword()),
                signupRequest.getEmail(),
                signupRequest.getFirstName(),
                signupRequest.getLastName(),
                false
        );

        Set<String> strRoles = signupRequest.getRole();
        Set<UserRole> roles = new HashSet<>();

        if (strRoles == null) {
            UserRole userRole = roleRepository.findByName(UserRoles.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException(GlobalConstant.NOT_FOUND_ROL));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        UserRole adminRole = roleRepository.findByName(UserRoles.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException(GlobalConstant.NOT_FOUND_ROL));
                        roles.add(adminRole);

                        break;
                    case "mod":
                        UserRole modRole = roleRepository.findByName(UserRoles.ROLE_MODERATOR)
                                .orElseThrow(() -> new RuntimeException(GlobalConstant.NOT_FOUND_ROL));
                        roles.add(modRole);

                        break;
                    default:
                        UserRole userRole = roleRepository.findByName(UserRoles.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException(GlobalConstant.NOT_FOUND_ROL));
                        roles.add(userRole);
                }
            });
        }

        user.setRoles(roles);
        userRepository.save(user);

        emailService.sendSimpleMail(prepareMailDetail(user.getEmail()));
    return new MessageResponse("User registered successfully!");
    }

    private EmailDetails prepareMailDetail(String recipient) {
        EmailDetails emailDetails = new EmailDetails();
        emailDetails.setRecipient(recipient);
        emailDetails.setSubject("Welcome To The Ownable-NS");
        emailDetails.setMsgBody("Hey! "+ recipient + "You're the new one for us. We're excited to meet with you.");

        return emailDetails;
    }

    private void checksBothPasswordAndConfirmSame(String password, String confirmPassword) {
        if(!password.equals(confirmPassword)) {
            throw new PasswordException("The Password And Confirm Password must be same.");
        }
    }

    @Override
    public JwtResponse authenticateUser(LoginRequest loginRequest) {
        this.patternMatches(loginRequest.getEmail());

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        this.checkUserStatus(loginRequest.getEmail());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles);
    }

    private void checkUserStatus(String userName) {
        var user = userRepository.findByUserName(userName);

        if(!user.get().isActive()) {
            throw new NotActivatedUserException(userName);
        }
    }


    private void patternMatches(String emailAddress) {
        var response = Pattern.compile(REGEX_PATTERN)
                .matcher(emailAddress)
                .matches();

        if(!response) {
            throw new FormatterException(emailAddress);
        }
    }
}
