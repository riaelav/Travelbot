package travelbot.demo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        //disabilito il form di login
        httpSecurity.formLogin(formLogin -> formLogin.disable());
        //disabilito la protezione da CSRF
        httpSecurity.csrf(csrf -> csrf.disable());
        //disabilito le sessioni
        httpSecurity.sessionManagement(sessions ->
                sessions.sessionCreationPolicy(SessionCreationPolicy.STATELESS));


//tolgo la protezione a tutto. gli endpoint protetti saranno custom
        httpSecurity.authorizeHttpRequests(h ->
                h.requestMatchers("/**").permitAll());


        return httpSecurity.build();

    }


    @Bean
    public PasswordEncoder getBCrypt() {
        return new BCryptPasswordEncoder(12);
    }

}
