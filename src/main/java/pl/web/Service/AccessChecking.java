package pl.web.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import pl.web.Entity.User;
import pl.web.Repository.UserRepository;

import java.util.Optional;

public abstract class AccessChecking {
    @Autowired
    private UserRepository userRepository;

    public boolean notAccess(Optional<User> user) {
        String authenticatedUserId = SecurityContextHolder.getContext().getAuthentication().getName();
        return !(authenticatedUserId.equals(String.valueOf(user))) && (user.get().getStatus().equals("banned"));
    }

    public boolean notAccess(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        return notAccess(user);
    }
}
