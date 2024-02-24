package pl.web.Model;

public class UserResponse {

    private String username;
    private String email;
    private Long id;
    private String status;
    private String Token;

    public UserResponse(String username, String email, Long id, String status) {
        this.username = username;
        this.email = email;
        this.id = id;
        this.status = status;
    }

    public UserResponse(String username, String email, Long id, String status, String token) {
        this.username = username;
        this.email = email;
        this.id = id;
        this.status = status;
        Token = token;
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