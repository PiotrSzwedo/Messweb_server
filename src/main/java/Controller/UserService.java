package Controller;

import Entity.User;
import Repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;

public class UserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    ObjectMapper objectMapper;


    private boolean emptyContent(String email, String name, String password, boolean ignoreEmail, boolean ignoreName, boolean ignorePassword) {
        boolean passwordIsEmpty = true;
        boolean emailIsEmpty = true;
        boolean nameIsEmpty = true;
        if (ignoreEmail) {
            emailIsEmpty = false;
        } else emailIsEmpty = email.isEmpty();
        if (ignoreName) {
            nameIsEmpty = false;
        } else nameIsEmpty = name.isEmpty();
        if (ignorePassword) {
            passwordIsEmpty = false;
        } else passwordIsEmpty = password.isEmpty();
        return emailIsEmpty || nameIsEmpty || passwordIsEmpty;
    }

    @PostMapping("/users")
    public ResponseEntity addUser(@RequestBody User user) {
        if (emptyContent(user.getEmail(), user.getUsername(), user.getPassword(), false, false, false)) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        Optional<User> userFromDb = userRepository.findByEmail(user.getUsername());

        if (userFromDb.isPresent()) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
        }

        User savedUser = userRepository.save(user);
        return ResponseEntity.ok(savedUser);
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody User user) {
        if (emptyContent(user.getEmail(), user.getUsername(), user.getPassword(), false, true, false)) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        Optional<User> userFromDb = userRepository.findByEmail(user.getUsername());

        if (userFromDb.isEmpty() || wrongPassword(userFromDb, user)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return ResponseEntity.ok().build();
    }

    private boolean wrongPassword(Optional<User> userFromDb, User user) {
        return !userFromDb.get().getPassword().equals(user.getPassword());
    }
}
