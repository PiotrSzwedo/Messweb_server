package pl.web.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.web.Entity.Settings;
import pl.web.Entity.User;
import pl.web.Model.EmailModel;
import pl.web.Model.IdModel;
import pl.web.Model.ListResponse;
import pl.web.Model.UserResponse;
import pl.web.Repository.SettingsRepository;
import pl.web.Repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UsersService {
    private final UserRepository userRepository;
    private final SettingsRepository settingsRepository;

    @Autowired
    public UsersService(UserRepository userRepository, SettingsRepository settingsRepository) {
        this.userRepository = userRepository;
        this.settingsRepository = settingsRepository;
    }

    public ResponseEntity<?> removeUser(User user) {
        if (user.getEmail().isEmpty() || user.getUsername().isEmpty() || user.getId() == null) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } else {
            Optional<User> userOptional = userRepository.findUserAllById(user.getId());
            userRepository.delete(userOptional.get());
            return ResponseEntity.ok().build();
        }
    }

    public ResponseEntity<UserResponse> getUserDataByEmail(EmailModel emailModel) {
        if (emailModel.getEmail().isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        Optional<User> userOptional = userRepository.findByEmail(emailModel.getEmail());
        return userOptional.map(user -> {
            return ResponseEntity.ok(new UserResponse(
                    userOptional.get().getUsername(),
                    userOptional.get().getEmail(),
                    userOptional.get().getId(),
                    userOptional.get().getStatus()
            ));
        }).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    public ResponseEntity<?> getUserDataById(IdModel idModel) {
        if (idModel.getId() == null) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        Optional<User> userOp = userRepository.findById(idModel.getId());
        if (userOp.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(new UserResponse(
                userOp.get().getUsername(),
                userOp.get().getEmail(),
                userOp.get().getId(),
                userOp.get().getStatus()
        ));
    }

    public ResponseEntity<?> getAllUsers() {
        List<User> users = userRepository.findAll();
        List<ListResponse> listResponses = new ArrayList<ListResponse>();
        if (users.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        for (User user : users) {
            assert false;
            listResponses.add(new ListResponse(user.getUsername(), user.getId()));
        }
        return ResponseEntity.ok(listResponses);
    }

    public ResponseEntity<?> getVisibilitySettings(IdModel idModel) {
        Optional<Settings> settingsOptional = settingsRepository.findByUserid(idModel.getId());
        if (settingsOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(settingsOptional.get());
    }
}
