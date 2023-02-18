package dev.aliak.blogapp.services.serviceInterfaces;

import dev.aliak.blogapp.payload.LoginDTO;
import dev.aliak.blogapp.payload.RegisterDTO;

public interface AuthServiceInterface {
    String login(LoginDTO loginDTO);

    String register(RegisterDTO registerDTO);
}
