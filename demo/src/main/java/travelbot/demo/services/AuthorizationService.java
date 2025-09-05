package travelbot.demo.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import travelbot.demo.entities.User;
import travelbot.demo.exceptions.UnauthorizedException;
import travelbot.demo.payloads.LoginRequestDTO;
import travelbot.demo.tools.JWTTools;

@Service
public class AuthorizationService {

    @Autowired
    private UserService userService;
    @Autowired
    private JWTTools jwtTools;
    @Autowired
    private PasswordEncoder bcrypt;


    public String checkCredentialsAndGenerateToken(LoginRequestDTO body) {

        User found = this.userService.findByEmail(body.email());


        if (bcrypt.matches(body.password(), found.getPassword())) {

            String accessToken = jwtTools.createToken(found);

            return accessToken;
        } else {
            throw new UnauthorizedException("Incorrect credentials!");
        }
    }


}
