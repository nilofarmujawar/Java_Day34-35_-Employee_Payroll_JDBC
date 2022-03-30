package com.day34;

/**
 * import arraylist class
 * import list class
 * import scanner class
 */

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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

    public List<EmployeePayrollData> employeePayrollList;

    public  EmployeePayrollService() {
    }

    public  EmployeePayrollService(List<EmployeePayrollData> employeePayrollList) {
        this.employeePayrollList = employeePayrollList;
    }

    public static void main(String[] args) {
        List<EmployeePayrollData> employeePayrollList = new ArrayList<EmployeePayrollData>();
        EmployeePayrollService employeePayrollService = new  EmployeePayrollService(employeePayrollList);
        Scanner consoleInputReader = new Scanner(System.in);
        employeePayrollService.readEmployeeData(consoleInputReader);
        employeePayrollService.writeEmployeeData(IOService.CONSOLE_IO);
    }

    public void readEmployeeData(Scanner consoleInputReader) {
        System.out.println("Enter employee ID : ");
        int id = Integer.parseInt(consoleInputReader.nextLine());
        System.out.println("Enter employee name : ");
        String name = consoleInputReader.nextLine();
        System.out.println("Enter employee salary : ");
        double salary = Double.parseDouble(consoleInputReader.nextLine());
        employeePayrollList.add(new EmployeePayrollData(id, name, salary));
    }

    public void writeEmployeeData(IOService ioService) {
        if (ioService.equals(IOService.CONSOLE_IO))
            System.out.println("Employee Payroll Data to Console\n" + employeePayrollList);
        else if (ioService.equals(IOService.FILE_IO))
            new EmployeePayrollFileIOService().writeData(employeePayrollList);
    }

    public void printData(IOService ioService) {
        new EmployeePayrollFileIOService().printData();
    }

    public long countEntries(IOService ioService) {
        if (ioService.equals(IOService.FILE_IO))
            return new EmployeePayrollFileIOService().countEntries();
        return 0;
    }

    public List<EmployeePayrollData> readEmployeepayrollData(IOService ioService) {
        if (ioService.equals(IOService.FILE_IO))
            return new EmployeePayrollFileIOService().readData();
        else if (ioService.equals(IOService.DB_IO))
            return new EmployeePayrollDbService().readData();
        else
            return null;
    }
}
