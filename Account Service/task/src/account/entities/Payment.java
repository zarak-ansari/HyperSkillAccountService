package account.entities;

import javax.persistence.*;
import javax.validation.constraints.*;

@Entity
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "employee", "period" }) })
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotEmpty
    @Email
    private String employee;

    @NotEmpty
    @Pattern(regexp = "(1[0-2]|0[1-9])-[0-9]{4}", message = "Incorrect period format")
    private String period;

    @NotNull
    @Min(value = 0, message = "Salary must be non-negative")
    private Long salary;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmployee() {
        return employee;
    }

    public void setEmployee(String employee) {
        this.employee = employee;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public Long getSalary() {
        return salary;
    }

    public void setSalary(Long salary) {
        this.salary = salary;
    }
}
