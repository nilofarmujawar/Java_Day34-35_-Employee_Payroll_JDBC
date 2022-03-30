package com.day34;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;


public class EmployeePayrollServiceTest {

    @Test
    public void given3Employees_WhenWrittenToFile_ShouldMatchEmployeeEntries() {
        EmployeePayrollData[] arrayOfEmp = { new EmployeePayrollData(1, "Bill", 100000.0),
                new EmployeePayrollData(2, "Mark ", 200000.0), new EmployeePayrollData(3, "Charlie", 300000.0) };
        EmployeePayrollService employeePayrollService;
        employeePayrollService = new EmployeePayrollService(Arrays.asList(arrayOfEmp));
        employeePayrollService.writeEmployeeData(EmployeePayrollService.IOService.FILE_IO);
        long entries = employeePayrollService.countEntries(EmployeePayrollService.IOService.FILE_IO);
        employeePayrollService.printData(EmployeePayrollService.IOService.FILE_IO);
        List<EmployeePayrollData> employeeList = employeePayrollService.readEmployeepayrollData(EmployeePayrollService.IOService.FILE_IO);
        System.out.println(employeeList);
        Assert.assertEquals(3, entries);
    }

    @Test
    public void givenEmployeePayrollInDB_WhenRetrieved_ShouldMatchEmployeeCount() {
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        List<EmployeePayrollData> employeePayrollData = employeePayrollService
                .readEmployeepayrollData(EmployeePayrollService.IOService.DB_IO);
        System.out.println(employeePayrollData);
        Assert.assertEquals(3, employeePayrollData.size());
    }


    @Test
    public void givenNewSalaryForEmployeeWhenupdatedShouldSyncWith_DB() throws EmployeePayrollException {
        EmployeePayrollService employeePayrollService = new EmployeePayrollService ();
        List<EmployeePayrollData> employeePayrollData = employeePayrollService
                .readEmployeepayrollData(EmployeePayrollService.IOService.DB_IO);
        employeePayrollService.updateEmployeeSalary("Terisa", 250000.0);
        boolean result = employeePayrollService.checkEmployeePayrollInSyncWithDB("Terisa");
        Assert.assertFalse(result);
        System.out.println(employeePayrollData);
    }

    @Test
    public void givenNewSalaryForEmployeeWhenUpdatedShouldSyncWith_DB() throws EmployeePayrollException {
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        List<EmployeePayrollData> employeePayrollData = employeePayrollService
                .readEmployeepayrollData(EmployeePayrollService.IOService.DB_IO);
        employeePayrollService.updateEmployeeSalary("Terisa", 350000.0);
        boolean result = employeePayrollService.checkEmployeePayrollInSyncWithDB("Terisa");
        Assert.assertFalse(result);
        System.out.println(employeePayrollData);
    }

}


