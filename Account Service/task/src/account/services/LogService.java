package account.services;

import account.entities.LogAction;
import account.entities.LogEntry;
import account.repositories.LogEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class LogService {

    @Autowired
    private LogEntryRepository logEntryRepository;

    public void makeLogEntry(LogAction action, String object, String path){

        LogEntry entry = new LogEntry();

        entry.setAction(action);
        entry.setObject(object);
        entry.setPath(path);

        entry.setDate(LocalDate.now());
        entry.setSubject(getUsername());

        logEntryRepository.save(entry);
    }

    public void makeLogEntry(LogAction action, String subject, String object, String path){

        LogEntry entry = new LogEntry();

        entry.setAction(action);
        entry.setObject(object);
        entry.setPath(path);

        entry.setDate(LocalDate.now());
        entry.setSubject(subject);

        logEntryRepository.save(entry);
    }


    private String getUsername(){
//        if(SecurityContextHolder.getContext().getAuthentication() == null) return "Anonymous";

        String usernameFromSecurityContext = SecurityContextHolder.getContext().getAuthentication().getName();
        if(usernameFromSecurityContext.equals("anonymousUser")){
            return "Anonymous";
        } else {
            return usernameFromSecurityContext;
        }
    }


    public ResponseEntity<List<LogEntry>> getAllEntries() {

        return ResponseEntity.ok(logEntryRepository.findAllByOrderByIdAsc());

    }
}
