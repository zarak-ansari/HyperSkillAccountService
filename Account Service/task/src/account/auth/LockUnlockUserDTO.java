package account.auth;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class LockUnlockUserDTO {
    @NotBlank @NotNull
    private String user;

    @NotBlank
    @Pattern(regexp = "LOCK|UNLOCK")
    private String operation;

    public LockUnlockUserDTO() {
    }

    public LockUnlockUserDTO(String username, String operation) {
        this.user = username;
        this.operation = operation;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }
}
