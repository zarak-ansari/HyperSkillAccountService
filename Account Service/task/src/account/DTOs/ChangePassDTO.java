package account.DTOs;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class ChangePassDTO {

    @NotNull
    private String new_password;

    public ChangePassDTO(String new_password) {
        this.new_password = new_password;
    }
    public ChangePassDTO() {
    }

    public String getNew_password() {
        return new_password;
    }

    public void setNew_password(String new_password) {
        this.new_password = new_password;
    }
}
