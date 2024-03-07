package pl.web.Controller;

import jakarta.mail.MessagingException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.web.Entity.User;
import pl.web.Model.EmailModel;
import pl.web.Model.LoginModel;
import pl.web.Model.RegisterModel;
import pl.web.Service.AuthorizationService;
import pl.web.Service.ChangeDataService;
import pl.web.Service.NotificationService;
import pl.web.Service.UsersService;

@Controller
@RequestMapping("/api/admin")
public class AdminController {
    private final NotificationService notificationService;
    private final UsersService usersService;
    private final AuthorizationService authorizationService;
    private final ChangeDataService changeDataService;

    public AdminController(NotificationService notificationService, UsersService usersService, AuthorizationService authorizationService, ChangeDataService changeDataService) {
        this.notificationService = notificationService;
        this.usersService = usersService;
        this.authorizationService = authorizationService;
        this.changeDataService = changeDataService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterModel registerModel, @RequestBody String adminToken) {
        return authorizationService.register(registerModel, "admin");
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody LoginModel loginModel, @RequestBody String adminToken){
        return changeDataService.changeUserData(new User(loginModel.getEmail(), null, loginModel.getPassword(), null));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> sendEmail(@RequestBody EmailModel emailModel) throws MessagingException {
        return notificationService.formatEmail(emailModel.getEmail(), "$$forgot-password$$", "");
    }
}
