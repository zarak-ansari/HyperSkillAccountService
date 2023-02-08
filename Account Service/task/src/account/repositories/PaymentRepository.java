package account.repositories;


import account.entities.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findFirstByEmployeeAndPeriod(String email, String period);

    List<Payment> findByEmployeeOrderByPeriodDesc(String email);
}
