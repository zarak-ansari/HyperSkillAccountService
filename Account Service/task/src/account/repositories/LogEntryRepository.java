package account.repositories;

import account.entities.LogAction;
import account.entities.LogEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface LogEntryRepository extends JpaRepository<LogEntry, Long> {

    List<LogEntry> findAllByOrderByIdAsc();
}
