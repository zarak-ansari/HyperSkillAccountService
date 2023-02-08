package account.auth;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class ChangeUserRoleDTO {

    @NotBlank
    private String user;

    @NotBlank
    private String role;

    @NotBlank @Pattern(regexp = "GRANT|REMOVE")
    private String operation;

    public ChangeUserRoleDTO() {
    }

    public ChangeUserRoleDTO(String user, String role, String operation) {
        this.user = user;
        this.role = role;
        this.operation = operation;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    //    @NotNull
//    private String user;
//
//    @NotNull @Pattern(regexp = "ADMINISTRATOR|ACCOUNTANT|USER")
//    private String role;
//
//    @NotNull @Pattern(regexp = "GRANT|REMOVE")
//    private String operation;
//
//    public ChangeUserRoleDTO() {
//    }
//
//    public ChangeUserRoleDTO(String user, String role, String operation) {
//        this.user = user;
//        this.role = role;
//        this.operation = operation;
//    }
//
//    public String getUser() {
//        return user;
//    }
//
//    public void setUser(String user) {
//        this.user = user;
//    }
//
//    public String getRole() {
//        return role;
//    }
//
//    public void setRole(String role) {
//        this.role = role;
//    }
//
//    public String getOperation() {
//        return operation;
//    }
//
//    public void setOperation(String operation) {
//        this.operation = operation;
//    }
}
