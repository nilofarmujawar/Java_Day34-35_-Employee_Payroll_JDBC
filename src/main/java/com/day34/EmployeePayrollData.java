package com.day34;

/**
 * import localdate class
 * import objects class
 */

import java.time.LocalDate;
import java.util.Objects;

/**
 * create class name as EmployeePayrollData
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
    public int departmentId;
    public String departmentName;
    public boolean active;

    /**
     * here using getter setter method,
     * The get method returns the variable value, and the set method sets the value.
     */


    /**
     * The get method returns the value of the variable id.
     * @return id
     */
    public int getId() {
        return id;
    }

    /**
     * The set method takes a parameter (id) and assigns it to the id variable
     * @param id-employee id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * The get method returns the value of the variable companyID
     * @return companyID
     */
    public int getCompanyId() {
        return companyId;
    }

    /**
     * The set method takes a parameter (companyId) and assigns it to the companyId variable
     * @param companyId -employee company id
     */
    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    /**
     * The get method returns the value of the variable department
     * @return department
     */
    public String[] getDepartment() {
        return department;
    }

    /**
     * The set method takes a parameter (department) and assigns it to the department variable
     * @param department-employee working department
     */
    public void setDepartment(String[] department) {
        this.department = department;
    }

    /**
     * create parameterized constructor name as EmployeePayrollData
     * @param id -employee id
     * @param name -employee name
     * @param salary - employee salary
     */
    public EmployeePayrollData(int id, String name, double salary) {
        /**
         * The this keyword is used to refer to the current object.
         */
        this.id = id;
        this.name = name;
        this.salary = salary;
    }

    /**
     * create a constructor
     * @param id- employee id
     * @param departmentId - employee department id
     */
    public EmployeePayrollData(int id, int departmentId) {
        /**
         * The this keyword is used to refer to the current object.
         */
        this.id = id;
        this.departmentId = departmentId;
    }

    /**
     * create a constructor ,this is parameterized method
     * @param id - employee id
     * @param name - employee name
     * @param salary - employee salary
     * @param start - employee start date
     */
    public EmployeePayrollData(int id, String name, double salary, LocalDate start) {
        /**
         * The this keyword is used to refer to the current object.
         */
        this(id, name, salary);
        this.start = start;
    }

    /**
     * create a parameterized constructor
     * @param id - employee id
     * @param name - employee name
     * @param salary - employee salary
     * @param start - employee start date
     * @param gender - employee gender
     */
    public EmployeePayrollData(int id, String name, double salary, LocalDate start, String gender) {
        /**
         * The this keyword is used to refer to the current object.
         */
        this(id, name, salary, start);
        this.gender = gender;
    }

    /**
     * create a parameterized constructor
     * @param id - employee id
     * @param name - employee name
     * @param salary - employee salary
     * @param start -employee start date
     * @param gender - employee gender
     * @param active - employee active
     */
    public EmployeePayrollData(int id, String name, double salary, LocalDate start, String gender, boolean active) {
        /**
         * The this keyword is used to refer to the current object.
         */
        this(id, name, salary, start, gender);
        this.active = active;
    }

    /**
     * create a parameterized constructor
     * @param id - employee id
     * @param name - employee name
     * @param salary- employee salary
     * @param start - employee start date
     * @param gender - employee gender
     * @param companyId - employee companyID
     */
    public EmployeePayrollData(int id, String name, double salary, LocalDate start, String gender, int companyId) {
        /**
         * The this keyword is used to refer to the current object.
         */
        this(id, name, salary, start, gender);
        this.companyId = companyId;
    }

    public EmployeePayrollData(int id, String name, double salary, LocalDate start, String gender,
                                 String[] department) {
        /**
         * The this keyword is used to refer to the current object.
         */
        this(id, name, salary, start, gender);
        this.department = department;
    }

    /**
     * create a parameterized constructor
     * @param id -employee id
     * @param name -employee name
     * @param salary -employee salary
     * @param start - employee start date
     * @param gender - employee gender
     * @param companyName - employee company name
     * @param companyId - employee companyID
     * @param department - employe department 
     */
    public EmployeePayrollData(int id, String name, double salary, LocalDate start, String gender, String companyName,
                                 int companyId, String[] department) {
        /**
         * The this keyword is used to refer to the current object.
         */
        this(id, name, salary, start, gender, department);
        this.companyName = companyName;
        this.companyId = companyId;
    }

    /**
     * create a constructor
     * @param departmentId -Employee departmentID
     * @param departmentName -Employee working department name
     */
    public EmployeePayrollData(int departmentId, String departmentName) {
        /**
         * The this keyword is used to refer to the current object.
         */
        this.departmentId = departmentId;
        this.departmentName = departmentName;
    }

    /**
     * overide hashcode method
     * @return name gender salary start
     */
    @Override
    public int hashCode() {
        return Objects.hash(name, gender, salary, start);
    }

    /**
     * overide to string method
     * @return id name salary startdate
     */
    @Override
    public String toString() {
        return "Employee_payroll_Data [id=" + id + ", name=" + name + ", salary=" + salary + ", startDate=" + start
                + "]";
    }

    /**
     * overide eqyals method
     * @param obj
     * @return id name salary
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        EmployeePayrollData that = (EmployeePayrollData) obj;
        return id == that.id && Double.compare(that.salary, salary) == 0 && name.equals(that.name);
    }

    /**
     * create a method name as printDepartments
     */
    public void printDepartments() {
        String departments[] = this.getDepartment();
        for (String s : departments) {
            System.out.println("id: " + this.getId() + ":" + s);
        }
    }
}