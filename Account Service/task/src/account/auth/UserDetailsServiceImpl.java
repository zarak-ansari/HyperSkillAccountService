package account.auth;

import account.DTOs.SignupDTO;
import account.entities.LogAction;
import account.services.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


import javax.transaction.Transactional;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private static final int MIN_PASSWORD_LENGTH = 12;

    private static final int ALLOWED_FAILED_ATTEMPTS_BEFORE_LOCK = 5;

    private static final Set<String> breachedPasswords = Set.of("PasswordForJanuary", "PasswordForFebruary", "PasswordForMarch", "PasswordForApril",
            "PasswordForMay", "PasswordForJune", "PasswordForJuly", "PasswordForAugust",
            "PasswordForSeptember", "PasswordForOctober", "PasswordForNovember", "PasswordForDecember");

    @Autowired
    private LogService logService;

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public AppUser loadUserByUsername(String email) throws UsernameNotFoundException {

        Optional<AppUser> user = appUserRepository.findByEmailIgnoreCase(email);
        if(user.isPresent()){
            return user.get();
        } else {
            throw new UsernameNotFoundException("Username not found");
        }
    }

    public boolean userExists(String email){
        Optional<AppUser> user = appUserRepository.findByEmailIgnoreCase(email);
        return user.isPresent();
    }

    @Transactional
    public ResponseEntity<Object> signupUser(SignupDTO userInput, String path){


        String newPassword = userInput.getPassword();
        if(isPasswordInListOfBreachedPasswords(newPassword)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The password is in the hacker's database!");
        }
        if(!isPasswordLongEnough(newPassword)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password length must be 12 chars minimum!");
        }
        Optional<AppUser> userFromRepo = appUserRepository.findByEmailIgnoreCase(userInput.getEmail());
        if(userFromRepo.isPresent()) throw new UserExistException();

        AppUser user = new AppUser();
        user.setName(userInput.getName());
        user.setLastname(userInput.getLastname());
        user.setEmail(userInput.getEmail().toLowerCase());
        user.setPassword(passwordEncoder.encode(userInput.getPassword()));
        if(isFirstUser()) {
            user.addAuthority(roleRepository.findByAuthority("ROLE_ADMINISTRATOR").get());
        } else {
            user.addAuthority(roleRepository.findByAuthority("ROLE_USER").get());
        }
        appUserRepository.save(user);

        logService.makeLogEntry(LogAction.CREATE_USER, userInput.getEmail().toLowerCase(), path);

        return ResponseEntity.ok(user);

    }

    private boolean isFirstUser() {
        return appUserRepository.findAll().size() == 0;
    }


    @Transactional
    public ResponseEntity<Map<String, String>> changePassword(String newPassword, UserDetails userDetails, String path) {

        if(passwordEncoder.matches(newPassword, userDetails.getPassword())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The passwords must be different!");
        }
        if(isPasswordInListOfBreachedPasswords(newPassword)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The password is in the hacker's database!");
        }
        if(!isPasswordLongEnough(newPassword)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password length must be 12 chars minimum!");
        }

        AppUser user = loadUserByUsername(userDetails.getUsername());
        user.setPassword(passwordEncoder.encode(newPassword));
        appUserRepository.save(user);
        logService.makeLogEntry(LogAction.CHANGE_PASSWORD, user.getUsername(), path);
        return ResponseEntity.ok(Map.of(
                "email",userDetails.getUsername(),
                "status","The password has been updated successfully"
        ));
    }

    private boolean isPasswordLongEnough(String password){
        return password.length()>=MIN_PASSWORD_LENGTH;
    }

    private boolean isPasswordInListOfBreachedPasswords(String password){
        return breachedPasswords.contains(password);
    }

    public ResponseEntity changeUserRole(ChangeUserRoleDTO requestBody, String path) {
        String operation = requestBody.getOperation();

        String usernameInRequest = requestBody.getUser();
        String roleInRequest = requestBody.getRole();

        Optional<AppUser> userInOptional = appUserRepository.findByEmailIgnoreCase(usernameInRequest);
        if(!userInOptional.isPresent()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!");
        AppUser user = userInOptional.get();

        Optional<Role> roleInOptional = roleRepository.findByAuthority("ROLE_" + roleInRequest);
        if(!roleInOptional.isPresent()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found!");
        Role role = roleInOptional.get();
        LogAction action; // Variable for logging
        String object; // Variable for logging
        if(operation.equals("GRANT")){
            checkBusinessAdminRoleConflict(user, role);
            checkIfUserAlreadyHasAuthority(user, role);
            user.addAuthority(role);
            action = LogAction.GRANT_ROLE;
            object = "Grant role "+ roleInRequest +" to " + user.getEmail();
        } else if(operation.equals("REMOVE")){
            checkIfRoleIsAdministrator(role, operation);
            checkIfUserDoesNotHaveAuthority(user, role);
            checkIfOnlyRemainingAuthority(user);
            user.removeAuthority(role);
            action = LogAction.REMOVE_ROLE;
            object = "Remove role "+ roleInRequest +" from " + user.getEmail();

        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid operation");
        }

        logService.makeLogEntry(action, object, path);
        appUserRepository.save(user);
        return ResponseEntity.ok(user);
    }

    private void checkIfUserDoesNotHaveAuthority(AppUser user, Role role) {
        if(!user.getRoles().contains(role.getAuthority())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The user does not have a role!");
        }
    }

    private void checkBusinessAdminRoleConflict(AppUser user, Role role) {
        boolean isAdminUser = user.getRoles().contains("ROLE_ADMINISTRATOR");
        boolean isNewRoleAdmin = role.getAuthority().equals("ROLE_ADMINISTRATOR");

        if(isAdminUser != isNewRoleAdmin){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The user cannot combine administrative and business roles!");
        }


    }

    private void checkIfRoleIsAdministrator(Role role, String operation) {
        if(role.getAuthority().equals("ROLE_ADMINISTRATOR")){
            if(operation.equals("REMOVE")) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can't remove ADMINISTRATOR role!");
            }
        }
    }

    private void checkIfUserAlreadyHasAuthority(AppUser user, Role role) {
        if(user.getAuthorities().contains(role)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User already has this role!");
        }
    }

    private void checkIfOnlyRemainingAuthority(AppUser user) {
        if(user.getAuthorities().size() == 1){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"The user must have at least one role!");
        }
    }

    @Transactional
    public ResponseEntity deleteUser(String email, String path) {
        Optional<AppUser> userOptional = appUserRepository.findByEmailIgnoreCase(email);

        if(!userOptional.isPresent()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!");
        }

        AppUser user = userOptional.get();
        if(user.getRoles().contains("ROLE_ADMINISTRATOR")){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can't remove ADMINISTRATOR role!");
        }
        logService.makeLogEntry(LogAction.DELETE_USER, email, path);
        appUserRepository.deleteByEmail(email);

        return ResponseEntity.ok(Map.of("user", email, "status", "Deleted successfully!"));
    }

    public ResponseEntity getAllUsers() {
        return ResponseEntity.ok(appUserRepository.findAll());
    }

    @Transactional
    public ResponseEntity lockOrUnlockUser(LockUnlockUserDTO lockUnlockUserDTO, String path) {


        String username = lockUnlockUserDTO.getUser();
        boolean operationIsUnLock = lockUnlockUserDTO.getOperation().equals("UNLOCK") ? true : false;

        AppUser user = loadUserByUsername(username);

        if(!operationIsUnLock && user.getRoles().contains("ROLE_ADMINISTRATOR")){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can't lock the ADMINISTRATOR!");
        }

        user.setNonLocked(operationIsUnLock);

        String responseMessage;
        LogAction action;
        String object;

        if(operationIsUnLock){
            user.setFailedAttempts(0);
            responseMessage = "User " + user.getEmail() + " unlocked!" ;
            action = LogAction.UNLOCK_USER;
            object = "Unlock user " + user.getEmail();
        } else {
            responseMessage = "User " + user.getEmail() + " locked!" ;
            action = LogAction.LOCK_USER;
            object = "Lock user " + user.getEmail();
        }
        logService.makeLogEntry(action, object, path);
        return ResponseEntity.ok(Map.of("status", responseMessage));
    }

    public void incrementFailedAttempts(String username, String path) {

        AppUser user = loadUserByUsername(username);

        int newFailedAttempts = user.getFailedAttempts() + 1;

        user.setFailedAttempts(newFailedAttempts);
        if(newFailedAttempts >= 5 && !user.getRoles().contains("ROLE_ADMINISTRATOR")){
            user.setNonLocked(false);
            logService.makeLogEntry(LogAction.BRUTE_FORCE, user.getEmail(), path, path);
            logService.makeLogEntry(LogAction.LOCK_USER, user.getEmail(), "Lock user " + user.getEmail(), path);
        }

        appUserRepository.save(user);
    }

    public void resetFailedLogins(String username) {
        AppUser user = loadUserByUsername(username);
        user.setFailedAttempts(0);
        appUserRepository.save(user);
    }
}
