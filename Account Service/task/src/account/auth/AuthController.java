package account.auth;

import account.DTOs.SignupDTO;
import account.DTOs.ChangePassDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("api/auth/")
@Validated
public class AuthController {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;


    @PostMapping("signup")
    public ResponseEntity<Object> signup(HttpServletRequest request, @RequestBody @Valid SignupDTO input){

        return userDetailsService.signupUser(input, request.getServletPath());
    }

    @PostMapping("changepass")
    public ResponseEntity<Map<String, String>> changePass(@RequestBody @Valid ChangePassDTO changePassDTO,
                                                          @AuthenticationPrincipal UserDetails userDetails, HttpServletRequest request){
        return userDetailsService.changePassword(changePassDTO.getNew_password(), userDetails, request.getServletPath() );
    }
}
