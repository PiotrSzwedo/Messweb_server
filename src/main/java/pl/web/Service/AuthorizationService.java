package pl.web.Service;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import pl.web.Entity.User;
import pl.web.Model.IdModel;
import pl.web.Model.LoginModel;
import pl.web.Model.RegisterModel;
import pl.web.Repository.UserRepository;

import java.util.Optional;

@Service
// The Class that is responsible for authorization users
public class AuthorizationService {
    private final UserRepository userRepository;
    private final ChangeDataService changeDataService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthorizationService(UserRepository userRepository, ChangeDataService changeDataService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.changeDataService = changeDataService;
        this.passwordEncoder = passwordEncoder;
    }

    // The function which login users
    public ResponseEntity<?> login(@NotNull LoginModel loginModel) {
        if (loginModel.getPassword().isEmpty() || loginModel.getEmail().isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        Optional<User> userOptional = userRepository.findByEmail(loginModel.getEmail());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (user.getStatus().equals("banned")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            } else if (user.getPassword().equals(loginModel.getPassword())) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // The function which register new users
    public ResponseEntity<?> register(@NotNull RegisterModel registerModel) {
        if (registerModel.getEmail().isEmpty() || registerModel.getUsername().isEmpty() || registerModel.getPassword().isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        Optional<User> userFromDb = userRepository.findByEmail(registerModel.getEmail());
        if (userFromDb.isPresent()) return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
        registerModel.setPassword(passwordEncoder.encode(registerModel.getPassword()));
        User savedUser = userRepository.save(new User(registerModel.getEmail(), registerModel.getUsername(), registerModel.getPassword(), "user"));
        changeDataService.generateDefualtVisibilitySettings(savedUser);
        return ResponseEntity.ok().build();
    }

    // The function that checks whether the user has administrator status
    public ResponseEntity<?> authorizeAdminStatus(@NotNull @RequestBody IdModel idModel) {
        Optional<User> userOptional = userRepository.findUserAllById(idModel.getId());
        if (userOptional.isEmpty() || !userOptional.get().getStatus().equals("admin")) {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } else return ResponseEntity.ok().build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}