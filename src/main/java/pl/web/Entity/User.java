package pl.web.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Arrays;
import java.util.List;


@Entity
@Table(name = "user")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@ToString
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    @NonNull
    private String username;

    @NonNull
    private String password;

    private String status;

    private static final List<String> availableStatuses = Arrays.asList(
            "admin", "banned", "user"
    );

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        if (availableStatuses.contains(status.toLowerCase())) {
            this.status = status;
        }else {
            this.status = "user";
        }
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}