package com.day34;

/**
 * import local date class
 */

import java.time.LocalDate;

/**
 * create a class name as EmployeePayrollData
 */
public class EmployeePayrollData {
    /**
     * variables
     */
    public int id;
    public String name;
    public double salary;
    public LocalDate start;
    public String gender;
    public String companyName;
    public int companyId;
    public String department[];

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public String[] getDepartment() {
        return department;
    }

    public void setDepartment(String[] department) {
        this.department = department;
    }


    public EmployeePayrollData (int id, String name, double salary) {
        this.id = id;
        this.name = name;
        this.salary = salary;
    }

    public EmployeePayrollData (int id, String name, double salary, LocalDate start) {
        this(id, name, salary);
        this.start = start;
    }

    public EmployeePayrollData (int id, String name, double salary, LocalDate start, String gender) {
        this(id, name, salary, start);
        this.gender = gender;
    }

    public EmployeePayrollData (int id, String name, double salary, LocalDate start, String gender, String companyName,
                                 int companyId, String[] department) {
        this.id = id;
        this.name = name;
        this.salary = salary;
        this.start = start;
        this.gender = gender;
        this.companyName = companyName;
        this.companyId = companyId;
        this.department = department;
    }

    @Override
    public String toString() {
        return "Employee_payroll_Data [id=" + id + ", name=" + name + ", salary=" + salary + ", startDate=" + start
                + "]";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        EmployeePayrollData that = (EmployeePayrollData ) obj;
        return id == that.id  &&  Double.compare(that.salary, salary) == 0 && name.equals(that.name);
    }
}