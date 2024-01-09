package pl.web.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
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

//The class responsible for acting on users
@Controller
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    UsersService usersService;
    @Autowired
    AuthorizationService authorizationService;
    @Autowired
    ChangeDataService changeDataService;

    //___________GetMapping___________
    @GetMapping("/user")
    public ResponseEntity users() throws JsonProcessingException {
        return usersService.getAllUsers();
    }

    //___________PostMapping___________
    @PostMapping("/users")
    public ResponseEntity<?> addUser(@RequestBody RegisterModel registerModel) {
        return authorizationService.register(registerModel);
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

    @PostMapping("/user-data")
    public ResponseEntity<?> getUserData(@RequestBody EmailModel emailModel) {
        return usersService.getUserDataByEmail(emailModel);
    }

    @PostMapping("/user-data-id")
    public ResponseEntity<?> findById(@RequestBody IdModel idModel) {
        return usersService.getUserDataById(idModel);
    }

    @PostMapping("/admin-autorize")
    public ResponseEntity<?> autorizeAdmin(@RequestBody IdModel idModel) {
        return authorizationService.autorizeAdminStatus(idModel);
    }
}
