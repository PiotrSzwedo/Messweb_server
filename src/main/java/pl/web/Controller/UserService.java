package pl.web.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import pl.web.Entity.User;
import pl.web.Repository.UserRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

//The class responsible for acting on users
@Controller
public class UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    SettingsService settingsService;

    //___________Other___________
    private boolean emptyContent(String email, String name, String password, boolean ignoreEmail, boolean ignoreName, boolean ignorePassword) {
        boolean passwordIsEmpty = true;
        boolean emailIsEmpty = true;
        boolean nameIsEmpty = true;
        if (ignoreEmail) {
            emailIsEmpty = false;
        } else {
            if (email != null) {
                emailIsEmpty = email.isEmpty();
            }
        }
        if (ignoreName) {
            nameIsEmpty = false;
        } else {
            if (name != null) {
                nameIsEmpty = name.isEmpty();
            }
        }
        if (ignorePassword) {
            passwordIsEmpty = false;
        } else {
            if (password != null) {
                passwordIsEmpty = password.isEmpty();
            }
        }
        return emailIsEmpty || nameIsEmpty || passwordIsEmpty;
    }

    private boolean wrongPassword(Optional<User> userFromDb, User user) {
        return userFromDb.map(value -> !value.getPassword().equals(user.getPassword())).orElse(true);
    }

    private void chnageStatus(User user, String newStatus) {
        user.setStatus(newStatus);
        userRepository.save(user);
    }
    //___________GetMapping___________
    @GetMapping("/user")
    public ResponseEntity users() throws JsonProcessingException {
        List<User> users = userRepository.findAll();
        if (users.isEmpty()){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(objectMapper.writeValueAsString(users));
    }
    //___________PostMapping___________
    @PostMapping("/users")
    public ResponseEntity addUser(@RequestBody User user) {
        if (emptyContent(user.getEmail(), user.getUsername(), user.getPassword(), false, false, false)) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        Optional<User> userFromDb = userRepository.findByEmail(user.getEmail());

        if (userFromDb.isPresent()) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
        }
        user.setStatus("user");
        User savedUser = userRepository.save(user);
        settingsService.generateDefualtSettings(user);
        return ResponseEntity.ok(savedUser);
    }

    @PostMapping("/user-delete")
    public ResponseEntity deleteUser(@RequestBody User user) {
        if (emptyContent(user.getEmail(), user.getUsername(), user.getPassword(), false, false, false)) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } else {
            Optional<User> userOptional = userRepository.findByEmail(user.getEmail());
            if (!wrongPassword(userOptional, user) && userOptional.isPresent()) {
                userRepository.delete(userOptional.get());
                return ResponseEntity.ok().build();
            } else return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody User user) {
        if (emptyContent(user.getEmail(), user.getUsername(), user.getPassword(), false, true, false)) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        Optional<User> userFromDb = userRepository.findByEmail(user.getEmail());

        if (userFromDb.isEmpty() || wrongPassword(userFromDb, user)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if (userFromDb.get().getStatus() == null) chnageStatus(userFromDb.get(), "user");
        return ResponseEntity.ok().build();
    }

    @PostMapping("/user-name")
    public ResponseEntity changeName(@RequestBody User user, @RequestBody String newName) {
        if (!emptyContent(user.getEmail(), user.getUsername(), user.getPassword(), false, false, false) || newName.isEmpty())
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        Optional<User> userOptional = userRepository.findByEmail(user.getEmail());
        if (userOptional.isPresent()) {
            if (wrongPassword(userOptional, user)) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            userOptional.get().setUsername(newName);
            userRepository.save(userOptional.get());
            return ResponseEntity.ok().build();
        } else return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PostMapping("/user-email")
    public ResponseEntity changeEmail(@RequestBody User user, @RequestBody String newEmail) {
        if (emptyContent(user.getEmail(), user.getUsername(), user.getPassword(), false, true, false) || newEmail.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } else {
            Optional<User> userOptional = userRepository.findByEmail(user.getEmail());
            if (userOptional.isEmpty() || wrongPassword(userOptional, user)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            } else {
                userOptional.get().setEmail(newEmail);
                userRepository.save(userOptional.get());
                return ResponseEntity.ok().build();
            }
        }
    }

    @PostMapping("/user-password")
    public ResponseEntity changePassword(@RequestBody User user, @RequestBody String newPassword) {
        if (emptyContent(user.getEmail(), user.getUsername(), user.getPassword(), false, true, false) || newPassword.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } else {
            Optional<User> userOptional = userRepository.findByEmail(user.getEmail());
            if (userOptional.isEmpty() || wrongPassword(userOptional, user)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            } else {
                userOptional.get().setPassword(newPassword);
                userRepository.save(userOptional.get());
                return ResponseEntity.ok().build();
            }
        }
    }

    @PostMapping("/user-data")
    public ResponseEntity getUserData(@RequestBody User user) {
        if (emptyContent(user.getEmail(), user.getUsername(), user.getPassword(), false, true, true)) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        Optional<User> userOptional = userRepository.findByEmail(user.getEmail());
        if (userOptional.isPresent()) {
            userOptional.get().setPassword("");
            try {
                return ResponseEntity.ok(objectMapper.writeValueAsString(userOptional));
            } catch (JsonProcessingException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        } else return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PostMapping("/user-data-id")
    public ResponseEntity findById(@RequestBody Map<String, Long> requestBody) {
        Long id = requestBody.get("id");
        if (id == null) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        Optional<User> userOp = userRepository.findById(id);
        if (userOp.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        try {
            return ResponseEntity.ok(objectMapper.writeValueAsString(userOp));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
