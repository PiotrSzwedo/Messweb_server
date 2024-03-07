package pl.web.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pl.web.Entity.User;
import pl.web.Repository.UserRepository;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@Service
public class NotificationService {
    @Autowired
    public NotificationService(UserRepository userRepository, JavaMailSender javaMailSender) {
        this.userRepository = userRepository;
        this.javaMailSender = javaMailSender;
    }

    private final UserRepository userRepository;
    private final JavaMailSender javaMailSender;

    public ResponseEntity<?> formatEmail(String to, String title, String body) {
        User user = userRepository.findByEmail(to).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        if (title.equals("$$forgot-password$$")) {
            title = "Change Your Forgot Password";
            body = forgotPassword(user.getUsername());
        }

        if (title.equals("$$confirm$$")) {
            title = "Confirm Your E-mail";
            body = confirm(user.getUsername());
        }

        try {
            sendEmail(to, title, body);
        } catch (MessagingException e) {
            return (ResponseEntity<?>) ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return ResponseEntity.ok().build();
    }

    private void sendEmail(String to, String title, String body) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
        mimeMessageHelper.setTo(to);
        mimeMessageHelper.setSubject(title);
        mimeMessageHelper.setText(body.formatted(to), true);

        javaMailSender.send(mimeMessage);
    }

    private String forgotPassword(String userName) {
        return String.format(
                """
                        <h3>Hi %s</h3>
                        <div>
                          <span>We have given a request to change your forgot password in %s. If you don't want to change your password, you can ignore this email, but we recommend checking whether someone has access to your account.</span>
                        </div>
                        <div style="display: flex;justify-content: center;">
                            <a href="localhost:3000/change-password?email" style='border-radius: 20px;background: blue;padding: 4px;color: white;'>Change password</a>
                        </div>
                        """, userName, date());
    }

    private String confirm(String userName) {
        return String.format(
                """
                        <h3>Hi %s</h3>
                        <div>
                          <span>We have given a request to confirm your email in %s. If you don't confirm your e-mail you won't use his to for example change forgot password.</span>
                          <span>If didn't you create account, you can contact with administration or try delete account yourself.</span>
                        </div>
                        <div style="display: flex;justify-content: center;">
                            <a href="localhost:3000/change-password?email" style='border-radius: 20px;background: blue;padding: 4px;color: white;'>Confirm E-mail</a>
                        </div>
                        """, userName, date());
    }

    private String date() {
        return new SimpleDateFormat("MMMM dd yyyy", Locale.getDefault()).format(Date.from(java.time.Instant.now()));
    }
}
