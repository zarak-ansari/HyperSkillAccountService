package account.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;


@Entity
public class LogEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    @NotNull
    @Enumerated(EnumType.STRING)
    private LogAction action;

    @NotBlank
    private String subject;

    @NotBlank
    private String object;

    @NotBlank
    private String path;

    public LogEntry() {
    }

    public LogEntry(Long id, LocalDate date, LogAction action, String subject, String object, String path) {
        this.id = id;
        this.date = date;
        this.action = action;
        this.subject = subject;
        this.object = object;
        this.path = path;
    }

    @JsonIgnore
    public Long getId() {
        return id;
    }

    @JsonIgnore
    public LocalDate getDate() {
        return date;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LogAction getAction() {
        return action;
    }

    public void setAction(LogAction action) {
        this.action = action;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return "LogEntry{" +
                "id=" + id +
                ", date=" + date +
                ", action=" + action +
                ", subject='" + subject + '\'' +
                ", object='" + object + '\'' +
                ", path='" + path + '\'' +
                '}';
    }
}
