package account.DTOs;

import javax.validation.constraints.*;

public class SignupDTO {

    @NotEmpty @NotNull
    private String name;
    @NotEmpty @NotNull
    private String lastname;
    @NotEmpty @NotNull @Email (regexp = ".+@acme\\.com$")
    private String email;
    @NotEmpty @NotNull
    @Size(min=12, message = "The password length must be at least 12 chars!")
    private String password;

    public SignupDTO() {
    }

    public SignupDTO(String name, String lastname, String email, String password) {
        this.name = name;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
    }

    public String getName() {
        return name;
    }

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
