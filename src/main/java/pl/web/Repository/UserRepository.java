package pl.web.Repository;


import pl.web.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Optional<User> findUserAllById(Long id);

    Optional<User> findByPhone(String phone);

    default Optional<User> findByPhoneNumber(String phone){
        if (phone.matches("\\d+")) {
            return findByPhone(phone);
        }else return Optional.empty();
    }
}
