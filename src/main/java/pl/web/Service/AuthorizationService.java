package pl.web.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import pl.web.Entity.User;
import pl.web.Model.IdModel;
import pl.web.Model.LoginModel;
import pl.web.Model.RegisterModel;
import pl.web.Repository.UserRepository;

import java.util.Optional;

@Service
// Class that is responsible for authorization users
public class AuthorizationService {
    private final UserRepository userRepository;
    private final ChangeDataService changeDataService;

    @Autowired
    public AuthorizationService(UserRepository userRepository, ChangeDataService changeDataService) {
        this.userRepository = userRepository;
        this.changeDataService = changeDataService;
    }

    public ResponseEntity<?> login(LoginModel loginModel) {
        if (loginModel.getPassword().isEmpty() || loginModel.getEmail().isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        Optional<User> userOptional = userRepository.findByEmail(loginModel.getEmail());
        if (userOptional.isPresent() && userOptional.get().getPassword().equals(loginModel.getPassword())) {
            return ResponseEntity.ok().build();
        } else return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    public ResponseEntity<?> register(RegisterModel registerModel) {
        if (registerModel.getEmail().isEmpty() || registerModel.getUsername().isEmpty() || registerModel.getPassword().isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        Optional<User> userFromDb = userRepository.findByEmail(registerModel.getEmail());
        if (userFromDb.isPresent()) return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
        User savedUser = userRepository.save(new User(registerModel.getEmail(), registerModel.getUsername(), registerModel.getPassword(), "user"));
        changeDataService.generateDefualtVisibilitySettings(savedUser);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<?> autorizeAdminStatus(@RequestBody IdModel idModel) {
        Optional<User> userOptional = userRepository.findUserAllById(idModel.getId());
        if (userOptional.isEmpty() || !userOptional.get().getStatus().equals("admin")) {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } else return ResponseEntity.ok().build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
