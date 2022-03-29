package com.day34;

/**
 * UC1 :- Ability to create a payroll service database and have java program connect to database
 *
 */

/**
 * import connection class
 * import driver class
 * import drivermanager class
 * import enumeration class
 */

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.util.Enumeration;

/**
 * create a class name as EmployeePayrollDB
 */
public class EmployeePayrollDB {

    /**
     * create a main method,  all program excecute in main method
     * @param args no arguments
     */
    public static void main(String[] args) {

        /**
         * jdbc connection with sql
         */
        String jdbcURL = "jdbc:mysql://localhost:3306/payroll_service?useSSL=false";
        /**
         * sql username
         */
        String userName = "root";
        /**
         * sql password
         */
        String password = "Mujawar#1118";
        Connection connection;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Driver Loaded Successfully");
        } catch (ClassNotFoundException exception) {
            throw new IllegalStateException("Cannot find driver in the classpath", exception);
        }
        listDrivers();
        try {
            System.out.println("Connecting to Database: " + jdbcURL);
            connection = DriverManager.getConnection(jdbcURL, userName, password);
            System.out.println("Connection is successful !!1: " + connection);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    /**
     * create a method name as listDrivers
     */
    private static void listDrivers() {
        /**
         * Enumeration :-
         *
         *  An object that implements the Enumeration interface generates a series of elements, one at a time.
         *  Successive calls to the nextElement method return successive elements of the series.
         */
        Enumeration<Driver> driverList = DriverManager.getDrivers();
        while (driverList.hasMoreElements()) {
            Driver driverClass = (Driver) driverList.nextElement();
            System.out.println("  " + driverClass.getClass().getName());
        }
    }
}