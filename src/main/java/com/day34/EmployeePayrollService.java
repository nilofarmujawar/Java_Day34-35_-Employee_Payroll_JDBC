package com.day34;

/**
 * UC1 :- Ability to create a payroll service database and have java program connect to database
 * UC2 :- Ability for Employee Payroll Service to retrieve the Employee Payroll from the Database
 * UC3 :- Ability to update the salary i.e. the base pay for Employee Terisa to 3000000.00 and sync it with Database
 * UC4 :- Ability to update the salary i.e. the base pay for Employee Terisa to 3000000.00 and sync it with Database
 *        using JDBC PreparedStatement
 * UC5 :- Ability to retrieve all employees who have joined in a particular data range from the payroll service database
 * UC6 :- Ability to find sum, average, min,max and number of male and female employees
 * UC7 :- Ability to add a new Employee to the Payroll
 * UC8 :- Ability to also add to payroll details when a new Employee is added to the Payroll
 * UC9 :- Implement the complete ER Diagram in the Database
 * UC10 :- Ensure UC 2 – UC 7  works with the new ER Diagram implemented into Payroll Service DB
 * UC11 :- Ability to add a new Employee to the Payroll
 * UC12 :- Ability to remove Employee from the Payroll
 *
 */

/**
 * import all classes
 */

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import com.day34.EmployeePayrollException.Exception;
import java.util.logging.Logger;

/**
 * create a class name as EmployeePayrollService
 */
public class EmployeePayrollService {
    /**
     * creating a enum class.
     * Enums can be thought of as classes which have a fixed set of constants (a variable that does not change).
     * The enum constants are static and final implicitly
     */
    public enum IOService {
        CONSOLE_IO, FILE_IO, DB_IO, REST_IO
    }

    /**
     * A Logger object is used to log messages for a specific system or application component.
     */
    Logger log = Logger.getLogger(EmployeePayrollService.class.getName());
    public List<EmployeePayrollData> employeePayrollList;
    private EmployeePayrollDbService employeePayrollDBService;
    private EmployeePayrollNewDBService employeePayrollNewDBService;
    private Map<String, Double> employeePayrollMap;

    /**
     * create default constructor name as EmployeePayrollService
     */
    public EmployeePayrollService() {
        employeePayrollDBService = EmployeePayrollDbService.getInstance();
        employeePayrollNewDBService = EmployeePayrollNewDBService.getInstance();
    }

    /**
     * create a parameterized construcor name as EmployeePayrollService
     * @param employeePayrollList in employee name.id,salary stored
     */
    public EmployeePayrollService(List<EmployeePayrollData> employeePayrollList) {
        this();
        this.employeePayrollList = employeePayrollList;
    }
    /**
     * create a parameterized construcor name as EmployeePayrollService
     * @param employeePayrollMap
     */
    public EmployeePayrollService(Map<String, Double> employeePayrollMap) {
        this();
        this.employeePayrollMap = employeePayrollMap;
    }

    /**
     * create a method name as readEmployeeData
     * @param consoleInputReader
     */
    public void readEmployeeData(Scanner consoleInputReader) {
        /**
         * Log an INFO message.
         * If the logger is currently enabled for the INFO message
         * level then the given message is forwarded to all the registered output Handler objects.
         */
        log.info("Enter employee ID : ");
        int id = Integer.parseInt(consoleInputReader.nextLine());
        log.info("Enter employee name : ");
        String name = consoleInputReader.nextLine();
        log.info("Enter employee salary : ");
        double salary = Double.parseDouble(consoleInputReader.nextLine());
        employeePayrollList.add(new EmployeePayrollData(id, name, salary));
    }

    public List<EmployeePayrollData> readEmployeepayrollData(IOService ioService) {
        if (ioService.equals(IOService.FILE_IO))
            return new EmployeePayrollFileIOService().readData();
        if (ioService.equals(IOService.DB_IO))
            return new EmployeePayrollNewDBService().readData();
        else
            return null;
    }

    /**
     * create a method name as readPayrollDataForRange
     * this is parameterized method
     * @param ioService
     * @param startDate
     * @param endDate
     * @return
     */
    public List<EmployeePayrollData> readPayrollDataForRange(IOService ioService, LocalDate startDate,
                                                             LocalDate endDate) {
        if (ioService.equals(IOService.DB_IO))
            this.employeePayrollList = employeePayrollNewDBService.readData();
        return employeePayrollList;
    }

    /**
     * create a method name as writeEmployeeData
     * @param ioService
     */
    public void writeEmployeeData(IOService ioService) {
        if (ioService.equals(IOService.CONSOLE_IO))
            log.info("Employee Payroll Data to Console\n" + employeePayrollList);
        else if (ioService.equals(IOService.FILE_IO))
            new EmployeePayrollFileIOService().writeData(employeePayrollList);
    }
    /**
     * create a method name as printData,
     * this is parameterized method
     * @param ioService
     */
    public void printData(IOService ioService) {
        new EmployeePayrollFileIOService().printData();
    }

    /**
     * create  a method name as countEntries,the method type is long
     * @param ioService
     * @return enteries
     */
    public long countEntries(IOService ioService) {
        if (ioService.equals(IOService.FILE_IO))
            return new EmployeePayrollFileIOService().countEntries();
        return employeePayrollList.size();
    }

    /**
     * create a method name as updateEmployeeSalary
     * @param name of employee
     * @param salary employee
     * @throws EmployeePayrollException
     * @throws NullPointerException
     */
    public void updateEmployeeSalary(String name, double salary) throws EmployeePayrollException {
        int result = employeePayrollNewDBService.updateEmployeeData(name, salary);
        if (result == 0) {
            throw new EmployeePayrollException(Exception.DATA_NULL, "No Data to update ");
        }
        EmployeePayrollData employeePayrollData = this.getEmployee_payroll_Data(name);
        if (employeePayrollData != null)
            employeePayrollData.salary = salary;
    }

    /**
     * create a method name as addEmployeeToPayRoll,this is parameterized method
     * @param name of employee
     * @param salary employee salary
     * @param start start date
     * @param gender employee gender
     */
    public void addEmployeeToPayRoll(String name, double salary, LocalDate start, String gender) {
        employeePayrollList.add(employeePayrollNewDBService.addEmployeeToPayroll(name, salary, start, gender));
    }

    /**
     * create a method name as getEmployee_payroll_Data
     * @param name of employee
     * @return name
     */
    private EmployeePayrollData getEmployee_payroll_Data(String name) {
        return this.employeePayrollList.stream().filter(emp_Data -> emp_Data.name.equals(name)).findFirst()
                .orElse(null);
    }

    /**
     * create a method name as checkEmployeePayrollInSyncWithDB
     * @param name of employee
     * @return equal name
     */
    public boolean checkEmployeePayrollInSyncWithDB(String name) {
        List<EmployeePayrollData> employeePayrollList = employeePayrollDBService.getEmployeePayrollData(name);
        return employeePayrollList.get(0).equals(getEmployee_payroll_Data(name));
    }

    /**
     * create a method name as readPayrollDataForAvgSalary
     * @param ioService
     * @return employeePayrollMap
     */
    public Map<String, Double> readPayrollDataForAvgSalary(IOService ioService) {
        if (ioService.equals(IOService.DB_IO))
            this.employeePayrollMap = employeePayrollDBService.get_AverageSalary_ByGender();
        return employeePayrollMap;
    }

    /**
     * create a map, create a method name as readPayrollDataForSumSalary
     * in this method read payroll data employee sum salary
     * @param ioService
     * @return employeePayrollMap
     */
    public Map<String, Double> readPayrollDataForSumSalary(IOService ioService) {
        if (ioService.equals(IOService.DB_IO))
            this.employeePayrollMap = employeePayrollDBService.get_SumOfSalary_ByGender();
        return employeePayrollMap;
    }

    /**
     * create a method name as readPayrollDataForMaxSalary
     * here read the data employee max salary
     * @param ioService
     * @return employeePayrollMap
     */
    public Map<String, Double> readPayrollDataForMaxSalary(IOService ioService) {
        if (ioService.equals(IOService.DB_IO))
            this.employeePayrollMap = employeePayrollNewDBService.get_Max_Salary_ByGender();
        return employeePayrollMap;
    }

    /**
     * create a method name as readPayrollDataForMinSalary
     * here read the data employee minimum salary
     * @param ioService
     * @return employeePayrollMap
     */
    public Map<String, Double> readPayrollDataForMinSalary(IOService ioService) {
        if (ioService.equals(IOService.DB_IO))
            this.employeePayrollMap = employeePayrollDBService.get_Min_Salary_ByGender();
        return employeePayrollMap;
    }

    /**
     * create a method name as readPayrollDataFor_CountOfEmployee_ByGender
     * method for count of employee by gender
     * @param ioService
     * @return
     */
    public Map<String, Double> readPayrollDataFor_CountOfEmployee_ByGender(IOService ioService) {
        if (ioService.equals(IOService.DB_IO))
            this.employeePayrollMap = employeePayrollNewDBService.get_CountOfEmployee_ByGender();
        return employeePayrollMap;
    }

    /**
     * create a method name as readPayrollDataForActiveEmployees
     * method for read payroll data for active employees
     * @param ioService
     * @return employeePayrollList
     */
    public List<EmployeePayrollData> readPayrollDataForActiveEmployees(IOService ioService) {
        if (ioService.equals(IOService.DB_IO))
            this.employeePayrollList = employeePayrollNewDBService.getActiveEmployees();
        return employeePayrollList;
    }

    /**
     * create a main method,all program execute in main method
     * @param args no arguments,its default.
     */
    public static void main(String[] args) {
        /**
         * create a list object name as  employeePayrollList
         * here EmployeePayrollData is a class.
         */
        List<EmployeePayrollData> employeePayrollList = new ArrayList<EmployeePayrollData>();
        /**
         * create a object for  EmployeePayrollService class ,object name as employeePayrollService
         */
        EmployeePayrollService employeePayrollService = new   EmployeePayrollService(employeePayrollList);
        /**
         * create a scanner class object name as object is consoleInputReader
         */
        Scanner consoleInputReader = new Scanner(System.in);
        /**
         * calling readEmployeeData method from employeePayrollService object
         */
        employeePayrollService.readEmployeeData(consoleInputReader);
        /**
         * calling writeEmployeeData method from employeePayrollService object
         */
        employeePayrollService.writeEmployeeData(IOService.CONSOLE_IO);
    }


}