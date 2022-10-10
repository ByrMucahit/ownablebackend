package com.example.ownablebackend.services.user.impl;

import com.example.ownablebackend.api.request.LoginRequest;
import com.example.ownablebackend.api.request.SignupRequest;
import com.example.ownablebackend.api.response.JwtResponse;
import com.example.ownablebackend.api.response.MessageResponse;
import com.example.ownablebackend.config.security.jwt.JwtUtils;

import com.example.ownablebackend.config.security.services.UserDetailsImpl;
import com.example.ownablebackend.constant.GlobalConstant;
import com.example.ownablebackend.domain.PasswordResetToken;
import com.example.ownablebackend.domain.User;
import com.example.ownablebackend.domain.UserRole;
import com.example.ownablebackend.domain.enumeration.UserRoles;
import com.example.ownablebackend.dto.mailservice.EmailDetails;
import com.example.ownablebackend.exception.FormatterException;
import com.example.ownablebackend.exception.NotActivatedUserException;
import com.example.ownablebackend.exception.PasswordException;
import com.example.ownablebackend.repositories.PasswordTokenRepository;
import com.example.ownablebackend.repositories.RoleRepository;
import com.example.ownablebackend.repositories.UserRepository;
import com.example.ownablebackend.services.dto.GenericResponse;
import com.example.ownablebackend.services.user.UserService;
import com.example.ownablebackend.services.email.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;


import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.context.MessageSource;


import java.util.HashSet;
import java.util.List;

import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private static final String REGEX_PATTERN = "^(.+)@(\\S+)$";
    private static final boolean DEFAULT_VALIDATION = false;

    private final AuthenticationManager authenticationManager;

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final EmailService emailService;

    private final PasswordEncoder encoder;

    private final JwtUtils jwtUtils;

    private final PasswordTokenRepository passwordTokenRepository;

    private final MessageSource messages;

    @Value("${spring.mail.username}")
    private String from;

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

        emailService.sendActivationMail(user);
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

    @Override
    public GenericResponse resetPassword(String userEmail) {
        Optional<User> user = userRepository.findByUserName(userEmail);

        if(user.isEmpty()) {
            throw new UsernameNotFoundException("User Not Found");
        }

        String token = UUID.randomUUID().toString();

        createPasswordResetTokenForUser(user.get(), token);

        emailService.sendSimpleMail(prepareResetPasswordDetails(token, userEmail));

        return new GenericResponse(messages.getMessage("message.resetPasswordEmail", null, null));
    }

    private EmailDetails prepareResetPasswordDetails(String token, String userEmail) {
        EmailDetails emailDetails = new EmailDetails();
        emailDetails.setMsgBody(token);
        emailDetails.setRecipient(userEmail);
        emailDetails.setSubject("RESET PASSWORD");

        return emailDetails;
    }


    @Override
    public void changeUserPassword(User user, String password) {
        user.setPassword(encoder.encode(password));
        userRepository.save(user);
    }

    @Override
    public Optional<User> getUserByPasswordResetToken(String token) {

        return Optional.ofNullable(passwordTokenRepository.findByToken(token).get().getUser());
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

    private void createPasswordResetTokenForUser(User user, String token) {
        PasswordResetToken resetToken = new PasswordResetToken(token, user, DEFAULT_VALIDATION);
        passwordTokenRepository.save(resetToken);

    }

    private SimpleMailMessage constructRestTokenEmail(String contextPath, Locale locale, String token, User user) {
        String url = contextPath + "/user/changePassword?token=" + token;
        String message = messages.getMessage("message.resetPasswordEmail", null, locale);
        return constructEmail("Reset Password", message + " \r\n" + url, user);
    }

    private SimpleMailMessage constructEmail(String subject, String body,
                                             User user) {
        SimpleMailMessage email = new SimpleMailMessage();
        email.setSubject(subject);
        email.setText(body);
        email.setTo(user.getEmail());
        email.setFrom(from);
        return email;
    }
}
