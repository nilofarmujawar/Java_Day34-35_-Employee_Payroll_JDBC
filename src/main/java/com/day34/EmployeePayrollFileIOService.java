package com.day34;
/**
 * import all classes
 */
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * create a class name as EmployeePayrollFileIOService
 */
public class EmployeePayrollFileIOService {
    /**
     * give the file path where ur file is stored
     */
    public static String PAYROLL_FILE_NAME = "C:\\Users\\admin\\Desktop\\java\\day34EmpPayrollSQL\\Java_Day34_-Employee_Payroll_JDBC\\EmployeePayroll_mySQL.txt";

    /**
     * create a method name as writeData,this is parameterized method
     * @param employeePayrollList
     */
    public void writeData(List<EmployeePayrollData> employeePayrollList) {
        /**
         * create a object for stringbuffer class ,object name as empBuffer
         *
         * StringBuffer :-
         *  The principal operations on a StringBuffer are the append and insert methods,
         *   which are overloaded so as to accept data of any type.
         */
        StringBuffer empBuffer = new StringBuffer();
        employeePayrollList.forEach(employee -> {
            String employeeDataString = employee.toString().concat("\n");
            empBuffer.append(employeeDataString);
        });
        try {
            Files.write(Paths.get(PAYROLL_FILE_NAME), empBuffer.toString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * create a method name as print data
     */
    public void printData() {
        try {
            Files.lines(new File(PAYROLL_FILE_NAME).toPath()).forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * create a method name as countEntries
     * @return entries
     */
    public long countEntries() {
        long entries = 0;
        try {
            entries = Files.lines(new File(PAYROLL_FILE_NAME).toPath()).count();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return entries;
    }

    /**
     * create a list method name as readData
     * @return employeePayrollList
     */
    public List<EmployeePayrollData> readData() {
        /**
         * create a list object name as employeePayrollList
         */
        List<EmployeePayrollData> employeePayrollList = new ArrayList<EmployeePayrollData>();
        try {
            Files.lines(new File(PAYROLL_FILE_NAME).toPath()).map(line -> line.trim()).forEach(line -> {
                String data = line.toString();
                String[] dataArr = data.split(",");
                for (int i = 0; i < dataArr.length; i++) {
                    int id = Integer.parseInt(dataArr[i].replaceAll("id =", ""));
                    i++;
                    String name = dataArr[i].replaceAll("name =", "");
                    i++;
                    double salary = Double.parseDouble(dataArr[i].replaceAll("salary =", ""));
                    EmployeePayrollData employeePayrollData = new EmployeePayrollData(id, name, salary);
                    employeePayrollList.add(employeePayrollData);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        /**
         * return employeepayrolllist
         */
        return employeePayrollList;
    }
}