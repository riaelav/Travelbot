package travelbot.demo.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import travelbot.demo.entities.User;
import travelbot.demo.exceptions.ValidationException;
import travelbot.demo.payloads.LoginRequestDTO;
import travelbot.demo.payloads.LoginRespDTO;
import travelbot.demo.payloads.UserRegistrationDTO;
import travelbot.demo.payloads.UserResponseDTO;
import travelbot.demo.services.AuthorizationService;
import travelbot.demo.services.UserService;

@RestController
@RequestMapping("/auth")
public class AuthorizationController {

    @Autowired
    private UserService userService;
    @Autowired
    private AuthorizationService authorizationService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDTO registerUser(@RequestBody @Validated UserRegistrationDTO payload,
                                        BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            throw new ValidationException(validationResult.getFieldErrors()
                    .stream().map(field -> field.getDefaultMessage()).toList());
        }
        User user = userService.register(payload);
        return new UserResponseDTO(user.getId(), user.getUsername(), user.getEmail());
    }

    @PostMapping("/login")
    public LoginRespDTO login(@RequestBody @Validated LoginRequestDTO payload,
                              BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            throw new ValidationException(validationResult.getFieldErrors()
                    .stream().map(field -> field.getDefaultMessage()).toList());
        }
        String accessToken = authorizationService.checkCredentialsAndGenerateToken(payload);
        return new LoginRespDTO(accessToken);
    }
}
