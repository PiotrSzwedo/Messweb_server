package pl.web.Service;

import org.jetbrains.annotations.NotNull;
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
public class UsersService extends AccessChecking {
    private final UserRepository userRepository;
    private final SettingsRepository settingsRepository;

    @Autowired
    public UsersService(UserRepository userRepository, SettingsRepository settingsRepository) {
        this.userRepository = userRepository;
        this.settingsRepository = settingsRepository;
    }

    public ResponseEntity<?> removeUser(@NotNull User user) {
        if (user.getEmail().isEmpty() || user.getUsername().isEmpty() || user.getId() == null) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        Optional<User> userOptional = userRepository.findUserAllById(user.getId());
        if (notAccess(userOptional)) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        userRepository.delete(userOptional.get());
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<?> getUserData(@NotNull EmailModel emailModel) {
        if (emailModel.getEmail().isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        Optional<User> userOptional = userRepository.findByEmail(emailModel.getEmail());
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(generateUserResponse(userOptional, true));
    }

    public ResponseEntity<?> getMyData(@NotNull IdModel idModel) {
        if (idModel.getId() == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        Optional<User> userOp = userRepository.findById(idModel.getId());
        if (userOp.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        if (notAccess(userOp)) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        return ResponseEntity.ok(generateUserResponse(userOp, false));
    }

    public ResponseEntity<?> getUserData(@NotNull IdModel idModel) {
        Optional<User> userOp = userRepository.findById(idModel.getId());
        if (userOp.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(generateUserResponse(userOp, true));
    }

    private Object generateUserResponse(Optional<User> userOp, Boolean includeStatus) {
        User user = userOp.orElseThrow();
        Settings settings = settingsRepository.findByUserid(user.getId()).orElseThrow();

        String userName = includeStatus && !settings.getShowName() ? null : user.getUsername();
        String userEmail = includeStatus && !settings.getShowEmail() ? null : user.getEmail();
        String userStatus = includeStatus && !settings.getShowStatus() ? null : user.getStatus();
        Long userId = user.getId();

        return new UserResponse(userName, userEmail, userId, userStatus);
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

    public ResponseEntity<?> getVisibilitySettings(@NotNull IdModel idModel) {
        Optional<Settings> settingsOptional = settingsRepository.findByUserid(idModel.getId());
        if (settingsOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(settingsOptional.get());
    }
}
