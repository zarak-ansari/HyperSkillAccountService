package account.controllers;


import account.auth.AppUser;
import account.auth.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("api/empl/")
public class EmployeeController {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @GetMapping("payment")
    public ResponseEntity<AppUser> payment(@AuthenticationPrincipal UserDetails userDetails){
        return ResponseEntity.ok(userDetailsService.loadUserByUsername(userDetails.getUsername()));
    }


}
