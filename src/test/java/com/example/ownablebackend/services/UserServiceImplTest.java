package com.example.ownablebackend.services;

import com.example.ownablebackend.api.request.SignupRequest;
import com.example.ownablebackend.api.response.MessageResponse;
import com.example.ownablebackend.domain.User;
import com.example.ownablebackend.services.impl.UserServiceImpl;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
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
