package pl.web.Service;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.web.Entity.User;
import pl.web.Model.IdModel;
import pl.web.Model.LoginModel;
import pl.web.Model.RegisterModel;
import pl.web.Repository.UserRepository;
import pl.web.Security.JWT.JwtService;

import java.util.Optional;

@Service
// The Class that is responsible for authorization users
public class AuthorizationService {
    @Autowired
    public AuthorizationService(UserRepository userRepository, ChangeDataService changeDataService, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.changeDataService = changeDataService;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    private final UserRepository userRepository;
    private final ChangeDataService changeDataService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    // The function which login users
    public ResponseEntity<?> login(@NotNull LoginModel loginModel) {
        Optional<User> user = userRepository.findByEmail(loginModel.getEmail());
        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        if (user.get().getStatus().equals("banned")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        if (checkPassword(loginModel.getPassword(), user.get().getPassword())) {
            return sendToken(user.get().getId());
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
    public boolean checkPassword(String password, String passwordFromDataBase) {
        return BCrypt.checkpw(password, passwordFromDataBase);
    }
    // The function which register new users
    public ResponseEntity<?> register(@NotNull RegisterModel registerModel, String status) {
        if (userRepository.findByEmail(registerModel.getEmail()).isEmpty())
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
        User user = createNewUser(registerModel, status);
        userRepository.save(user);
        changeDataService.generateDefualtVisibilitySettings(user);
        return sendToken(user.getId());
    }

    // The function which is responsible to sending token
    private ResponseEntity<?> sendToken(Long id) {
        String token = jwtService.generateToken(new IdModel(id));
        return ResponseEntity.ok(token);
    }

    // The function that create new user entity from registering model
    private User createNewUser(RegisterModel registerModel, String status) {
        return new User(
                registerModel.getEmail(),
                registerModel.getUsername(),
                passwordEncoder.encode(registerModel.getPassword()),
                status
        );
    }

    // The function that checks whether the user has administrator status
    public ResponseEntity<?> authorizeAdminStatus(@NotNull IdModel idModel) {
        Optional<User> userOptional = userRepository.findUserAllById(idModel.getId());
        if (userOptional.isEmpty() || !userOptional.get().getStatus().equals("admin")) {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } else return ResponseEntity.ok().build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    private void passwordIsEncode(String password){

    }
}