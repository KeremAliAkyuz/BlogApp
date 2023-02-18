package dev.aliak.blogapp.services;

import dev.aliak.blogapp.entity.Role;
import dev.aliak.blogapp.entity.User;
import dev.aliak.blogapp.exception.BlogAPIException;
import dev.aliak.blogapp.payload.LoginDTO;
import dev.aliak.blogapp.payload.RegisterDTO;
import dev.aliak.blogapp.repositories.RoleRepository;
import dev.aliak.blogapp.repositories.UserRepository;
import dev.aliak.blogapp.security.JWTTokenProvider;
import dev.aliak.blogapp.services.serviceInterfaces.AuthServiceInterface;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class AuthService implements AuthServiceInterface {
    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private JWTTokenProvider jwtTokenProvider;


    public AuthService(AuthenticationManager authenticationManager,
                       UserRepository userRepository,
                       RoleRepository roleRepository,
                       PasswordEncoder passwordEncoder,
                       JWTTokenProvider jwtTokenProvider
    ) {
        this.jwtTokenProvider=jwtTokenProvider;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public String login(LoginDTO loginDTO) {

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDTO.getUsernameOrEmail(),
                loginDTO.getPassword()
        ));//parametre olarak authentication objesi alır


        SecurityContextHolder.getContext().setAuthentication(authentication);
        //oluşturduğumuz auth objesini security context holder'a kaydettik

        String token = jwtTokenProvider.generateToken(authentication);



        return token;
    }

    @Override
    public String register(RegisterDTO registerDTO) {

        //add check for username exists in database
        if(userRepository.existsByUsername(registerDTO.getUsername())){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST,"Username is already exists!.");
        }
        if(userRepository.existsByEmail(registerDTO.getEmail())){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST,"Email is already exists!.");
        }

        User user = new User();
        user.setName(registerDTO.getName());
        user.setUsername(registerDTO.getUsername());
        user.setEmail(registerDTO.getEmail());
        user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));

        Set<Role> roles =new HashSet<>();
        Role userRole = roleRepository.findByName("ROLE_USER").get();
        roles.add(userRole);
        user.setRoles(roles);

        userRepository.save(user);


        return "User registered successfully!.";
    }
}
