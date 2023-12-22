package pl.web.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "settings")
@AllArgsConstructor
@NoArgsConstructor
public class Settings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userid;
    private Boolean showname;
    private Boolean showemail;
    private Boolean showstatus;

    public void setShowName(Boolean showName) {
        this.showname = showName;
    }

    public void setUserid(Long userid) {
        if (this.userid == null) {
            this.userid = userid;
        }
    }

    public void setShowEmail(Boolean showEmail) {
        this.showemail = showEmail;
    }

    public void setShowStatus(Boolean showStatus) {
        this.showstatus = showStatus;
    }

    public Long getUserid() {
        return userid;
    }

    public Boolean getShowName() {
        return showname;
    }

    public Boolean getShowEmail() {
        return showemail;
    }

    public Boolean getShowStatus() {
        return showstatus;
    }
}
