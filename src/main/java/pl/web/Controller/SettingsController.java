package pl.web.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import pl.web.Entity.Settings;
import pl.web.Model.IdModel;
import pl.web.Service.ChangeDataService;
import pl.web.Service.UsersService;

@Controller
public class SettingsController {
    @Autowired
    public SettingsController(UsersService usersService, ChangeDataService changeDataService) {
        this.usersService = usersService;
        this.changeDataService = changeDataService;
    }

    UsersService usersService;
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