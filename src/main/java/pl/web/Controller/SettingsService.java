package pl.web.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import pl.web.Entity.Settings;
import pl.web.Entity.User;
import pl.web.Repository.SettingsRepository;

import java.util.Optional;

@Controller
public class SettingsService {
    @Autowired
    SettingsRepository settingsRepository;
    @Autowired
    ObjectMapper objectMapper;

    public void generateDefualtSettings(User user){
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

    @PostMapping("/settings")
    public ResponseEntity getSettings(@RequestBody User user) throws JsonProcessingException {
        Optional<Settings> settingsOptional = settingsRepository.findByUserid(user.getId());
        if (settingsOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return  ResponseEntity.ok(objectMapper.writeValueAsString(settingsOptional));
    }
    @PostMapping("/settings-change")
    public ResponseEntity setSettings(@RequestBody Settings settings){
        Optional<Settings> settingsOptional = settingsRepository.findByUserid(settings.getUserid());
        if (settingsOptional.isEmpty()) return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        if (!settingsOptional.get().getShowEmail() == settings.getShowEmail()) settingsOptional.get().setShowEmail(settings.getShowEmail());
        if (!settingsOptional.get().getShowName() == settings.getShowName()) settingsOptional.get().setShowName(settings.getShowName());
        if (!settingsOptional.get().getShowStatus() == settings.getShowStatus()) settingsOptional.get().setShowStatus(settings.getShowStatus());
        settingsRepository.save(settingsOptional.get());
        return ResponseEntity.ok().build();
    }

}
