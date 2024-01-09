package pl.web.Model.jwt;

public class UserResponse {

    private String username;
    private String email;
    private Long id;
    private String status;

    public UserResponse(String userName, String userEmail, Long userId, String status) {
        this.username = userName;
        this.email = userEmail;
        this.id = userId;
        this.status = status;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setStatus(String status) {
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
