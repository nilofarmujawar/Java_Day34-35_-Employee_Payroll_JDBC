package com.day34;

import java.time.LocalDate;

public class EmployeePayrollData {
    public int id;
    public String name;
    public double salary;
    public LocalDate startDate;

    public EmployeePayrollData(int id, String name, double salary) {
        this.id = id;
        this.name = name;
        this.salary = salary;
    }

    public EmployeePayrollData(int id, String name, double salary, LocalDate startDate) {
        this(id, name, salary);
        this.startDate = startDate;
    }

    @Override
    public String toString() {
        return "id =" + id + ",name =" + name + ",salary =" + salary;
    }

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
