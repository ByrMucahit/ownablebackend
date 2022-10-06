package com.example.ownablebackend.api;

import com.example.ownablebackend.api.request.LoginRequest;
import com.example.ownablebackend.api.request.SignupRequest;
import com.example.ownablebackend.api.response.JwtResponse;
import com.example.ownablebackend.api.response.MessageResponse;
import com.example.ownablebackend.domain.User;
import com.example.ownablebackend.services.dto.GenericResponse;
import com.example.ownablebackend.services.security.SecurityService;
import com.example.ownablebackend.services.user.UserService;
import com.example.ownablebackend.api.dto.PasswordDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/api/auth")
@Slf4j
@RequiredArgsConstructor
public class AuthResource {

   private final UserService userService;
   private final SecurityService securityService;
   private final MessageSource messages;

    @PostMapping("/signin")
    public ResponseEntity<JwtResponse> authenticateUser(@Validated @RequestBody LoginRequest loginRequest) {
        log.info("The REST API to sign in " + loginRequest);
        return ResponseEntity.ok(userService.authenticateUser(loginRequest));
    }

    @PostMapping("/signup")
    public ResponseEntity<MessageResponse> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        var response = userService.saveUser(signUpRequest);

        if(!Objects.isNull(response)) {
            return ResponseEntity
                    .badRequest()
                    .body(response);
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/resetpassword")
    public ResponseEntity<GenericResponse> resetPassword(@RequestParam(value = "mail") String userEmail) {
      return ResponseEntity.ok(userService.resetPassword(userEmail)) ;
    }

    @GetMapping("/changepassword")
    public String showChangePasswordPage(Locale locale, Model model,
                                         @RequestParam("token") String token) {
        String result = securityService.validatePasswordResetToken(token);
        if(result != null) {
            String message = messages.getMessage("auth.message." + result, null, locale);
            return "redirect:/login.html?lang="
                    + locale.getLanguage() + "&message=" + message;
        } else {
            model.addAttribute("token", token);
            return "redirect:/updatePassword.html?lang=" + locale.getLanguage();
        }
    }

    @PostMapping("/user/savePassword")
    public GenericResponse savePassword(final Locale locale, @Valid PasswordDTO passwordDTO) {

        String result = securityService.validatePasswordResetToken(passwordDTO.getToken());

        if(result != null) {
            return new GenericResponse(messages.getMessage(
                    "auth.message." + result, null, locale));
        }

        Optional<User> user = userService.getUserByPasswordResetToken(passwordDTO.getToken());
        if(user.isPresent()) {
            userService.changeUserPassword(user.get(), passwordDTO.getNewPassword());
            return new GenericResponse(messages.getMessage(
                    "message.resetPasswordSuc", null, locale));
        } else {
            return new GenericResponse(messages.getMessage(
                    "auth.message.invalid", null, locale));
        }
    }

}
