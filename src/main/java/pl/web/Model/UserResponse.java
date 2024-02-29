package pl.web.Model;

public class UserResponse {

    private final String username;
    private final String email;
    private final Long id;
    private final String status;

    public UserResponse(String username, String email, Long id, String status) {
        this.username = username;
        this.email = email;
        this.id = id;
        this.status = status;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public Long getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }
}