package com.example.ownablebackend.services;

import com.example.ownablebackend.api.request.SignupRequest;
import com.example.ownablebackend.api.response.MessageResponse;
import com.example.ownablebackend.services.user.UserService;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    private static final String SIGNUP_RESPONSE = "User registered successfully!";
    @MockBean
    UserService userService;

    @Before
    public void init() {
        MockitoAnnotations.openMocks(this);
    }



    @Test
    public void it_should_signup_then_should_success() {
        //assert
        SignupRequest user = prepareUser();
       when(userService.saveUser(user)).thenReturn(new MessageResponse(SIGNUP_RESPONSE));


    }

    private SignupRequest prepareUser() {
        SignupRequest request = new SignupRequest();
        request.setEmail("example@example.com");
        request.setFirstName("example firstname");
        request.setLastName("example lastname");
        request.setPassword("examplepass");
        request.setConfirmPassword("examplepass");
        return request;
    }
}
