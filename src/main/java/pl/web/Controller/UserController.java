package pl.web.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.web.Entity.User;
import pl.web.Model.EmailModel;
import pl.web.Model.IdModel;
import pl.web.Model.LoginModel;
import pl.web.Model.RegisterModel;
import pl.web.Service.AuthorizationService;
import pl.web.Service.ChangeDataService;
import pl.web.Service.UsersService;

@Controller
@RequestMapping("/api/user")
//The class responsible for controlling acting on a users
public class UserController {
    private final UsersService usersService;
    private final AuthorizationService authorizationService;
    private final ChangeDataService changeDataService;

    @Autowired
    public UserController(UsersService usersService, AuthorizationService authorizationService, ChangeDataService changeDataService) {
        this.usersService = usersService;
        this.authorizationService = authorizationService;
        this.changeDataService = changeDataService;
    }

    //___________GetMapping___________
    @GetMapping("/user")
    public ResponseEntity users() {
        return usersService.getAllUsers();
    }

    //___________PostMapping___________
    @PostMapping("/register")
    public ResponseEntity<?> addUser(@RequestBody RegisterModel registerModel) {
        return authorizationService.register(registerModel, "user");
    }

    @GetMapping("/get")
    public ResponseEntity sayHello() {
        return ResponseEntity.ok("Brawo dostałeś się");
    }

    @PostMapping("/user-delete")
    public ResponseEntity<?> deleteUser(@RequestBody User user) {
        return usersService.removeUser(user);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginModel loginModel) {
        return authorizationService.login(loginModel);
    }

    @PostMapping("/user-data-change")
    public ResponseEntity<?> changeUserData(@RequestBody User user) {
        return changeDataService.changeUserData(user);
    }

    @PostMapping("/my-data")
    public ResponseEntity<?> getMyData(@RequestBody IdModel idModel) {
        return usersService.getMyData(idModel);
    }

    @PostMapping("/user-data")
    public ResponseEntity<?> getUserData(@RequestBody EmailModel emailModel) {
        return usersService.getUserData(emailModel);
    }

    @PostMapping("/user-data-id")
    public ResponseEntity<?> findById(@RequestBody IdModel idModel) {
        return usersService.getUserData(idModel);
    }

    @PostMapping("/admin-autorize")
    public ResponseEntity<?> authorizeAdmin(@RequestBody IdModel idModel) {
        return authorizationService.authorizeAdminStatus(idModel);
    }
}