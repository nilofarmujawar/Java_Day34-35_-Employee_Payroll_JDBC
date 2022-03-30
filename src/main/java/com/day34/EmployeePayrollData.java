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
    public LocalDate startDate;

    /**
     * create parameterized constructor name as EmployeePayrollData
     * @param id is employee
     * @param name is employee
     * @param salary is employee
     */
    public  EmployeePayrollData(int id, String name, double salary) {
        this.id = id;
        this.name = name;
        this.salary = salary;
    }

    /**
     * Create a constructor name as EmployeePayrollData, this is parameterized constructor
     * @param id is employee
     * @param name is employee
     * @param salary is employee
     * @param startDate of salary
     */
    public  EmployeePayrollData(int id, String name, double salary, LocalDate startDate) {
        this(id, name, salary);
        this.startDate = startDate;
    }

    /**
     * overiding method
     * @return id, name , salary
     */
    @Override
    public String toString() {
        return "Employee_payroll_Data [id=" + id + ", name=" + name + ", salary=" + salary + ", startDate=" + startDate
                + "]";
    }

    /**
     * overide equal method
     * @param obj of employee
     * @return employee id name and salary
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        EmployeePayrollData emp_payroll = (EmployeePayrollData) obj;
        return id == emp_payroll.id && Double.compare(emp_payroll.salary, salary) == 0 && name.equals(emp_payroll.name);
    }
}