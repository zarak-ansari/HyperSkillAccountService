package account.auth;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.stream.Collectors;

@Entity
public class AppUser implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;
    @NotEmpty @NotNull
    private String name;
    @NotEmpty @NotNull
    private String lastname;
    @NotEmpty @NotNull @Email
    private String email;
    @NotEmpty @NotNull @JsonIgnore
    private String password;

    @NotNull
    private boolean nonLocked;

    @NotNull
    private int failedAttempts;

    @ManyToMany(
            fetch = FetchType.EAGER,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            }
    )
    private Set<Role> roles;

    public AppUser(long id, String name, String lastname, String email, String password) {
        this.id = id;
        this.name = name;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
        this.roles = new HashSet<>();
        this.nonLocked = true;
        this.failedAttempts = 0;
    }

    public AppUser() {
        this.roles = new HashSet<>();
        this.nonLocked = true;
        this.failedAttempts = 0;
    }

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles;
    }

    public Collection<String> getRoles() {
        List<String> result = new ArrayList<>();
        for(Role role : this.roles){
            result.add(role.getAuthority());
        }

        return result.stream().sorted().collect(Collectors.toList());
    }

    public void addAuthority(Role authority){

        this.roles.add(authority);
    }

    public boolean removeAuthority(Role authority){
        if(this.roles.contains(authority)){
            this.roles.remove(authority);
            return true;
        } else {
            return false;
        }
    }


    @Override
    public String getPassword() {
        return password;
    }

    @Override
    @JsonIgnore
    public String getUsername() {
        return email;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return this.nonLocked;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return true;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName(){ return this.name; }
    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setNonLocked(boolean nonLocked){ this.nonLocked = nonLocked; }

    @JsonIgnore
    public int getFailedAttempts() {
        return failedAttempts;
    }

    public void setFailedAttempts(int failedAttempts) {
        this.failedAttempts = failedAttempts;
    }
}
