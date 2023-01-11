package com.company.model;

import com.company.enums.EmployeeStatus;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Employee {
    private String id;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private EmployeeStatus employeeStatus;
    private boolean isCustomer=false;

    private boolean isEmployee=false;
    private boolean isAdmin = false;


    public Employee(String id, String firstName, String lastName, String phoneNumber, String employeeStatus, boolean isCustomer, boolean isAdmin, boolean isEmployee) {
        this.id = id;
        this.firstName=firstName;
        this.lastName=lastName;
        this.phoneNumber=phoneNumber;
        this.employeeStatus= EmployeeStatus.valueOf(employeeStatus);
        this.isCustomer=isCustomer;
        this.isAdmin=isAdmin;
        this.isEmployee=isEmployee;
    }
}
