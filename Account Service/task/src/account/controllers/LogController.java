package account.controllers;


import account.entities.LogEntry;
import account.services.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;


@Controller
@RequestMapping("/api/security/")
public class LogController {


    @Autowired
    private LogService logService;

    @GetMapping("/events/")
    public ResponseEntity<List<LogEntry>> getAllEvents(){
        return logService.getAllEntries();
    }

}
