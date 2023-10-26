package com.liraz.classmanagement.controllers.auth;

import com.liraz.classmanagement.configuration.TokenService;
import com.liraz.classmanagement.domain.user.UserModel;
import com.liraz.classmanagement.domain.user.UserRole;
import com.liraz.classmanagement.dtos.auth.LoginResponseDTO;
import com.liraz.classmanagement.dtos.auth.UserRequestDTO;
import com.liraz.classmanagement.exceptions.AuthenticationCustomizedException;
import com.liraz.classmanagement.repositories.StudentRepository;
import com.liraz.classmanagement.repositories.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;


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

    @PostMapping("/register/admin") //ok
    public ResponseEntity<?> registerAdmin(@RequestBody @Valid UserRequestDTO userRequestDTO) throws InstantiationException, IllegalAccessException {

        if(userRequestDTO.login().length() < 8 || !userRequestDTO.login().matches(".*[a-zA-Z].*")){
            return new ResponseEntity<AuthenticationCustomizedException>(
                    new AuthenticationCustomizedException(
                            "An admin login must be at least 8 characters long and have at least 1 letter."),
                    HttpStatus.BAD_REQUEST);
        }

        if(repository.findByLogin(userRequestDTO.login()) != null){
            return new ResponseEntity<AuthenticationCustomizedException>(
                    new AuthenticationCustomizedException(
                            "Admin login already registered."),
                    HttpStatus.BAD_REQUEST);
        }

        String encryptedPassword = new BCryptPasswordEncoder().encode(userRequestDTO.password());

        UserModel newUser = new UserModel(userRequestDTO.login(), encryptedPassword,UserRole.ADMIN);

        this.repository.save(newUser);

        return ResponseEntity.ok().build();
    }
    @PostMapping("/register/student") // ok
    public ResponseEntity<?> registerStudent(@RequestBody @Valid UserRequestDTO userRequestDTO) throws InstantiationException, IllegalAccessException {

        if(!isStringAnInteger(userRequestDTO.login())){
            return new ResponseEntity<AuthenticationCustomizedException>(
                    new AuthenticationCustomizedException(
                            "A student's login must be their registration number."),
                    HttpStatus.BAD_REQUEST);
        }


        if(studentRepository.findByRegistration(Integer.parseInt(userRequestDTO.login())) == null){
            return new ResponseEntity<AuthenticationCustomizedException>(
                    new AuthenticationCustomizedException(
                            "Student wasn't previously registered. " +
                                    "An admin must register a student before they can create a login"),
                    HttpStatus.BAD_REQUEST);
        }

        if(repository.findByLogin(userRequestDTO.login()) != null){
            return new ResponseEntity<AuthenticationCustomizedException>(
                    new AuthenticationCustomizedException(
                            "User's login was already registered."),
                    HttpStatus.BAD_REQUEST);
        }
        String encryptedPassword = new BCryptPasswordEncoder().encode(userRequestDTO.password());

        UserModel newUser = new UserModel(userRequestDTO.login(),
                encryptedPassword, UserRole.USER);

        this.repository.save(newUser);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete") // ok
    public ResponseEntity<?> delete(@RequestBody @Valid UserRequestDTO userRequestDTO){
        UserModel userModel = this.repository.findByLogin(userRequestDTO.login());
        if(userModel == null)
            return ResponseEntity.badRequest().build();

        repository.delete(userModel);

        return ResponseEntity.ok().build();
    }

    @PutMapping("/update/{login}") //ok
    public ResponseEntity<?> updateUser(@RequestBody @Valid UserRequestDTO userRequestDTO, @PathVariable String login){
        UserModel user = repository.findByLogin(login);
        if(user != null){
            if(user.getUserRole() == UserRole.USER && !Objects.equals(login, userRequestDTO.login())){
                return new ResponseEntity<AuthenticationCustomizedException>(
                        new AuthenticationCustomizedException(
                                "You cannot change a student's login."),
                        HttpStatus.BAD_REQUEST);
            }

            if(repository.findByLogin(userRequestDTO.login()) != null){
                return new ResponseEntity<AuthenticationCustomizedException>(
                        new AuthenticationCustomizedException(
                                "Username in use."),
                        HttpStatus.BAD_REQUEST);
            }

            String encryptedPassword = new BCryptPasswordEncoder().encode(userRequestDTO.password());

            UserModel newUser = new UserModel(userRequestDTO.login(), encryptedPassword, user.getUserRole());

            this.repository.save(newUser);

            return ResponseEntity.ok().build();
        }else{
            return ResponseEntity.notFound().build();
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
