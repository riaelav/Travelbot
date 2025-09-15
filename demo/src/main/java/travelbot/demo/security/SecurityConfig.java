package travelbot.demo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

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
        //abilito il CORS
        httpSecurity.cors(Customizer.withDefaults());


//tolgo la protezione a tutto. gli endpoint protetti saranno custom
        httpSecurity.authorizeHttpRequests(h ->
                h.requestMatchers("/**").permitAll());


        return httpSecurity.build();

    }


    @Bean
    public PasswordEncoder getBCrypt() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration c = new CorsConfiguration();
        c.setAllowedOrigins(List.of("http://localhost:5173")); // frontend dev
        c.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        c.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        c.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", c);
        return source;
    }
}
