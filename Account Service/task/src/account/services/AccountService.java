package account.services;

import account.auth.AppUser;
import account.auth.UserDetailsServiceImpl;
import account.entities.Payment;
import account.repositories.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.time.Month;
import java.util.*;

@Service
public class AccountService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;



    public ResponseEntity<Map<String, String>> getPaymentByPeriod(String username, String period) {
        Optional<Payment> payment = paymentRepository.findFirstByEmployeeAndPeriod(username, period);
        if(!payment.isPresent()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        AppUser user = userDetailsService.loadUserByUsername(username);
        Payment paymentOut = payment.get();

        return ResponseEntity.ok(formatPayment(user, paymentOut));

    }

    public ResponseEntity<List<Map<String,String>>> getAllPaymentsOfEmployee(String username) {
        List<Payment> query = paymentRepository.findByEmployeeOrderByPeriodDesc(username);
        List<Map<String, String>> result = new ArrayList<>();
        AppUser user = userDetailsService.loadUserByUsername(username);

        for(Payment payment : query){
            result.add(formatPayment(user, payment));
        }
        return ResponseEntity.ok(result);

    }

    private Map<String, String> formatPayment(AppUser user, Payment payment){

        return new TreeMap<>(Map.of(
                "name",user.getName(),
                "lastname", user.getLastname(),
                "period", formatPeriod(payment.getPeriod()),
                "salary", formatSalary(payment.getSalary())
        ));

    }

    private String formatSalary(Long salary) {
        long dollars = (long) salary/100;
        long cents = salary % 100;
        return dollars + " dollar(s) " + cents + " cent(s)";
    }

    private String formatPeriod(String period){
        String[] parts = period.split("-");

        String month = Month.of(Integer.valueOf(parts[0])).name();
        month = month.charAt(0) + month.substring(1).toLowerCase();
        String year = parts[1];
        return month + "-" + year;
    }

    @Transactional
    public ResponseEntity<Map<String, String>> uploadPayroll(List<Payment> paymentsList) {

        HashMap<String, HashSet<String>> employeeToPeriods = new HashMap<>();

        for(Payment payment : paymentsList){

            String email = payment.getEmployee();
            if(!employeeToPeriods.containsKey(email)) employeeToPeriods.put(email, new HashSet<>());

            String currentPeriod = payment.getPeriod();
            if(userDetailsService.userExists(email) && !employeeToPeriods.get(email).contains(currentPeriod)){
                paymentRepository.save(payment);
                employeeToPeriods.get(email).add(currentPeriod);
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error!");
            }
        }

        return ResponseEntity.ok(Map.of("status","Added successfully!"));
    }

    public ResponseEntity<Map<String, String>> updatePayrollOfSpecificEmployee(Payment newPayment){

        Optional<Payment> oldPayment = paymentRepository.findFirstByEmployeeAndPeriod(newPayment.getEmployee(), newPayment.getPeriod());
        if(oldPayment.isPresent()) {
            Payment paymentToBeSaved = oldPayment.get();
            paymentToBeSaved.setSalary(newPayment.getSalary());
            paymentRepository.save(paymentToBeSaved);
            return ResponseEntity.ok(Map.of("status","Updated successfully!"));
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Previous payment not found");
        }
    }

}
