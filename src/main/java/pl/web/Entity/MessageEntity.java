package pl.web.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "message")
@Getter
@Setter
@RequiredArgsConstructor
@ToString
public class MessageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long from;
    private Long to;
    private String body;
    private boolean text;

    public MessageEntity(Long from, Long to, String body, boolean text) {
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setFrom(Long from) {
        this.from = from;
    }

    public void setTo(Long to) {
        this.to = to;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setText(boolean text) {
        this.text = text;
    }

    public Long getId() {
        return id;
    }

    public Long getFrom() {
        return from;
    }

    public Long getTo() {
        return to;
    }

    public String getBody() {
        return body;
    }

    public boolean isText() {
        return text;
    }
}
