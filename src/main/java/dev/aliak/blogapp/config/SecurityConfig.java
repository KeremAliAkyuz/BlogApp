package dev.aliak.blogapp.config;

import dev.aliak.blogapp.security.JWTAuthenticationEntryPoint;
import dev.aliak.blogapp.security.JWTAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration//we can define all the spring bean definitions
@EnableMethodSecurity//in order to enable method level security,we have to annotate this class with annotation
public class SecurityConfig {

    private JWTAuthenticationEntryPoint authenticationEntryPoint;
    private UserDetailsService userDetailsService;
    private JWTAuthenticationFilter authenticationFilter;

    public SecurityConfig(UserDetailsService userDetailsService,
                          JWTAuthenticationEntryPoint authenticationEntryPoint,
                          JWTAuthenticationFilter authenticationFilter){
        this.authenticationFilter=authenticationFilter;
        this.userDetailsService=userDetailsService;
        this.authenticationEntryPoint=authenticationEntryPoint;
    }

    //AuthenticationManager will use this UserDetailService to get user from the db.
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
    @Bean
    public static PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf().disable()
                .authorizeHttpRequests((authorize) ->
                                authorize.requestMatchers(HttpMethod.GET,"/api/v1/**").permitAll()//we are providing permission for all the users to access GET endpoints
                                        .requestMatchers("/api/v1/auth/**").permitAll()
                                        .anyRequest().authenticated()
                )
                        //authorize.anyRequest().authenticated() we are going to use to authorize all the incoming HTTP request to our application
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(authenticationEntryPoint)
                ).sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );

        http.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

//    @Bean
//    public UserDetailsService userDetailsService(){
//        UserDetails ali = User.builder()
//                .username("ali")
//                .password(passwordEncoder().encode("ali"))//this plain text password won't work in case of spring security,so use PasswordEncoder
//                .roles("USER")
//                .build();
//
//        UserDetails admin = User.builder()
//                .username("admin")
//                .password(passwordEncoder().encode("admin"))
//                .roles("ADMIN")
//                .build();
//        return new InMemoryUserDetailsManager(ali,admin);
//    }
}
