package account.controllers;


import account.auth.AppUser;
import account.auth.UserDetailsServiceImpl;
import account.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;
import java.util.List;

@Controller
@RequestMapping("api/empl/")
public class EmployeeController {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private AccountService accountService;


    @GetMapping(value = "payment", params={}) // defined params to differentiate from mapping with a period parameter
    public ResponseEntity<List<Map<String,String>>> getPayments(@AuthenticationPrincipal UserDetails userDetails){
        return accountService.getAllPaymentsOfEmployee(userDetails.getUsername());
    }

    @GetMapping(value="payment", params="period")
    public ResponseEntity<Map<String,String>> payment(@AuthenticationPrincipal UserDetails userDetails, @RequestParam String period){
        return accountService.getPaymentByPeriod(userDetails.getUsername(), period);
    }


}
