package account.auth;

import account.DTOs.SignupDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.Map;
import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public AppUser loadUserByUsername(String email) throws UsernameNotFoundException {

        Optional<AppUser> user = appUserRepository.findByEmailIgnoreCase(email);
        if(user.isPresent()){
            return user.get();
        } else {
            throw new UsernameNotFoundException(String.format("Username not found"));
        }
    }

    public ResponseEntity<Object> signupUser(SignupDTO userInput){

        Optional<AppUser> userFromRepo = appUserRepository.findByEmailIgnoreCase(userInput.getEmail());
        if(userFromRepo.isPresent()) throw new UserExistException();

        AppUser user = new AppUser();
        user.setName(userInput.getName());
        user.setLastname(userInput.getLastname());
        user.setEmail(userInput.getEmail());
        user.setPassword(passwordEncoder.encode(userInput.getPassword()));
        appUserRepository.save(user);

        return ResponseEntity.ok(user);

    }

}
