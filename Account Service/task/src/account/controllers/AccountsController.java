package account.controllers;

import account.entities.Payment;
import account.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.List;

import java.util.Map;

@Controller
@Validated
@RequestMapping("/api/acct/")
public class AccountsController {

    @Autowired
    private AccountService accountService;

    @PostMapping(value = "payments")
    public ResponseEntity<Map<String, String>> uploadPayroll(@RequestBody List<@Valid Payment> paymentsList){
        return accountService.uploadPayroll(paymentsList);
    }

    @PutMapping("payments")
    public ResponseEntity<Map<String, String>> updatePayrollOfSpecificEmployee(@RequestBody @Valid Payment payment){
        return accountService.updatePayrollOfSpecificEmployee(payment);
    }

}
