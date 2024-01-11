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
import pl.web.Model.IdModel;
import pl.web.Repository.SettingsRepository;
import pl.web.Service.ChangeDataService;
import pl.web.Service.UsersService;

import java.util.Optional;

@Controller
public class SettingsController {
    @Autowired
    UsersService usersService;
    @Autowired
    ChangeDataService changeDataService;


    @PostMapping("/settings")
    public ResponseEntity getSettings(@RequestBody IdModel idModel) throws JsonProcessingException {
        return usersService.getVisibilitySettings(idModel);
    }

    @PostMapping("/settings-change")
    public ResponseEntity setSettings(@RequestBody Settings settings) {
        return changeDataService.changeVisibilitySettings(settings);
    }
}