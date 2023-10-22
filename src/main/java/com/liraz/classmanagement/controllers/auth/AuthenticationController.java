package com.liraz.classmanagement.controllers.auth;

import com.liraz.classmanagement.configuration.TokenService;
import com.liraz.classmanagement.domain.user.UserModel;
import com.liraz.classmanagement.domain.user.UserRole;
import com.liraz.classmanagement.dtos.auth.LoginResponseDTO;
import com.liraz.classmanagement.dtos.auth.RegisterDTO;
import com.liraz.classmanagement.dtos.auth.UserRequestDTO;
import com.liraz.classmanagement.exceptions.AuthenticationCustomizedException;
import com.liraz.classmanagement.repositories.StudentRepository;
import com.liraz.classmanagement.repositories.UserRepository;
import com.liraz.classmanagement.services.StudentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;


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

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid UserRequestDTO userRequestDTO){

        if(repository.findByLogin(userRequestDTO.login()) == null)
            return ResponseEntity.badRequest().build();

        var usernamePassword = new UsernamePasswordAuthenticationToken(
                userRequestDTO.login(), userRequestDTO.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);
        var token = tokenService.generateToken((UserModel) auth.getPrincipal());

        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid RegisterDTO registerDTO) throws InstantiationException, IllegalAccessException {

        //if(this.repository.findByLogin(registerDTO.login()) != null)
          //  return ResponseEntity.badRequest().build();
        try{
            if( registerDTO.userRole() == UserRole.USER
                    && this.studentRepository.findByRegistration(Integer.parseInt(registerDTO.login())) == null){
                return new ResponseEntity<AuthenticationCustomizedException>(
                        new AuthenticationCustomizedException(
                                "A student login must be their's registration number. You must register it first."),
                        HttpStatus.BAD_REQUEST);
            }
        }catch (Exception e){
            return new ResponseEntity<AuthenticationCustomizedException>(
                    new AuthenticationCustomizedException(
                            "A student login must be their's registration number. It must be an integer."),
                    HttpStatus.BAD_REQUEST);
        }

        String encryptedPassword = new BCryptPasswordEncoder().encode(registerDTO.password());

        UserModel newUser = new UserModel(registerDTO.login(), encryptedPassword, registerDTO.userRole());

        this.repository.save(newUser);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> delete(@RequestBody @Valid UserRequestDTO userRequestDTO){
        UserModel userModel = this.repository.findByLogin(userRequestDTO.login());
        if(userModel == null)
            return ResponseEntity.badRequest().build();

        repository.delete(userModel);

        return ResponseEntity.ok().build();
    }
}
