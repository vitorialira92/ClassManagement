package com.liraz.classmanagement.controllers.auth;

import com.liraz.classmanagement.configuration.TokenService;
import com.liraz.classmanagement.domain.user.UserModel;
import com.liraz.classmanagement.domain.user.UserRole;
import com.liraz.classmanagement.dtos.auth.LoginResponseDTO;
import com.liraz.classmanagement.dtos.auth.UserRequestDTO;
import com.liraz.classmanagement.exceptions.AuthenticationCustomizedException;
import com.liraz.classmanagement.repositories.StudentRepository;
import com.liraz.classmanagement.repositories.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@Tag(name = "Authentication management")
@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserRepository repository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private TokenService tokenService;

    @Operation(summary = "Login an user", responses = {
            @ApiResponse(responseCode = "200",
                    description = "User's token", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "403",
                    description = "Credentials do not match")
    })
    @PostMapping("/login") // ok
    public ResponseEntity login(@RequestBody @Valid UserRequestDTO userRequestDTO){

        if(repository.findByLogin(userRequestDTO.login()) == null)
            return ResponseEntity.badRequest().build();

        var usernamePassword = new UsernamePasswordAuthenticationToken(
                userRequestDTO.login(), userRequestDTO.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);
        var token = tokenService.generateToken((UserModel) auth.getPrincipal());

        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    @Operation(summary = "Register an ADMIN", responses = {
            @ApiResponse(responseCode = "200",
                    description = "Admin registered successfully"),
            @ApiResponse(responseCode = "400",
                    description = "An admin login must be at least " +
                            "8 characters long and have at least 1 letter OR Admin login already registered ",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AuthenticationCustomizedException.class)))
    })
    @PostMapping("/register/admin") //ok
    public ResponseEntity<?> registerAdmin(@RequestBody @Valid UserRequestDTO userRequestDTO) throws InstantiationException, IllegalAccessException {

        if(userRequestDTO.login().length() < 8 || !userRequestDTO.login().matches(".*[a-zA-Z].*")){
            throw new AuthenticationCustomizedException(
                            "An admin login must be at least 8 characters long and have at least 1 letter");
        }

        if(repository.findByLogin(userRequestDTO.login()) != null){
            throw new AuthenticationCustomizedException(
                            "Admin login already registered.");
        }

        String encryptedPassword = new BCryptPasswordEncoder().encode(userRequestDTO.password());

        UserModel newUser = new UserModel(userRequestDTO.login(), encryptedPassword,UserRole.ADMIN);

        this.repository.save(newUser);

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Register a student's login information", responses = {
            @ApiResponse(responseCode = "200",
                    description = "Student registered successfully"),
            @ApiResponse(responseCode = "400",
                    description = "A student's login must be their registration number OR " +
                            "Student wasn't previously registered. An admin must register a student before they can create a login " +
                            "OR User's login was already registered.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AuthenticationCustomizedException.class)))
    })
    @PostMapping("/register/student") // ok
    public ResponseEntity<?> registerStudent(@RequestBody @Valid UserRequestDTO userRequestDTO) throws InstantiationException, IllegalAccessException {

        if(!isStringAnInteger(userRequestDTO.login())){
            throw new AuthenticationCustomizedException(
                            "A student's login must be their registration number.");
        }


        if(studentRepository.findByRegistration(Integer.parseInt(userRequestDTO.login())) == null){
            throw new AuthenticationCustomizedException(
                            "Student wasn't previously registered. " +
                                    "An admin must register a student before they can create a login");
        }

        if(repository.findByLogin(userRequestDTO.login()) != null){
            throw new AuthenticationCustomizedException(
                            "User's login was already registered.");
        }
        String encryptedPassword = new BCryptPasswordEncoder().encode(userRequestDTO.password());

        UserModel newUser = new UserModel(userRequestDTO.login(),
                encryptedPassword, UserRole.USER);

        this.repository.save(newUser);

        return ResponseEntity.ok().build();
    }


    @Operation(summary = "Delete an user's login information", responses = {
            @ApiResponse(responseCode = "200",
                    description = "Login information deleted"),
            @ApiResponse(responseCode = "400",
                    description = "User not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AuthenticationCustomizedException.class)))
    })
    @DeleteMapping("/delete") // ok
    public ResponseEntity<?> delete(@RequestBody @Valid UserRequestDTO userRequestDTO){
        UserModel userModel = this.repository.findByLogin(userRequestDTO.login());
        if(userModel == null)
            throw new AuthenticationCustomizedException(
                    "User not found");

        repository.delete(userModel);

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Update an user's login information", responses = {
            @ApiResponse(responseCode = "200",
                    description = "Login information updated"),
            @ApiResponse(responseCode = "400",
                    description = "You cannot change a student's login OR Username in use " +
                            "OR ser not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AuthenticationCustomizedException.class)))
    })
    @PutMapping("/update/{login}") //ok
    public ResponseEntity<?> updateUser(@RequestBody @Valid UserRequestDTO userRequestDTO, @PathVariable String login){
        UserModel user = repository.findByLogin(login);
        if(user != null){
            if(user.getUserRole() == UserRole.USER && !Objects.equals(login, userRequestDTO.login())){
                throw new AuthenticationCustomizedException(
                                "You cannot change a student's login.");
            }

            if(repository.findByLogin(userRequestDTO.login()) != null){
                throw new AuthenticationCustomizedException(
                                "Username in use.");
            }

            String encryptedPassword = new BCryptPasswordEncoder().encode(userRequestDTO.password());

            UserModel newUser = new UserModel(userRequestDTO.login(), encryptedPassword, user.getUserRole());

            this.repository.save(newUser);

            return ResponseEntity.ok().build();
        }else{
            throw new AuthenticationCustomizedException(
                    "User not found.");
        }
    }
    private static boolean isStringAnInteger(String str) {
        try {
            double number = Double.parseDouble(str);
            return number == (int) number;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
