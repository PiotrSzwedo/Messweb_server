package pl.web.Entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;


@Entity
@Table(name = "user")
@Getter
@Setter
@RequiredArgsConstructor
@ToString
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    @NonNull
    private String username;

    @NonNull
    private String password;
    private String phone;
    private String status;

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public User() {
    }

    public User(String email, String username, String password, String status) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.status = status;
    }

    private static final List<String> availableStatuses = Arrays.asList(
            "admin", "banned", "user"
    );
    //Getters

    public String getStatus() {
        return status;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    //Setters
    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setStatus(String status) {
        if (availableStatuses.contains(status.toLowerCase())) {
            this.status = status;
        } else {
            this.status = "user";
        }
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(status));
    }
}