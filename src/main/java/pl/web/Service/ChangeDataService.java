package pl.web.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.web.Entity.Settings;
import pl.web.Entity.User;
import pl.web.Repository.SettingsRepository;
import pl.web.Repository.UserRepository;

import java.util.Objects;
import java.util.Optional;

@Service
// The class which is responsible for changing data in mysql server
public class ChangeDataService extends AccessChecking{
    private final UserRepository userRepository;

    private final SettingsRepository settingsRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public ChangeDataService(UserRepository userRepository, SettingsRepository settingsRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.settingsRepository = settingsRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Function which change user data
    public ResponseEntity<?> changeUserData(User user) {
        Optional<User> user1 = userRepository.findUserAllById(user.getId());
        if (notAccess(user1)) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        if (user1.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        if (user.getStatus() != null && !user1.get().getStatus().equals(user.getStatus()) && !user.getStatus().isEmpty()) {
            //Changing user status
            user1.get().setStatus(user.getStatus());
        }
        if (user.getUsername() != null && !user1.get().getUsername().equals(user.getUsername()) && !user.getUsername().isEmpty()) {
            //Changing user name
            user1.get().setUsername(user.getUsername());
        }
        if (user.getPassword() != null && !user1.get().getPassword().equals(user.getPassword()) && !user.getPassword().isEmpty()) {
            //Changing password
            user1.get().setPassword(passwordEncoder.encode(user.getPassword()));
        }
        if (user.getEmail() != null && !user1.get().getEmail().equals(user.getEmail()) && !user.getEmail().isEmpty()) {
            //Changing user e-mail
            user1.get().setEmail(user.getEmail());
        }
        userRepository.save(user1.get());
        return ResponseEntity.ok().build();
    }

    // Function which change visibility settings (settings that set visibility data on user profile)
    public ResponseEntity<?> changeVisibilitySettings(Settings settings) {
        if (notAccess(settings.getUserid())) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        if (settings.getUserid() == null) return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        Optional<Settings> settingsOptional = settingsRepository.findByUserid(settings.getUserid());
        if (settingsOptional.isEmpty()) return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

        if (settingsOptional.get().getShowEmail() != settings.getShowEmail() && settings.getShowEmail() != null)
            settingsOptional.get().setShowEmail(settings.getShowEmail());

        if (settingsOptional.get().getShowName() != settings.getShowName() && settings.getShowName() != null)
            settingsOptional.get().setShowName(settings.getShowName());

        if (settingsOptional.get().getShowStatus() != settings.getShowStatus() && settings.getShowStatus() != null)
            settingsOptional.get().setShowStatus(settings.getShowStatus());

        settingsRepository.save(settingsOptional.get());
        return ResponseEntity.ok().build();
    }

    // Function that responsible for generate default visibility settings
    public void generateDefualtVisibilitySettings(User user) {
        if (userRepository.findById(user.getId()).isEmpty()) return;
        Optional<Settings> settingsOptional = settingsRepository.findByUserid(user.getId());
        if (settingsOptional.isEmpty()) {
            Settings settings = new Settings();
            settings.setUserid(user.getId());
            settings.setShowEmail(true);
            settings.setShowName(true);
            settings.setShowStatus(true);
            settingsRepository.save(settings);
        }
    }
}