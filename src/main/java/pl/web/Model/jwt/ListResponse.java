package pl.web.Model.jwt;

public class ListResponse {
    private String username;
    private Long id;

    public ListResponse(String username, Long id) {
        this.username = username;
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }
}
