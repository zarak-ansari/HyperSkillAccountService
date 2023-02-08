package account.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Map;

@Controller
@RequestMapping(value = "/api/admin/", method = {RequestMethod.GET, RequestMethod.DELETE, RequestMethod.PUT})
public class AdminController {

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @PutMapping("user/role")
    public ResponseEntity changeUserRole(@RequestBody @Valid ChangeUserRoleDTO requestBody, HttpServletRequest request){
        return userDetailsService.changeUserRole(requestBody, request.getServletPath());
    }


    @DeleteMapping("user/{email}")
    public ResponseEntity deleteUser(@PathVariable String email, HttpServletRequest request){

        return userDetailsService.deleteUser(email, request.getServletPath());
    }

    @GetMapping("user")
    public ResponseEntity getAllUsers(){
        return userDetailsService.getAllUsers();
    }

    @PutMapping("user/access")
    public ResponseEntity lockOrUnlockUser(@RequestBody LockUnlockUserDTO lockUnlockUserDTO, HttpServletRequest request){
        return userDetailsService.lockOrUnlockUser(lockUnlockUserDTO, request.getServletPath());
    }

}
