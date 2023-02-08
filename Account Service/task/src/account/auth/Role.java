package account.auth;

import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Set;

@Entity
public class Role implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank @Column(unique=true)
    private String authority;

    @ManyToMany(mappedBy = "roles")
    private Set<AppUser> users;

    public Role(){}

    public Role(String name){
        this.authority = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    @Override
    public boolean equals(Object other) {

        /* Check if o is an instance of Complex or not
          "null instanceof [type]" also returns false */
        if (!(other instanceof Role)) {
            return false;
        }

        // If the object is compared with itself then return true
        if (other == this) {
            return true;
        }

        // typecast o to Complex so that we can compare data members
        Role otherRole = (Role) other;

        // Compare the data members and return accordingly
        return this.getAuthority().equals(otherRole.getAuthority());
    }
}
